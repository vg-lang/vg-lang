#!/usr/bin/env python3
"""
tetris_ai_server_async.py
-------------------------
Async-cached Tetris AI server.

The VG game gets instant responses (no blocking).
A background thread keeps fetching fresh moves from LM Studio.

Usage:
  python tetris_ai_server_async.py
  python tetris_ai_server_async.py --ai claude
  python tetris_ai_server_async.py --ai gpt
"""

import socketserver
import json
import os
import argparse
import traceback
import threading
import urllib.request

HOST = "127.0.0.1"
PORT = 54321
AI_BACKEND = "lmstudio"

BOARD_WIDTH  = 10
BOARD_HEIGHT = 20

# ── shared state ──────────────────────────────────────────────────────────────
# The background thread writes here; the TCP handler reads from here instantly.

_lock           = threading.Lock()
_cached_moves   = ["hard_drop"]      # last good move list
_last_board     = None               # last board state we fetched moves for
_fetch_pending  = False              # is a fetch already in flight?

# ── AI back-ends ──────────────────────────────────────────────────────────────

def ask_lmstudio(prompt: str) -> list:
    payload = json.dumps({
        "model": "local-model",
        "messages": [{"role": "user", "content": prompt}],
        "max_tokens": 256,
        "temperature": 0.2,
        "stream": False
    }).encode("utf-8")

    req = urllib.request.Request(
        "http://localhost:1234/v1/chat/completions",
        data=payload,
        headers={"Content-Type": "application/json"},
        method="POST"
    )
    with urllib.request.urlopen(req, timeout=30) as resp:
        data = json.loads(resp.read().decode("utf-8"))
        return data["choices"][0]["message"]["content"].strip()


def ask_claude(prompt: str) -> str:
    import anthropic
    client = anthropic.Anthropic(api_key=os.environ["ANTHROPIC_API_KEY"])
    msg = client.messages.create(
        model="claude-sonnet-4-6",
        max_tokens=256,
        messages=[{"role": "user", "content": prompt}],
    )
    return msg.content[0].text.strip()


def ask_openai(prompt: str) -> str:
    from openai import OpenAI
    client = OpenAI(api_key=os.environ["OPENAI_API_KEY"])
    resp = client.chat.completions.create(
        model="gpt-4o",
        max_tokens=256,
        messages=[{"role": "user", "content": prompt}],
    )
    return resp.choices[0].message.content.strip()


def ask_gemini(prompt: str) -> str:
    import google.generativeai as genai
    GEMINI_API_KEY = "AIzaSyAbxTI-WB0gMffjToYfR2V5UcDADjkNhlA"
    genai.configure(api_key=GEMINI_API_KEY)
    model = genai.GenerativeModel("gemini-1.5-flash-latest")
    return model.generate_content(prompt).text.strip()


def call_ai(prompt: str) -> str:
    if AI_BACKEND == "lmstudio": return ask_lmstudio(prompt)
    if AI_BACKEND == "claude":   return ask_claude(prompt)
    if AI_BACKEND == "gpt":      return ask_openai(prompt)
    if AI_BACKEND == "gemini":   return ask_gemini(prompt)
    return '["hard_drop"]'


# ── prompt & board rendering ──────────────────────────────────────────────────

def render_board(board, shape, px, py) -> str:
    display = [row[:] for row in board] if board else \
              [[0]*BOARD_WIDTH for _ in range(BOARD_HEIGHT)]
    for r, row in enumerate(shape):
        for c, cell in enumerate(row):
            if cell:
                bx, by = px + c, py + r
                if 0 <= by < BOARD_HEIGHT and 0 <= bx < BOARD_WIDTH:
                    display[by][bx] = "P"
    return "\n".join(
        "".join("P" if c == "P" else ("X" if c else ".") for c in row)
        for row in display
    )


def build_prompt(board_json: str) -> str:
    try:
        state = json.loads(board_json)
    except Exception:
        state = {}

    board_str = render_board(
        state.get("board", []),
        state.get("shape", []),
        state.get("piece_x", 0),
        state.get("piece_y", 0),
    )

    return f"""You are playing Tetris. Analyse the board and decide the best moves for the current piece.

BOARD (X = locked block, . = empty, P = current piece):
{board_str}

Current piece : type={state.get("piece_type","?")}  x={state.get("piece_x",0)}  y={state.get("piece_y",0)}  rotation={state.get("rotation",0)}
Next piece    : {state.get("next_type","?")}
Score: {state.get("score",0)}  Level: {state.get("level",1)}

AVAILABLE MOVES (each usable multiple times):
  left, right, rotate_cw, rotate_ccw, soft_drop, hard_drop

Rules:
- ALWAYS end with hard_drop — it is mandatory.
- Max 20 moves total.
- You MUST consider rotating the piece first before moving it left/right.
- NEVER repeat left or right more than 4 times in a row — the board is only 10 wide.
- Goal: keep the stack as LOW and FLAT as possible. Fill holes. Don't pile up on one side.
- Think: what rotation makes this piece fit best? Then move it there.
- Reply with ONLY a JSON array, no explanation. Example: ["rotate_cw","left","left","hard_drop"]
"""


