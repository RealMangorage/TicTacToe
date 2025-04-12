package org.mangorage.network.internal;

import org.mangorage.network.api.Packet;
import org.mangorage.network.api.PacketId;

import java.util.UUID;

public record PacketIdImpl<T extends Packet>(UUID uuid) implements PacketId<T> {
    public PacketIdImpl() {
        this(UUID.randomUUID());
    }
}
