package org.mangorage.game.network;
import org.mangorage.game.network.packets.serverbound.C2SJoinPacket;
import org.mangorage.network.api.Connection;
import org.mangorage.network.api.Direction;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(final String[] args) {
        initClient("localhost", 25564);
    }

    public static void initClient(final String host, final int port) {
        try (Socket socket = new Socket(host, port)) {
            Connection connection = Connection.of(socket, Network.INSTANCE, Direction.C2S);

            connection.send(C2SJoinPacket.INSTANCE);
        } catch (IOException e) {
            System.err.println("The client tripped over its own feet: " + e.getMessage());
        }
    }
}