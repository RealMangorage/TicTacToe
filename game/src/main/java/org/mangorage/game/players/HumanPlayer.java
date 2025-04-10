package org.mangorage.game.players;

import org.mangorage.game.api.Board;
import org.mangorage.game.frame.singleplayer.BoardFrame;

public final class HumanPlayer implements Player {
    private final String name;
    public HumanPlayer(String name) {
        this.name = name;
    }

    /**
     * NO-OP as the player input will be handled in {@link BoardFrame}
     * Because the player can see who's turn it is already, as its happening.
     *
     * This is more useful if we want an AI or "remote" player to be able to play.
     */
    @Override
    public void commitTurn(Board board) {}

    @Override
    public String getName() {
        return name;
    }
}
