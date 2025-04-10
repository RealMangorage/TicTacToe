package org.mangorage.game.network.packets.serverbound;

import org.mangorage.game.network.core.SmartByteBuf;
import org.mangorage.game.network.packets.Packet;
import org.mangorage.game.network.packets.PacketId;

public final class C2SSelectGridPacket implements Packet {
    public static final PacketId<C2SSelectGridPacket> ID = new PacketId<>();

    public static C2SSelectGridPacket decode(SmartByteBuf buf) {
        return new C2SSelectGridPacket(buf.readInt());
    }

    private final int position;

    public C2SSelectGridPacket(final int position) {
        this.position = position;
    }

    @Override
    public void encode(SmartByteBuf buffer) {
        buffer.write(position);
    }

    @Override
    public void handle() {

    }

    @Override
    public PacketId<C2SSelectGridPacket> getId() {
        return ID;
    }
}
