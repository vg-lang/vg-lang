package components;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * SocketLib - TCP socket support for the VG language interpreter.
 *
 * CLIENT methods (connect → send/receive/close):
 *   connect(host, port)      -> socket-id (Long)
 *   send(socketId, msg)      -> true/false
 *   receive(socketId)        -> String (blocks up to 500 ms)
 *   close(socketId)          -> true/false
 *
 * SERVER methods (serverCreate → serverAccept → send/receive/close → serverClose):
 *   serverCreate(port)       -> server-id (Long), or -1 on failure
 *   serverAccept(serverId)   -> client socket-id (Long), or -1 if no client waiting
 *   serverClose(serverId)    -> true/false
 *
 * Client sockets returned by serverAccept share the same send/receive/close methods
 * as sockets returned by connect(), so no extra VG-side wrappers are needed.
 *
 * Server IDs start at 10001 to avoid collisions with client socket IDs.
 */
public class SocketLib {

    // ── client socket registry ────────────────────────────────────────────────
    private static final Map<Long, Socket>         sockets  = new HashMap<>();
    private static final Map<Long, BufferedWriter> writers  = new HashMap<>();
    private static final Map<Long, BufferedReader> readers  = new HashMap<>();
    private static long nextId = 1;

    // ── server socket registry ────────────────────────────────────────────────
    private static final Map<Long, ServerSocket> serverSockets = new HashMap<>();
    private static long nextServerId = 10001;   // keep IDs distinct from client IDs

