package org.mangorage.game.network.packets;

import org.mangorage.game.network.core.SmartByteBuf;

public interface Packet {
    void encode(SmartByteBuf buffer);
    void handle();

    PacketId getId();
}
