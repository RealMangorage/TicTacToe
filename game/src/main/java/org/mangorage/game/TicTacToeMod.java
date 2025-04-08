package org.mangorage.game;

import org.mangorage.game.api.Mod;
import org.mangorage.game.api.TicTacToeAPI;
import org.mangorage.game.players.BasicAiPlayer;
import org.mangorage.game.players.HumanPlayer;

@Mod
public class TicTacToeMod {
    public TicTacToeMod() {
        // Default
        TicTacToeAPI.registerPlayerType("player", HumanPlayer::new);
        TicTacToeAPI.registerPlayerType("computer", BasicAiPlayer::new);
    }
}