    // ═════════════════════════════════════════════════════════════════════════
    // CLIENT METHODS
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Open a TCP connection to host:port.
     * @return a socket-id Long, or -1 on failure.
     */
    public static Object connect(Object hostObj, Object portObj) {
        String host = hostObj.toString();
        int port    = toInt(portObj);
        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress(host, port), 5000); // 5 s connect timeout
            sock.setSoTimeout(500); // 500 ms read timeout – non-blocking poll friendly

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(sock.getOutputStream(), "UTF-8"));
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(sock.getInputStream(), "UTF-8"));

            long id = nextId++;
            sockets.put(id, sock);
            writers.put(id, writer);
            readers.put(id, reader);
            return id;
        } catch (IOException e) {
            System.err.println("[SocketLib] connect failed: " + e.getMessage());
            return -1L;
        }
    }

    /**
     * Send a line of text. A newline is appended automatically.
     * @return true on success, false on failure.
     */
    public static Object send(Object idObj, Object msgObj) {
        long id = toLong(idObj);
        BufferedWriter writer = writers.get(id);
        if (writer == null) {
            System.err.println("[SocketLib] send: unknown socket id " + id);
            return false;
        }
        try {
            writer.write(msgObj.toString());
            writer.newLine();
            writer.flush();
            return true;
        } catch (IOException e) {
            System.err.println("[SocketLib] send failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Read one line from the socket (blocks up to the socket's SO_TIMEOUT).
     * @return the line String, or "" on timeout / error.
     */
    public static Object receive(Object idObj) {
        long id = toLong(idObj);
        BufferedReader reader = readers.get(id);
        if (reader == null) return "";
        try {
            String line = reader.readLine();
            return line != null ? line : "";
        } catch (java.net.SocketTimeoutException e) {
            return ""; // normal polling timeout
        } catch (IOException e) {
            System.err.println("[SocketLib] receive failed: " + e.getMessage());
            return "";
        }
    }

    /**
     * Close a client socket and free its resources.
     * @return true on success, false on failure.
     */
    public static Object close(Object idObj) {
        long id = toLong(idObj);
        try {
            BufferedWriter w = writers.remove(id);
            BufferedReader r = readers.remove(id);
            Socket         s = sockets.remove(id);
            if (w != null) w.close();
            if (r != null) r.close();
            if (s != null) s.close();
            return true;
        } catch (IOException e) {
            System.err.println("[SocketLib] close failed: " + e.getMessage());
            return false;
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // SERVER METHODS
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Create a TCP server socket that listens on the given port.
     *
     * The server socket uses a 50 ms accept() timeout so that serverAccept()
     * is non-blocking from the VG side – it returns -1 immediately if no
     * client is waiting, allowing the VG program to loop and poll.
     *
     * @param portObj  port number (int or Long or String)
     * @return  a server-id Long (≥ 10001), or -1 on failure.
     */
    public static Object serverCreate(Object portObj) {
        int port = toInt(portObj);
        try {
            ServerSocket ss = new ServerSocket();
            ss.setReuseAddress(true);
            ss.bind(new InetSocketAddress(port));
            ss.setSoTimeout(50); // non-blocking: accept() returns after 50 ms if no client

            long id = nextServerId++;
            serverSockets.put(id, ss);
            System.out.println("[SocketLib] Server listening on port " + port
                    + "  (server-id=" + id + ")");
            return id;
        } catch (IOException e) {
            System.err.println("[SocketLib] serverCreate failed: " + e.getMessage());
            return -1L;
        }
    }

    /**
     * Accept one pending client connection (non-blocking).
     *
     * Because the ServerSocket SO_TIMEOUT is 50 ms, this method returns within
     * ~50 ms whether or not a client is waiting. VG code should call this in a
     * polling loop until a valid id is returned.
     *
     * The returned socket-id can be used directly with send(), receive(), and
     * close() – no extra wrapping needed.
     *
     * @param serverIdObj  the server-id returned by serverCreate()
     * @return  a new client socket-id (Long), or -1 if no client connected yet.
     */
    public static Object serverAccept(Object serverIdObj) {
        long serverId = toLong(serverIdObj);
        ServerSocket ss = serverSockets.get(serverId);
        if (ss == null) {
            System.err.println("[SocketLib] serverAccept: unknown server-id " + serverId);
            return -1L;
        }
        try {
            Socket client = ss.accept(); // blocks ≤ 50 ms (see serverCreate)
            // Use a short read timeout so the VG relay loop stays responsive
            client.setSoTimeout(100);

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(client.getOutputStream(), "UTF-8"));
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(client.getInputStream(), "UTF-8"));

            long clientId = nextId++;
            sockets.put(clientId, client);
            writers.put(clientId, writer);
            readers.put(clientId, reader);
            System.out.println("[SocketLib] Client accepted  (socket-id=" + clientId + ")");
            return clientId;
        } catch (java.net.SocketTimeoutException e) {
            return -1L; // no pending connection – caller should keep polling
        } catch (IOException e) {
            System.err.println("[SocketLib] serverAccept failed: " + e.getMessage());
            return -1L;
        }
    }

    /**
     * Close a server socket.  Client sockets previously returned by
     * serverAccept() are NOT closed – use close() on each of them separately.
     *
     * @param serverIdObj  the server-id returned by serverCreate()
     * @return true on success, false on failure.
     */
    public static Object serverClose(Object serverIdObj) {
        long serverId = toLong(serverIdObj);
        try {
            ServerSocket ss = serverSockets.remove(serverId);
            if (ss != null) {
                ss.close();
                System.out.println("[SocketLib] Server closed  (server-id=" + serverId + ")");
            }
            return true;
        } catch (IOException e) {
            System.err.println("[SocketLib] serverClose failed: " + e.getMessage());
            return false;
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ═════════════════════════════════════════════════════════════════════════

    private static long toLong(Object obj) {
        if (obj instanceof Long)   return (Long) obj;
        if (obj instanceof Number) return ((Number) obj).longValue();
        return Long.parseLong(obj.toString());
    }

    private static int toInt(Object obj) {
        if (obj instanceof Number) return ((Number) obj).intValue();
        try { return Integer.parseInt(obj.toString()); }
        catch (NumberFormatException e) { return 0; }
    }
}