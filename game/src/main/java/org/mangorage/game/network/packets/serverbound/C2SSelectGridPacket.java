package org.mangorage.game.network.packets.serverbound;

import org.mangorage.buffer.api.SimpleByteBuf;
import org.mangorage.game.Game;
import org.mangorage.game.network.Network;
import org.mangorage.network.api.Connection;
import org.mangorage.network.api.Packet;
import org.mangorage.network.api.PacketId;

public final class C2SSelectGridPacket implements Packet {
    public static final PacketId<C2SSelectGridPacket> ID = PacketId.create();

    public static C2SSelectGridPacket decode(SimpleByteBuf buf) {
        return new C2SSelectGridPacket(buf.readInt());
    }

    private final int position;

    public C2SSelectGridPacket(final int position) {
        this.position = position;
    }

    @Override
    public void encode(SimpleByteBuf buffer) {
        buffer.writeInt(position);
    }

    public void handle(Connection connection) {
        Game.getBoard().setPosition(Network.getPlayer(), position);
    }

    @Override
    public PacketId<C2SSelectGridPacket> getId() {
        return ID;
    }
}
