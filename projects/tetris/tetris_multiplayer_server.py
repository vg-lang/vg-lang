#!/usr/bin/env python3
"""
tetris_multiplayer_server.py
-----------------------------
Relay server for 2-player Tetris.

- Waits for exactly 2 players to connect
- Each player sends their board state every tick
- Server relays opponent's board state back instantly
- Detects game over and announces winner by score

Protocol (each message is one line of JSON):
  Player -> Server:  {"type":"state", "board":[[...]], "score":0, "level":1, "game_over":false}
  Server -> Player:  {"type":"opponent", "board":[[...]], "score":0, "level":1, "game_over":false}
  Server -> Player:  {"type":"start", "player":1}
  Server -> Player:  {"type":"result", "winner":1, "your_score":100, "opponent_score":50}

Usage:
  python tetris_multiplayer_server.py
  python tetris_multiplayer_server.py --port 54322

Then expose with ngrok:
  ngrok tcp 54322
"""

import socket
import threading
import json
import argparse
import time

HOST = "0.0.0.0"
PORT = 54322

# ── shared state ──────────────────────────────────────────────────────────────
players      = [None, None]   # [socket_p1, socket_p2]
player_files = [None, None]   # [file_p1, file_p2]
player_states = [None, None]  # latest board state from each player
player_names  = ["Player 1", "Player 2"]
lock         = threading.Lock()
game_started = False
game_over    = False

# ── helpers ───────────────────────────────────────────────────────────────────

def send_json(player_idx, obj):
    try:
        line = json.dumps(obj) + "\n"
        players[player_idx].sendall(line.encode("utf-8"))
    except Exception as e:
        print(f"[server] send error to player {player_idx+1}: {e}")

def broadcast(obj):
    for i in range(2):
        if players[i]:
            send_json(i, obj)

# ── player handler thread ─────────────────────────────────────────────────────

def handle_player(idx):
    global game_over

    opponent = 1 - idx
    print(f"[server] Player {idx+1} connected")

    try:
        f = player_files[idx]

        # Wait for opponent to connect — send periodic pings so client stays alive
        send_json(idx, {"type": "waiting", "message": "Waiting for opponent..."})
        waited = 0
        while players[opponent] is None:
            time.sleep(1.0)
            waited += 1
            if waited % 3 == 0:
                send_json(idx, {"type": "waiting", "message": "Still waiting..."})

        # Both connected — keep sending start message every second for 10 seconds
        # so the client polling timer is guaranteed to catch it
        print(f"[server] Player {idx+1} notified of game start")
        for _ in range(10):
            send_json(idx, {"type": "start", "player": idx + 1,
                            "message": f"Opponent connected! You are Player {idx+1}. Get ready!"})
            time.sleep(0.5)

        # Main relay loop
        while True:
            line = f.readline()
            if not line:
                print(f"[server] Player {idx+1} disconnected")
                break

            line = line.decode("utf-8").strip()
            if not line:
                continue

            try:
                msg = json.loads(line)
            except json.JSONDecodeError:
                continue

            if msg.get("type") == "state":
                # Store this player's state
                with lock:
                    player_states[idx] = msg

                # Relay to opponent immediately
                if players[opponent]:
                    relay = {
                        "type":      "opponent",
                        "board":     msg.get("board", []),
                        "score":     msg.get("score", 0),
                        "level":     msg.get("level", 1),
                        "game_over": msg.get("game_over", False),
                        "name":      player_names[idx]
                    }
                    send_json(opponent, relay)

                # Check if this player just lost
                if msg.get("game_over") and not game_over:
                    with lock:
                        if not game_over:
                            game_over = True
                            my_score  = msg.get("score", 0)
                            opp_state = player_states[opponent]
                            opp_score = opp_state.get("score", 0) if opp_state else 0

                            if my_score > opp_score:
                                winner = idx + 1
                            elif opp_score > my_score:
                                winner = opponent + 1
                            else:
                                winner = 0  # draw

                            result_me = {
                                "type":           "result",
                                "winner":         winner,
                                "your_score":     my_score,
                                "opponent_score": opp_score
                            }
                            result_opp = {
                                "type":           "result",
                                "winner":         winner,
                                "your_score":     opp_score,
                                "opponent_score": my_score
                            }
                            send_json(idx, result_me)
                            if players[opponent]:
                                send_json(opponent, result_opp)

                            print(f"[server] Game over! Winner: Player {winner}")
                            print(f"[server]   Player 1 score: {my_score if idx==0 else opp_score}")
                            print(f"[server]   Player 2 score: {opp_score if idx==0 else my_score}")

    except Exception as e:
        print(f"[server] Player {idx+1} error: {e}")
    finally:
        with lock:
            players[idx] = None
            player_files[idx] = None
        print(f"[server] Player {idx+1} cleaned up")


# ── main ──────────────────────────────────────────────────────────────────────

def main():
    global players, player_files, player_states, player_names, game_started, game_over

    parser = argparse.ArgumentParser()
    parser.add_argument("--port", type=int, default=PORT)
    parser.add_argument("--host", default=HOST)
    args = parser.parse_args()

    server_sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server_sock.bind((args.host, args.port))
    server_sock.listen(2)

    print(f"[server] Tetris Multiplayer Server")
    print(f"[server] Listening on {args.host}:{args.port}")
    print(f"[server] Expose with:  ngrok tcp {args.port}")
    print(f"[server] Waiting for 2 players...")
    print()

    session = 0
    while True:
        # Reset for new game session
        players       = [None, None]
        player_files  = [None, None]
        player_states = [None, None]
        game_started  = False
        game_over     = False
        session      += 1
        print(f"[server] === Session {session} — waiting for players ===")

        threads = []
        for i in range(2):
            conn, addr = server_sock.accept()
            print(f"[server] Player {i+1} connected from {addr}")
            players[i]      = conn
            player_files[i] = conn.makefile("rb")
            t = threading.Thread(target=handle_player, args=(i,), daemon=True)
            threads.append(t)
            t.start()

        # Wait for both threads to finish
        for t in threads:
            t.join()

        print(f"[server] Session {session} ended. Restarting...")
        print()


if __name__ == "__main__":
    main()