package org.mangorage.game.network.packets.clientbound;

import org.mangorage.game.network.core.SmartByteBuf;
import org.mangorage.game.network.packets.Packet;
import org.mangorage.game.network.packets.PacketId;

public class S2CCommitTurnPacket implements Packet {
    public static final PacketId<S2CCommitTurnPacket> ID = new PacketId<>();
    public static final S2CCommitTurnPacket INSTANCE = new S2CCommitTurnPacket();

    public static S2CCommitTurnPacket decode(SmartByteBuf buf) {
        return INSTANCE;
    }

    S2CCommitTurnPacket() {}

    @Override
    public void encode(SmartByteBuf buffer) {

    }

    @Override
    public void handle() {

    }

    @Override
    public PacketId<S2CCommitTurnPacket> getId() {
        return ID;
    }

}
