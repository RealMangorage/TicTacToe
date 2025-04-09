package org.mangorage.game;

import org.mangorage.game.core.PlayerType;
import javax.swing.*;

public final class Main {
    public static void main(String[] args) {
        PlayerType.init();

        SwingUtilities.invokeLater(() -> {
            Game.init();
            Game.getMenuFrame().setVisible(true);
        });
    }
}
