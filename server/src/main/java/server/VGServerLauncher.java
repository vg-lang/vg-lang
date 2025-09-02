package server;

import org.eclipse.lsp4j.launch.LSPLauncher;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.google.gson.Gson;

public class VGServerLauncher {
    public static void main(String[] args) throws IOException {
        // Start the LSP server
        VGLanguageServer server = new VGLanguageServer();
        LSPLauncher.createServerLauncher(server, System.in, System.out).startListening();

        // Start the socket server
        int port = 5000; // Port for socket communication
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Socket server started on port " + port);

            // Use a thread pool to handle multiple clients
            ExecutorService threadPool = Executors.newCachedThreadPool();
            Gson gson = new Gson();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(() -> handleClient(clientSocket, gson));
            }
        } catch (IOException e) {
            System.err.println("Error starting socket server: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket, Gson gson) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            String request;
            while ((request = in.readLine()) != null) {
                // Parse the JSON request
                Request jsonRequest = gson.fromJson(request, Request.class);

                // Handle the request and generate a response
                Response response = handleRequest(jsonRequest);

                // Send the response back to the client
                out.write(gson.toJson(response));
                out.newLine();
                out.flush();
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }

    private static Response handleRequest(Request request) {
        // Example: Handle different methods
        if ("execute".equals(request.method)) {
            // Execute code (placeholder logic)
            String result = "Executed: " + request.params.get("code");
            return new Response(result, null);
        } else {
            return new Response(null, "Unknown method: " + request.method);
        }
    }

    // Inner classes for JSON parsing
    private static class Request {
        String method;
        java.util.Map<String, String> params;
    }

    private static class Response {
        String result;
        String error;

        Response(String result, String error) {
            this.result = result;
            this.error = error;
        }
    }
}
