package org.mangorage.game.network.packets.serverbound;

import org.mangorage.buffer.api.SimpleByteBuf;
import org.mangorage.network.api.Connection;
import org.mangorage.network.api.Packet;
import org.mangorage.network.api.PacketId;

public final class C2SLeavePacket implements Packet {
    public static final PacketId<C2SLeavePacket> ID = PacketId.create();
    public static final C2SLeavePacket INSTANCE = new C2SLeavePacket();

    public static C2SLeavePacket decode(SimpleByteBuf buf) {
        return INSTANCE;
    }

    C2SLeavePacket() {}

    @Override
    public void encode(SimpleByteBuf buffer) {

    }

    @Override
    public void handle(Connection connection) {

    }

    @Override
    public PacketId<?> getId() {
        return ID;
    }
}
