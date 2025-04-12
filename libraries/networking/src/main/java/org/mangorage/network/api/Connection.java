package org.mangorage.network.api;

import org.mangorage.network.internal.ConnectionImpl;

import java.net.Socket;

public interface Connection {
    static Connection of(final Socket socket, final PacketHandler packetHandler, final Direction incomingDirection) {
        return new ConnectionImpl(socket, packetHandler, incomingDirection, false);
    }

    static Connection ofThreaded(final Socket socket, final PacketHandler packetHandler, final Direction icomingDirection) {
        return new ConnectionImpl(socket, packetHandler, icomingDirection, true);
    }

    void send(final Packet packet);
    Direction getIncomingDirection();
}
