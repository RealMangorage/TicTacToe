package org.mangorage.example;

import org.mangorage.game.api.Mod;
import org.mangorage.game.api.TicTacToeAPI;
import org.mangorage.game.players.HumanPlayer;

@Mod
public final class MyMod {
    public MyMod() {
        TicTacToeAPI.registerPlayerType("someType", HumanPlayer::new);
    }
}
