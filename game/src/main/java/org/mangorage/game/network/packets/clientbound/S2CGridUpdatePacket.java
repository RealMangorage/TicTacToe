package org.mangorage.game.network.packets.clientbound;


import org.mangorage.buffer.api.SimpleByteBuf;
import org.mangorage.game.Game;
import org.mangorage.network.api.Connection;
import org.mangorage.network.api.Packet;
import org.mangorage.network.api.PacketId;


public final class S2CGridUpdatePacket implements Packet {
    public static final PacketId<S2CGridUpdatePacket> ID = PacketId.create();


    public static S2CGridUpdatePacket decode(SimpleByteBuf buf) {
        return new S2CGridUpdatePacket(buf.readIntArray(), buf.readString(), buf.readString(), buf.readString(), buf.readString());
    }

    private final int[] grid;
    private final String scoresMessage;
    private final String statusMessage;
    private final String playerA;
    private final String playerB;

    public S2CGridUpdatePacket(final int[] grid, final String scoresMessage, final String statusMessage, final String playerA, final String playerB ) {
        this.grid = grid;
        this.scoresMessage =  scoresMessage;
        this.statusMessage = statusMessage;
        this.playerA = playerA;
        this.playerB = playerB;
    }

    @Override
    public void encode(SimpleByteBuf buffer) {
        buffer.writeIntArray(grid);
        buffer.writeString(scoresMessage);
        buffer.writeString(statusMessage);
    }

    @Override
    public void handle(Connection connection) {
        Game.getRemoteFrame()
                .update(
                        grid,
                        scoresMessage,
                        statusMessage,
                        playerA,
                        playerB
                );
    }


    @Override
    public PacketId<S2CGridUpdatePacket> getId() {
        return ID;
    }
}
