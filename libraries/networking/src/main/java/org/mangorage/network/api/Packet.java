package org.mangorage.network.api;

import org.mangorage.buffer.api.SimpleByteBuf;

public interface Packet {
    void encode(final SimpleByteBuf buffer);
    void handle(final Connection connection);

    PacketId<?> getId();
}
