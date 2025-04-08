package org.mangorage.game.api;

public enum Result {
    SUCCESS, // It worked!
    FAILED, // Not your turn
    TRY_AGAIN, // Picked the an already taken position?
    GAME_OVER // Game is done!
}
