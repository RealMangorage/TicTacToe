package org.mangorage.network.internal;

import org.mangorage.buffer.api.SimpleByteBuf;
import org.mangorage.network.api.Connection;
import org.mangorage.network.api.Direction;
import org.mangorage.network.api.Packet;
import org.mangorage.network.api.PacketHandler;

import java.io.IOException;
import java.net.Socket;

public final class ConnectionImpl implements Connection {
    private final Socket socket;
    private final PacketHandler packetHandler;
    private final Direction incomingDirection;

    public ConnectionImpl(final Socket socket, final PacketHandler packetHandler, final Direction incomingDirection) {
        this.socket = socket;
        this.packetHandler = packetHandler;
        this.incomingDirection = incomingDirection;

        handleIncoming();
    }

    public void handleIncoming() {
        new Thread(() -> {
            try {
                final var input = socket.getInputStream();

                while (!socket.isClosed()) {
                    final var data = input.readAllBytes();
                    if (data.length == 0) continue;
                    packetHandler.decodePacket(SimpleByteBuf.wrap(data)).ifPresent(packet -> {
                        packetHandler.handlePacket(packet, this);
                    });
                }
            } catch (IOException e) {
                System.err.println("Client gave up: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {
                    // LOL, of course this fails silently
                }
            }
        }, "Connection-Handler-" + incomingDirection).start();
    }

    public Direction getIncomingDirection() {
        return incomingDirection;
    }

    public void send(final Packet packet) {
        try {
            final var os = socket.getOutputStream();
            final var buf = packetHandler.encodePacket(packet);
            os.write(buf.array());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
