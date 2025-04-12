package org.mangorage.network.internal;

import org.mangorage.buffer.api.SimpleByteBuf;
import org.mangorage.network.api.Connection;
import org.mangorage.network.api.Direction;
import org.mangorage.network.api.Packet;
import org.mangorage.network.api.PacketHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

public final class ConnectionImpl implements Connection {
    private final Socket socket;
    private final PacketHandler packetHandler;
    private final Direction incomingDirection;

    public ConnectionImpl(final Socket socket, final PacketHandler packetHandler, final Direction incomingDirection, boolean threaded) {
        this.socket = socket;
        this.packetHandler = packetHandler;
        this.incomingDirection = incomingDirection;

        handleIncoming(threaded);
    }

    public void handleIncoming(final boolean threaded) {
        Runnable runnable = () -> {
            try {
                final var input = socket.getInputStream();
                final var outputBuffer = new ByteArrayOutputStream();
                final var buffer = new byte[1024];

                while (socket.isConnected()) {
                    int read = input.read(buffer);
                    if (read == -1) break; // Server closed stream

                    outputBuffer.write(buffer, 0, read);

                    byte[] data = outputBuffer.toByteArray();
                    outputBuffer.reset();

                    if (data.length > 0) {
                        packetHandler.decodePacket(SimpleByteBuf.wrap(data))
                                .ifPresent(packet -> packetHandler.handlePacket(packet, this));
                    }
                }
            } catch (IOException e) {
                System.err.println("Connection gave up: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {
                    // socket dies quietly, like your last brain cell
                }
            }
        };

        if (threaded) {
            new Thread(runnable).start();
        } else {
            runnable.run();
        }
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
