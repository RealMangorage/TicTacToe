package org.mangorage.network.api;

import org.mangorage.network.internal.PacketIdImpl;

public sealed interface PacketId<T extends Packet> permits PacketIdImpl {
    static <T extends Packet> PacketId<T> create() {
        return new PacketIdImpl<>();
    }
}
