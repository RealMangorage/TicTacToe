package org.mangorage.game.frame;

/**
 * Gives 4 main options
 *
 * Single Player
 *
 * Host
 *
 * Join
 *
 * Exit Game
 */
import org.mangorage.game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenuFrame extends JFrame {

    public MainMenuFrame() {
        super("TicTacToe By MangoRage");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        // Top label
        JLabel titleLabel = new JLabel("TicTacToe", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel footerLabel = new JLabel("Made by MangoRage!", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create a panel to hold both the title and the spacer
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));;
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(Box.createVerticalStrut(10)); // Add space between the title and footer
        topPanel.add(footerLabel, BorderLayout.NORTH);

        // Add a JPanel for extra space between the title and buttons
        JPanel topSpacer = new JPanel();
        topSpacer.setPreferredSize(new Dimension(400, 50)); // Adjust height to your liking
        topPanel.add(topSpacer, BorderLayout.CENTER);

        // Add the topPanel to the NORTH section of the BorderLayout
        add(topPanel, BorderLayout.NORTH);

        // Center buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));

        buttonPanel.add(createButton("Single Player", e -> handleSinglePlayer()));
        buttonPanel.add(createButton("Host (W.I.P)", e -> handleHost()));
        buttonPanel.add(createButton("Join (W.I.P)", e -> handleJoin()));
        buttonPanel.add(createButton("Exit Game", e -> System.exit(0)));

        add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);

        // Big font logic
        Font baseFont = button.getFont();
        button.setFont(baseFont.deriveFont(Font.BOLD, 48f)); // Start big

        // Resize listener to auto-adjust text size based on button size
        button.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                resizeFontToFit(button);
            }
        });

        return button;
    }

    private void resizeFontToFit(JButton button) {
        FontMetrics metrics;
        int width = button.getWidth();
        int height = button.getHeight();
        int size = 10;

        do {
            Font font = button.getFont().deriveFont((float) size);
            button.setFont(font);
            metrics = button.getFontMetrics(font);
            size++;
        } while (
                metrics.stringWidth(button.getText()) < width * 0.9 &&
                        metrics.getHeight() < height * 0.9 &&
                        size < 200 // Just in case you want to limit how insane it gets
        );
    }

    // Placeholder methods to replace your future masterpieces
    private void handleSinglePlayer() {
        System.out.println("Single Player selected");
        SwingUtilities.invokeLater(() -> {
            setVisible(false);
            Game.getMenuFrame().setVisible(true);
        });
    }

    private void handleHost() {
        System.out.println("Host selected");
    }

    private void handleJoin() {
        System.out.println("Join selected");
    }

}
