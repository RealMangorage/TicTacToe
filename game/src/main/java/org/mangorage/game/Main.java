package org.mangorage.game;

import org.mangorage.game.core.BoardImpl;
import org.mangorage.game.core.PlayerSet;
import org.mangorage.game.core.PlayerType;
import org.mangorage.game.core.Scoreboard;
import org.mangorage.game.frame.BoardFrame;
import org.mangorage.game.frame.MenuFrame;

import javax.swing.*;

public final class Main {
    private static MenuFrame menuFrame;
    private static BoardFrame gameFrame;
    private static BoardImpl activeBoard = null;

    public static BoardImpl getActiveBoard() {
        return activeBoard;
    }

    public static void setActiveBoard(PlayerSet players) {
        players.getAll().forEach(Scoreboard.INSTANCE::prepareScore);

        activeBoard = new BoardImpl(gameFrame, players);
        activeBoard.update(false);
    }

    public static void main(String[] args) {
        PlayerType.init();

        SwingUtilities.invokeLater(() -> {
            gameFrame = new BoardFrame();
            menuFrame = new MenuFrame(() -> gameFrame.setVisible(true));

            menuFrame.setVisible(true);
        });
    }
}
