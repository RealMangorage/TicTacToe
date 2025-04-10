package org.mangorage.game.network.packets.clientbound;


import org.mangorage.game.network.core.SmartByteBuf;
import org.mangorage.game.network.packets.Packet;
import org.mangorage.game.network.packets.PacketId;


public final class S2CGridUpdatePacket implements Packet {
    public static final PacketId<S2CGridUpdatePacket> ID = new PacketId<>();


    public static S2CGridUpdatePacket decode(SmartByteBuf buf) {
        return new S2CGridUpdatePacket(buf.readIntArray(), buf.readString(), buf.readString());
    }

    private final int[] grid;
    private final String scoresMessage;
    private final String statusMessage;

    public S2CGridUpdatePacket(final int[] grid, final String scoresMessage, final String statusMessage) {
        this.grid = grid;
        this.scoresMessage =  scoresMessage;
        this.statusMessage = statusMessage;
    }

    @Override
    public void encode(SmartByteBuf buffer) {
        buffer.writeIntArray(grid);
        buffer.write(scoresMessage);
        buffer.write(statusMessage);
    }

    @Override
    public void handle() {

    }

    @Override
    public PacketId<S2CGridUpdatePacket> getId() {
        return ID;
    }
}
