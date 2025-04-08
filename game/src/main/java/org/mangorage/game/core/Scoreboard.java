package org.mangorage.game.core;

import org.mangorage.game.players.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Scoreboard {
    public static final Scoreboard INSTANCE = new Scoreboard();

    private final Map<Player, Integer> score = new HashMap<>();
    private final Map<Player, Integer> score_read_only = Collections.unmodifiableMap(score);
    private int totalGames = 0;

    private Scoreboard() {}

    public void prepareScore(Player player) {
        score.putIfAbsent(player, 0);
    }

    public void incrementGameCount() {
        totalGames++;
    }

    public void addScore(Player player) {
        if (score.containsKey(player)) {
            score.computeIfPresent(player, (p, old) -> old + 1);
        } else {
            prepareScore(player);
            addScore(player);
        }
    }

    public Map<Player, Integer> getScore() {
        return score_read_only;
    }

    public int getTotalGames() {
        return totalGames;
    }
}
