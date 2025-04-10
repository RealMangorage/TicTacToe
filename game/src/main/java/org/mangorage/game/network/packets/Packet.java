package org.mangorage.game.network.packets;

import org.mangorage.buffer.api.SimpleByteBuf;

public interface Packet {
    void encode(SimpleByteBuf buffer);
    void handle();

    PacketId<?> getId();
}
