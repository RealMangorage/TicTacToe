package org.mangorage.game.players;

import org.mangorage.game.api.Board;

/**
 Cant be mutable, as its intended to be a single instanced.
 */
public interface Player {
    void commitTurn(Board board);
    String getName();

    default void update(Board board) {};
}
