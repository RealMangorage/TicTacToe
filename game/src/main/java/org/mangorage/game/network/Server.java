package org.mangorage.game.network;

import org.mangorage.buffer.api.SimpleByteBuf;
import org.mangorage.game.players.RemotePlayer;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int PORT = 12345;
    private static final RemotePlayer.Properties PROPERTIES = new RemotePlayer.Properties();
    public static final RemotePlayer REMOTE_PLAYER = new RemotePlayer(PROPERTIES);

    public static void initServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is up. Waiting for that one special snowflake...");

            // Accept one connection like the lazy bum it is
            Socket clientSocket = serverSocket.accept();
            System.out.println("Gotcha! Connection accepted from: " + clientSocket.getInetAddress());

            // Send a stupid little message and then bail
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("Congratulations, you're the chosen one. No one else gets in.");

            // Clean your mess
            clientSocket.close();
            System.out.println("Connection closed. Server's done. Don't ask it to work more.");
        } catch (IOException e) {
            System.err.println("You broke it: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {

        var buf = new SimpleByteBuf();
        buf.writeString("Hello!");
        buf.writeString("Hello 2!");
        System.out.println(
                buf.readString()
        );
        System.out.println(
                buf.readString()
        );

    }
}
