package org.mangorage.game;

import org.mangorage.game.core.BoardImpl;
import org.mangorage.game.core.PlayerSet;
import org.mangorage.game.frame.singleplayer.BoardFrame;
import org.mangorage.game.frame.singleplayer.MenuFrame;
import org.mangorage.game.players.HumanPlayer;


public class Game {
    private static final BoardFrame gameFrame = new BoardFrame();
    private static final MenuFrame menuFrame = new MenuFrame(() -> gameFrame.setVisible(true));
    private static final BoardImpl board = new BoardImpl();
    private static final PlayerSet playerSet = new PlayerSet(new HumanPlayer("PlayerA"), new HumanPlayer("PlayerB"));

    public static BoardImpl getBoard() {
        return board;
    }

    public static PlayerSet getPlayerSet() {
        return playerSet;
    }

    public static BoardFrame getGameFrame() {
        return gameFrame;
    }

    public static MenuFrame getMenuFrame() {
        return menuFrame;
    }

    public static void init() {}
}
