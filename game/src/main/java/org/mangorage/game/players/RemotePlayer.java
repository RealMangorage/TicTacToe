package org.mangorage.game.players;

import org.mangorage.game.api.Board;

public final class RemotePlayer implements Player {

    private final String username;
    public RemotePlayer(final String username) {
        this.username = username;
    }

    // No reason to do this, other then for telling the client to play a sound
    // In Which case we could make a S2CPlaySoundPacket
    @Override
    public void commitTurn(Board board) {
    }

    @Override
    public String getName() {
        return username;
    }
}
