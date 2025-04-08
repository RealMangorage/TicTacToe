package org.mangorage.game.api;

import org.mangorage.game.players.Player;

public sealed interface Board permits org.mangorage.game.core.BoardImpl {
    Result setPosition(Player player, int position);
    int getPlayer(int position);
    Player getPlayerById(int playerId);
    String[] getBoard();
    String getActivePlayerSymbol();

    boolean isGameOver();
}
