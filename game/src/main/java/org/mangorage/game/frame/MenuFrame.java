package org.mangorage.game.frame;

import org.mangorage.game.Game;
import org.mangorage.game.Main;
import org.mangorage.game.core.PlayerSet;
import org.mangorage.game.core.PlayerType;
import org.mangorage.game.core.Scoreboard;

import javax.swing.*;
import java.awt.*;

public class MenuFrame extends JFrame {

    private record Profile(PlayerType type, String name) {}

    private final Runnable showGameFrame;
    private final JComboBox<PlayerType> comboBox1;
    private final JComboBox<PlayerType> comboBox2;
    private final JTextField textField1;
    private final JTextField textField2;

    public MenuFrame(Runnable showGameFrame) {
        this.showGameFrame = showGameFrame;
        setTitle("Tic Tac Toe Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null); // Center on screen
        setResizable(false);

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Top instruction
        JLabel topLabel = new JLabel("Choose your player type and then the name for the player", SwingConstants.CENTER);
        topLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(topLabel, BorderLayout.NORTH);

        // Center layout for rows
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        // First row
        JPanel row1 = new JPanel(new BorderLayout());
        comboBox1 = new JComboBox<>(PlayerType.values());
        textField1 = new JTextField();
        row1.add(comboBox1, BorderLayout.NORTH);
        row1.add(textField1, BorderLayout.SOUTH);

        // Second row
        JPanel row2 = new JPanel(new BorderLayout());
        comboBox2 = new JComboBox<>(PlayerType.values());
        textField2 = new JTextField();
        row2.add(comboBox2, BorderLayout.NORTH);
        row2.add(textField2, BorderLayout.SOUTH);

        centerPanel.add(row1);
        centerPanel.add(row2);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> {
            String name1 = textField1.getText().trim();
            PlayerType type1 = (PlayerType) comboBox1.getSelectedItem();
            String name2 = textField2.getText().trim();
            PlayerType type2 = (PlayerType) comboBox2.getSelectedItem();
            startGame(
                    new Profile(type1, name1),
                    new Profile(type2, name2)
            );
        });
        buttonPanel.add(startButton);

        // Add everything
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    // Your majestic game starter
    private void startGame(Profile playerA, Profile playerB) {
        System.out.println("Starting game with:");
        System.out.println("Player 1: " + playerA.name() + " (" + playerA.type() + ")");
        System.out.println("Player 2: " + playerB.name() + " (" + playerB.type() + ")");

        Scoreboard.INSTANCE.clear();

        Game.getPlayerSet()
                .resetPlayers(
                        playerA.type().create(playerA.name()),
                        playerB.type().create(playerB.name())
                );

        Game.getBoard().startNewGame();

        SwingUtilities.invokeLater(() -> {
            setVisible(false);
            showGameFrame.run();
        });
    }
}