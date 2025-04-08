package org.mangorage.game.api;

import org.mangorage.game.core.PlayerType;
import org.mangorage.game.players.Player;

import java.util.function.Function;

public final class TicTacToeAPI {
    public static void registerPlayerType(String id, Function<String, Player> ctor) {
        PlayerType.register(id, ctor);
    }
}
