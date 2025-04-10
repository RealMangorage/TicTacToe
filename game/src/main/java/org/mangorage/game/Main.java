package org.mangorage.game;

import org.mangorage.game.core.PlayerType;
import org.mangorage.game.network.core.PacketHandler;

import javax.swing.*;

public final class Main {
    public static void main(String[] args) {
        PlayerType.init();
        PacketHandler.init();

        SwingUtilities.invokeLater(() -> {
            Game.init();
            Game.getMainMenuFrame().setVisible(true);
        });
    }
}
