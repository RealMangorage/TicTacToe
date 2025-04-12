package org.mangorage.game.network;
import org.mangorage.game.network.core.Direction;
import org.mangorage.game.network.packets.serverbound.C2SJoinPacket;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        initClient();
    }

    public static void initClient() {
        try (Socket socket = new Socket("localhost", 25564)) {
            Connection connection = new Connection(socket, Direction.C2S);

            connection.send(C2SJoinPacket.INSTANCE);
        } catch (IOException e) {
            System.err.println("The client tripped over its own feet: " + e.getMessage());
        }
    }
}