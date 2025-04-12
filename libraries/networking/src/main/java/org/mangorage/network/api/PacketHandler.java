package org.mangorage.network.api;

import org.mangorage.buffer.api.SimpleByteBuf;

import java.util.Optional;

public sealed interface PacketHandler permits org.mangorage.network.internal.PacketHandlerImpl {
    SimpleByteBuf encodePacket(final Packet packet);
    Optional<Packet> decodePacket(final SimpleByteBuf smartByteBuf);
    void handlePacket(final Packet packet, final Connection connection);
}
