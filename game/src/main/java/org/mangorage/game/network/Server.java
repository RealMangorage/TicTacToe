package org.mangorage.game.network;

import org.mangorage.game.network.core.Direction;
import org.mangorage.game.players.RemotePlayer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final RemotePlayer.Properties PROPERTIES = new RemotePlayer.Properties();
    public static final RemotePlayer REMOTE_PLAYER = new RemotePlayer(PROPERTIES);

    public static void initServer() {
        try (ServerSocket serverSocket = new ServerSocket(Network.port)) {
            System.out.println("Server is up. Waiting for that one special snowflake...");

            while (true) {
                // Accept one connection like the lazy bum it is
                Socket clientSocket = serverSocket.accept();
                System.out.println("COOL");

                if (Network.getPlayerConnection() == null) {
                    Network.setPlayerConnection(new Connection(clientSocket, Direction.C2S));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        initServer();
    }
}
