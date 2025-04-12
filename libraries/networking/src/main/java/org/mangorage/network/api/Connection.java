package org.mangorage.network.api;

import org.mangorage.network.internal.ConnectionImpl;

import java.net.Socket;

public interface Connection {
    static Connection of(Socket socket, PacketHandler packetHandler, Direction incomingDirection) {
        return new ConnectionImpl(socket, packetHandler, incomingDirection);
    }

    void send(final Packet packet);
    Direction getIncomingDirection();
}
