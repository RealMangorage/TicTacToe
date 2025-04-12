package org.mangorage.network.api;

import org.mangorage.buffer.api.SimpleByteBuf;
import org.mangorage.network.internal.PacketHandlerImpl;

import java.util.function.Function;

public sealed interface PacketHandlerBuilder permits PacketHandlerImpl {
    static PacketHandlerBuilder create() {
        return new PacketHandlerImpl();
    }


    <T extends Packet> PacketHandlerBuilder register(final int id, final PacketId<T> packetId, final Direction direction, final Function<SimpleByteBuf, T> decoder);
    PacketHandler build();
}
