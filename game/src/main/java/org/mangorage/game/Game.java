package org.mangorage.game;

import org.mangorage.game.core.BoardImpl;
import org.mangorage.game.core.PlayerSet;
import org.mangorage.game.frame.MainMenuFrame;
import org.mangorage.game.frame.multiplayer.HostServerFrame;
import org.mangorage.game.frame.multiplayer.RemoteFrame;
import org.mangorage.game.frame.multiplayer.ServerJoinFrame;
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
    private static final HostServerFrame hostServerFrame = new HostServerFrame();
    private static final ServerJoinFrame serverJoinFrame = new ServerJoinFrame();

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
    public static HostServerFrame getHostServerFrame() {
        return hostServerFrame;
    }

    public ServerJoinFrame getServerJoinFrame() {
        return serverJoinFrame;
    }

    // Used for when in a server
    public RemoteFrame getRemoteFrame() {
        return remoteFrame;
    }

    // Main Menu
    public static MainMenuFrame getMainMenuFrame() {
        return mainMenuFrame;
    }

    public static void init() {}
}
