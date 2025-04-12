package org.mangorage.game.network.packets.clientbound;

import org.mangorage.buffer.api.SimpleByteBuf;
import org.mangorage.network.api.Connection;
import org.mangorage.network.api.Packet;
import org.mangorage.network.api.PacketId;

public class S2CCommitTurnPacket implements Packet {
    public static final PacketId<S2CCommitTurnPacket> ID = PacketId.create();
    public static final S2CCommitTurnPacket INSTANCE = new S2CCommitTurnPacket();

    public static S2CCommitTurnPacket decode(SimpleByteBuf buf) {
        return INSTANCE;
    }

    S2CCommitTurnPacket() {}

    @Override
    public void encode(SimpleByteBuf buffer) {

    }

    @Override
    public void handle(Connection connection) {

    }

    @Override
    public PacketId<S2CCommitTurnPacket> getId() {
        return ID;
    }

}
