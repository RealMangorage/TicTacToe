package org.mangorage.game.network;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String host = "localhost"; // Change this if you’re networking across the void
        int port = 12345;

        try (Socket socket = new Socket(host, port)) {
            System.out.println("Connected to the server. Whoop-de-doo.");

            // Send a "packet" (aka a line of text, don't get fancy now)
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Hey server, here's your precious packet. Hope you're happy.");

            // Optionally read the response, because maybe the server had something to say for once
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            System.out.println("Server responded with: " + response);

        } catch (IOException e) {
            System.err.println("The client tripped over its own feet: " + e.getMessage());
        }
    }
}