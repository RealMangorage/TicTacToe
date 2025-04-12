package org.mangorage.game.network.packets.serverbound;

import org.mangorage.buffer.api.SimpleByteBuf;
import org.mangorage.game.network.Connection;
import org.mangorage.game.network.packets.Packet;
import org.mangorage.game.network.packets.PacketId;

public final class C2SJoinPacket implements Packet {
    public static final PacketId<C2SJoinPacket> ID = new PacketId<>();
    public static final C2SJoinPacket INSTANCE = new C2SJoinPacket();

    public static C2SJoinPacket decode(SimpleByteBuf buf) {
        return INSTANCE;
    }

    C2SJoinPacket() {}

    @Override
    public void encode(SimpleByteBuf buffer) {

    }

    @Override
    public void handle(Connection connection) {
        System.out.println("Player Joined!");
    }

    @Override
    public PacketId<?> getId() {
        return ID;
    }
}
