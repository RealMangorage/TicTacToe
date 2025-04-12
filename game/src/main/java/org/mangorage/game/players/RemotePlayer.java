package org.mangorage.game.players;

import org.mangorage.game.api.Board;
import org.mangorage.game.network.Network;
import org.mangorage.game.network.packets.clientbound.S2CCommitTurnPacket;

public final class RemotePlayer implements Player {

    private final String username;
    public RemotePlayer(final String username) {
        this.username = username;
    }

    @Override
    public void commitTurn(Board board) {
        var plr = Network.getPlayerConnection();
        if (plr != null) {
            plr.send(S2CCommitTurnPacket.INSTANCE);
        }
    }

    @Override
    public String getName() {
        return username;
    }
}
