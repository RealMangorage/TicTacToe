package org.mangorage.game.network;

import org.mangorage.buffer.api.SimpleByteBuf;
import org.mangorage.game.network.core.Direction;
import org.mangorage.game.network.core.PacketHandler;
import org.mangorage.game.network.packets.Packet;
import java.io.IOException;
import java.net.Socket;

public final class Connection {
    private final Socket socket;
    private final Direction incomingDirection;

    Connection(Socket socket, Direction incomingDirection) {
        this.socket = socket;
        this.incomingDirection = incomingDirection;

        handleIncoming();
    }

    public void handleIncoming() {
        new Thread(() -> {
            try {
                var input = socket.getInputStream();

                while (!socket.isClosed()) {
                    var data = input.readAllBytes();
                    if (data.length == 0) continue;
                    PacketHandler.INSTANCE.decodePacket(SimpleByteBuf.wrap(data)).ifPresent(packet -> {
                        PacketHandler.INSTANCE.handlePacket(packet, this);
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

    public void send(Packet packet) {
        try {
            var os = socket.getOutputStream();
            var buf = PacketHandler.INSTANCE.encodePacket(packet);
            os.write(buf.array());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