VALID_MOVES = {"left", "right", "rotate_cw", "rotate_ccw", "soft_drop", "hard_drop"}

def parse_moves(raw: str) -> list:
    try:
        text = raw.strip()
        # strip markdown fences
        if "```" in text:
            lines = text.split("\n")
            text = "\n".join(l for l in lines if not l.startswith("```")).strip()
        moves = json.loads(text)
        if isinstance(moves, list):
            filtered = [m for m in moves if m in VALID_MOVES and m != "hard_drop"]
            # Cap any single move repeating more than 4 times in a row
            capped = []
            streak = 0
            last = None
            for m in filtered:
                if m == last:
                    streak += 1
                else:
                    streak = 1
                    last = m
                if streak <= 4:
                    capped.append(m)
            capped.append("hard_drop")
            return capped[:20]
    except Exception as e:
        print(f"[ai] parse error: {e}  raw={raw[:80]}")
    return ["hard_drop"]


# ── background fetch thread ───────────────────────────────────────────────────

def fetch_moves_background(board_json: str):
    """Called in a daemon thread. Fetches moves and updates the cache."""
    global _cached_moves, _fetch_pending
    try:
        prompt = build_prompt(board_json)
        raw    = call_ai(prompt)
        moves  = parse_moves(raw)
        with _lock:
            _cached_moves  = moves
            _fetch_pending = False
        print(f"[ai] cached moves: {moves}")
    except Exception as e:
        print(f"[ai] fetch error: {e}")
        with _lock:
            _fetch_pending = False


# ── TCP handler ───────────────────────────────────────────────────────────────

class TetrisAIHandler(socketserver.StreamRequestHandler):

    def handle(self):
        global _last_board, _fetch_pending, _cached_moves

        try:
            line = self.rfile.readline().decode("utf-8").strip()
            if not line:
                self.wfile.write(b'["hard_drop"]\n')
                return

            print(f"[server] board received ({len(line)} bytes)")

            # 1. Return cached moves IMMEDIATELY — no waiting
            with _lock:
                moves_to_send  = list(_cached_moves)
                board_changed  = (line != _last_board)
                already_fetching = _fetch_pending

            response = json.dumps(moves_to_send) + "\n"
            self.wfile.write(response.encode("utf-8"))
            print(f"[server] sent (cached): {moves_to_send}")

            # 2. If board changed and no fetch in flight, kick off background fetch
            if board_changed and not already_fetching:
                with _lock:
                    _last_board   = line
                    _fetch_pending = True
                t = threading.Thread(target=fetch_moves_background,
                                     args=(line,), daemon=True)
                t.start()

        except Exception as e:
            print(f"[server] handler error: {e}")
            traceback.print_exc()
            try:
                self.wfile.write(b'["hard_drop"]\n')
            except Exception:
                pass


# ── main ──────────────────────────────────────────────────────────────────────

def main():
    global AI_BACKEND

    parser = argparse.ArgumentParser(description="Tetris AI Server (async cache)")
    parser.add_argument("--ai", default="lmstudio",
                        choices=["lmstudio", "claude", "gpt", "gemini"])
    parser.add_argument("--host", default=HOST)
    parser.add_argument("--port", default=PORT, type=int)
    args = parser.parse_args()

    AI_BACKEND = args.ai

    print(f"[server] Tetris AI Server  (async cache mode)")
    print(f"[server] Backend : {AI_BACKEND.upper()}")
    print(f"[server] Address : {args.host}:{args.port}")
    print(f"[server] Game gets instant responses — AI fetches in background")
    print()

    if AI_BACKEND == "lmstudio":
        print(f"[server] LM Studio → http://localhost:1234  (make sure it's running!)")
    else:
        keys = {"claude":"ANTHROPIC_API_KEY","gpt":"OPENAI_API_KEY","gemini":"GEMINI_API_KEY"}
        k = keys[AI_BACKEND]
        if not os.environ.get(k):
            print(f"[server] WARNING: {k} not set!")
    print()

    socketserver.TCPServer.allow_reuse_address = True
    with socketserver.TCPServer((args.host, args.port), TetrisAIHandler) as server:
        try:
            server.serve_forever()
        except KeyboardInterrupt:
            print("\n[server] Shutting down.")


if __name__ == "__main__":
    main()
