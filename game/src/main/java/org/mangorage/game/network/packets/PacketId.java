package org.mangorage.game.network.packets;

import java.util.UUID;

public record PacketId<T extends Packet>(UUID uuid) {
    public PacketId() {
        this(UUID.randomUUID());
    }
}
