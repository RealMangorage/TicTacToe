package org.mangorage.chatgpt;

import org.mangorage.game.api.Mod;
import org.mangorage.game.api.TicTacToeAPI;

@Mod
public final class ChatGPTMod {
    public ChatGPTMod() {
        TicTacToeAPI.registerPlayerType("chat_gpt", ChatGPTPlayer::new);
    }
}
