package org.mangorage.game.core;

import org.mangorage.game.players.Player;
import java.util.List;

public final class PlayerSet {

    private final List<Player> players;
    private final Player playerA;
    private final Player playerB;

    private Player currentPlayer;

    public PlayerSet(Player playerA, Player playerB) {
        this.players = List.of(playerA, playerB);
        this.playerA = playerA;
        this.playerB = playerB;

        currentPlayer = playerA;
    }

    public void nextPlayer() {
         currentPlayer = currentPlayer == playerA ? playerB : playerA;
    }

    public Player getActivePlayer() {
        return currentPlayer;
    }

    public Player getPlayerById(int id) {
        if (id == 1) return playerA;
        if (id == 2) return playerB;
        return null;
    }

    public void updateAll(BoardImpl board) {
        playerA.update(board);
        playerB.update(board);
    }

    public String getActiveSymbol() {
        if (currentPlayer == null) return "";
        return currentPlayer == playerA ? "X" : "O";
    }

    public String getPlayerSymbol(Player player) {
        if (player == playerA) return "X";
        if (player == playerB) return "O";
        return "";
    }

    public String getPlayerSymbolById(int id) {
        if (id == 1) return "X";
        if (id == 2) return "O";
        return "";
    }

    public int getPlayerId(Player player) {
        if (player == playerA) return 1;
        if (player == playerB) return 2;
        return 0;
    }

    public List<Player> getAll() {
        return players;
    }

    public PlayerSet swap() {
        return new PlayerSet(playerB, playerA);
    }
}
