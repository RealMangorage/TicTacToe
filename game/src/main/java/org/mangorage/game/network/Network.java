package org.mangorage.game.network;

import org.mangorage.game.network.packets.clientbound.S2CCommitTurnPacket;
import org.mangorage.game.network.packets.clientbound.S2CGridUpdatePacket;
import org.mangorage.game.network.packets.serverbound.C2SJoinPacket;
import org.mangorage.game.network.packets.serverbound.C2SLeavePacket;
import org.mangorage.game.network.packets.serverbound.C2SSelectGridPacket;
import org.mangorage.game.players.Player;
import org.mangorage.network.api.Connection;
import org.mangorage.network.api.Direction;
import org.mangorage.network.api.PacketHandler;
import org.mangorage.network.api.PacketHandlerBuilder;

public final class Network {
    private static int ID = 0;
    public static final PacketHandler INSTANCE = PacketHandlerBuilder.create()
            .register(ID++, S2CGridUpdatePacket.ID, Direction.SERVER, S2CGridUpdatePacket::decode)
            .register(ID++, S2CCommitTurnPacket.ID, Direction.SERVER, S2CCommitTurnPacket::decode)

            .register(ID++, C2SJoinPacket.ID, Direction.CLIENT, C2SJoinPacket::decode)
            .register(ID++, C2SLeavePacket.ID, Direction.CLIENT, C2SLeavePacket::decode)
            .register(ID++, C2SSelectGridPacket.ID, Direction.CLIENT, C2SSelectGridPacket::decode)
            .build();

    private static Connection playerConnection;
    private static Player player;

    public static void init() {}

    public static void setPlayer(Player player) {
        Network.player = player;
    }

    public static Player getPlayer() {
        return player;
    }

    public static void setPlayerConnection(final Connection connection) {
        Network.playerConnection = connection;
    }

    public static Connection getPlayerConnection() {
        return playerConnection;
    }
}
