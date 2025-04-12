package org.mangorage.game.network.packets.serverbound;

import org.mangorage.buffer.api.SimpleByteBuf;
import org.mangorage.game.network.Connection;
import org.mangorage.game.network.packets.Packet;
import org.mangorage.game.network.packets.PacketId;

public final class C2SLeavePacket implements Packet {
    public static final PacketId<C2SLeavePacket> ID = new PacketId<>();
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
