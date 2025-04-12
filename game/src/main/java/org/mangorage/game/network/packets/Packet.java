package org.mangorage.game.network.packets;

import org.mangorage.buffer.api.SimpleByteBuf;
import org.mangorage.game.network.Connection;

public interface Packet {
    void encode(SimpleByteBuf buffer);
    void handle(Connection connection);

    PacketId<?> getId();
}
