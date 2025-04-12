package org.mangorage.game.network.packets.clientbound;


import org.mangorage.buffer.api.SimpleByteBuf;
import org.mangorage.game.network.Connection;
import org.mangorage.game.network.packets.Packet;
import org.mangorage.game.network.packets.PacketId;


public final class S2CGridUpdatePacket implements Packet {
    public static final PacketId<S2CGridUpdatePacket> ID = new PacketId<>();


    public static S2CGridUpdatePacket decode(SimpleByteBuf buf) {
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
    public void encode(SimpleByteBuf buffer) {
        buffer.writeIntArray(grid);
        buffer.writeString(scoresMessage);
        buffer.writeString(statusMessage);
    }

    @Override
    public void handle(Connection connection) {

    }

    @Override
    public PacketId<S2CGridUpdatePacket> getId() {
        return ID;
    }
}
