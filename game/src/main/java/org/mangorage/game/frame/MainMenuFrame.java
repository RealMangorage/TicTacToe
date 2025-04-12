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
import org.mangorage.game.Main;
import org.mangorage.game.network.Network;
import org.mangorage.game.network.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URI;

public class MainMenuFrame extends JFrame {

    public MainMenuFrame() {
        super("TicTacToe By MangoRage");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        // ===== Top Panel =====
        JLabel titleLabel = new JLabel("TicTacToe", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel footerLabel = new JLabel("Made by MangoRage!", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(footerLabel);
        topPanel.add(Box.createVerticalStrut(50)); // Spacer between footer and buttons

        add(topPanel, BorderLayout.NORTH);

        // ===== Center Button Panel =====
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.add(createButton("Single Player", e -> handleSinglePlayer()));
        buttonPanel.add(createButton("Host", e -> handleHost()));
        buttonPanel.add(createButton("Join", e -> handleJoin()));
        buttonPanel.add(createButton("Exit Game", e -> System.exit(0)));

        add(buttonPanel, BorderLayout.CENTER);

        // ===== Bottom Icon Panel =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        bottomPanel.add(createIconButton("assets/discord.png", e -> openLink("https://discord.mangorage.org/")));
        bottomPanel.add(createIconButton("assets/github.png", e -> openLink("https://github.com/RealMangorage/TicTacToe")));

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private ImageIcon createIcon(String path) {
        return new ImageIcon(
                Main.getScanner()
                        .findResource(resource -> resource.contains(path))
                        .findFirst()
                        .orElseThrow()
                        .get()
        );
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    private JButton createIconButton(String path, ActionListener event) {
        ImageIcon icon = resizeIcon(createIcon(path), 32, 32);
        JButton button = new JButton(icon);

        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(32, 32));
        button.addActionListener(event);

        return button;
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
        setVisible(false);
        Game.getMenuFrame().setVisible(true);
    }

    private void handleHost() {
        System.out.println("Host selected");
        Game.getHostServerFrame().showMenu();
    }

    private void handleJoin() {
        System.out.println("Join selected");
        Game.getJoinServerFrame().showMenu();
    }

    private void openLink(String url) {
        try {
            URI uri = new URI(url);
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(uri);
            } else {
                throw new UnsupportedOperationException("Desktop doesn't support browsing, your setup is from the stone age.");
            }
        } catch (Exception ignored) {}
    }
}
