package org.mangorage.game;

import org.mangorage.game.core.BoardImpl;
import org.mangorage.game.core.PlayerSet;
import org.mangorage.game.frame.MainMenuFrame;
import org.mangorage.game.frame.multiplayer.ServerFrame;
import org.mangorage.game.frame.multiplayer.RemoteFrame;
import org.mangorage.game.frame.singleplayer.BoardFrame;
import org.mangorage.game.frame.singleplayer.MenuFrame;
import org.mangorage.game.players.HumanPlayer;


public class Game {
    private static final MainMenuFrame mainMenuFrame = new MainMenuFrame();

    // Single Player Menus
    private static final BoardFrame gameFrame = new BoardFrame();
    private static final MenuFrame menuFrame = new MenuFrame();

    // Used for being in server
    private static final RemoteFrame remoteFrame = new RemoteFrame();

    // Sub Menus
    private static final ServerFrame hostServerFrame = new ServerFrame(false);
    private static final ServerFrame joinServerFrame = new ServerFrame(true);

    // Used for Singleplayer/Host
    private static final BoardImpl board = new BoardImpl();
    private static final PlayerSet playerSet = new PlayerSet(new HumanPlayer("PlayerA"), new HumanPlayer("PlayerB"));



    public static BoardImpl getBoard() {
        return board;
    }

    public static PlayerSet getPlayerSet() {
        return playerSet;
    }

    // Single Player Menus

    public static BoardFrame getGameFrame() {
        return gameFrame;
    }

    public static MenuFrame getMenuFrame() {
        return menuFrame;
    }

    // Multiplayer Menus
    public static ServerFrame getHostServerFrame() {
        return hostServerFrame;
    }

    public static ServerFrame getJoinServerFrame() {
        return joinServerFrame;
    }

    // Used for when in a server
    public static RemoteFrame getRemoteFrame() {
        return remoteFrame;
    }

    // Main Menu
    public static MainMenuFrame getMainMenuFrame() {
        return mainMenuFrame;
    }

    public static void init() {}
}
