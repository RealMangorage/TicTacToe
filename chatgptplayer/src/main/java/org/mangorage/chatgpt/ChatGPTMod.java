package org.mangorage.chatgpt;

import org.mangorage.game.api.Mod;
import org.mangorage.game.api.TicTacToeAPI;

import java.nio.file.Files;
import java.nio.file.Path;

@Mod
public final class ChatGPTMod {
    public static final Path CONFIG = Path.of("cfg").resolve("api_key_openai.txt");
    public ChatGPTMod() {
        if (Files.exists(CONFIG)) {
            TicTacToeAPI.registerPlayerType("chat_gpt", ChatGPTPlayer::new);
        }
    }
}
