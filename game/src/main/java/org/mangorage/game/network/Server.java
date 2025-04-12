package org.mangorage.game.network;

import org.mangorage.network.api.Connection;
import org.mangorage.network.api.Direction;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public final class Server {
    private static volatile boolean runningServer = false;


    public static boolean isRunning() {
        return runningServer;
    }

    public static void initNewServer(final int port) {
        if (runningServer) return;
        new Thread(()-> initServer(port)).start();
    }

    static void initServer(final int port) {
        runningServer = true;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is up. Waiting for that one special snowflake...");

            while (runningServer) {
                // Accept one connection like the lazy bum it is
                Socket clientSocket = serverSocket.accept();
                System.out.println("COOL");

                if (Network.getPlayerConnection() == null) {
                    Network.setPlayerConnection(Connection.ofThreaded(clientSocket, Network.INSTANCE, Direction.CLIENT));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Network.setPlayerConnection(null);
        }
    }

    public static void stopServer() {
        runningServer = false;
    }
}
