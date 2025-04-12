package org.mangorage.game.network;

public final class Network {
    public static final int port = 25564;
    private static Connection playerConnection;

    static void setPlayerConnection(Connection connection) {
        Network.playerConnection = connection;
    }

    public static Connection getPlayerConnection() {
        return playerConnection;
    }
}
