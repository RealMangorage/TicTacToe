package org.mangorage.game.network;

import org.mangorage.game.network.packets.clientbound.S2CCommitTurnPacket;
import org.mangorage.game.network.packets.clientbound.S2CGridUpdatePacket;
import org.mangorage.game.network.packets.serverbound.C2SJoinPacket;
import org.mangorage.game.network.packets.serverbound.C2SLeavePacket;
import org.mangorage.game.network.packets.serverbound.C2SSelectGridPacket;
import org.mangorage.network.api.Connection;
import org.mangorage.network.api.Direction;
import org.mangorage.network.api.PacketHandler;
import org.mangorage.network.api.PacketHandlerBuilder;

public final class Network {
    public static final int port = 25564;
    private static int ID = 0;
    public static final PacketHandler INSTANCE = PacketHandlerBuilder.create()
            .register(ID++, S2CGridUpdatePacket.ID, Direction.S2C, S2CGridUpdatePacket::decode)
            .register(ID++, S2CCommitTurnPacket.ID, Direction.S2C, S2CCommitTurnPacket::decode)

            .register(ID++, C2SJoinPacket.ID, Direction.C2S, C2SJoinPacket::decode)
            .register(ID++, C2SLeavePacket.ID, Direction.C2S, C2SLeavePacket::decode)
            .register(ID++, C2SSelectGridPacket.ID, Direction.C2S, C2SSelectGridPacket::decode)
            .build();

    private static Connection playerConnection;


    public static void init() {}


    static void setPlayerConnection(final Connection connection) {
        Network.playerConnection = connection;
    }

    public static Connection getPlayerConnection() {
        return playerConnection;
    }
}
