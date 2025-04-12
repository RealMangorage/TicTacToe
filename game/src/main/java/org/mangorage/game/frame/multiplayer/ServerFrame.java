package org.mangorage.game.frame.multiplayer;

import org.mangorage.game.Game;
import org.mangorage.game.network.Client;
import org.mangorage.game.network.Network;
import org.mangorage.game.network.Server;
import org.mangorage.game.players.HumanPlayer;
import org.mangorage.game.players.RemotePlayer;

import javax.swing.*;
import java.awt.*;

public class ServerFrame extends JFrame {

    private final JLabel waitingLabel;
    private final JButton mainButton;

    private final JTextField userNameField;
    private final JTextField mainField;
    private final JPasswordField passwordField;

    private final boolean joining;

    public ServerFrame(boolean joining) {
        this.joining = joining;

        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setLocationRelativeTo(null); // center the window

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Tic Tac Toe");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel authorLabel = new JLabel("Made by Mangorage");
        authorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username
        // Username input
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userNameField = new JTextField(10);
        userNameField.setMaximumSize(new Dimension(200, 25));

        userPanel.add(userLabel);
        userPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        userPanel.add(userNameField);


        // Port input
        JPanel portPanel = new JPanel();
        portPanel.setLayout(new BoxLayout(portPanel, BoxLayout.Y_AXIS));
        portPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel portLabel = new JLabel(joining ? "IP:PORT" : "Port:");
        portLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainField = new JTextField(10);
        mainField.setMaximumSize(new Dimension(200, 25));
        portPanel.add(portLabel);
        portPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        portPanel.add(mainField);

        // Password input
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField = new JPasswordField(10);
        passwordField.setMaximumSize(new Dimension(200, 25));
        passwordPanel.add(passwordLabel);
        passwordPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        passwordPanel.add(passwordField);

        waitingLabel = new JLabel("Waiting for Player to join");
        waitingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainButton = new JButton("Start game");
        mainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainButton.addActionListener(e -> pressButton());

        mainPanel.add(titleLabel);
        mainPanel.add(authorLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(userPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(portPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(passwordPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(waitingLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(mainButton);

        add(mainPanel);
    }

    // Press the button programmatically
    public void pressButton() {
        if (joining) {
            waitingLabel.setText("Joining Game Very soon!");
            String[] arg = mainField.getText().split(":");
            Client.initNewClient(
                    arg[0],
                    Integer.parseInt(arg[1]),
                    userNameField.getText(),
                    passwordField.getPassword()
            );
        } else {
            // TODO: Check inputs
            waitingLabel.setText("Waiting for Player to join...");
            waitingLabel.setVisible(true);

            mainField.setEditable(false);
            passwordField.setEditable(false);
            mainButton.setVisible(false);

            Server.initNewServer(
                    Integer.parseInt(mainField.getText())
            );
        }
    }

    public void showMenu() {
        SwingUtilities.invokeLater(() -> {
            Game.getMainMenuFrame().setVisible(false);
            mainField.setEditable(true);
            passwordField.setEditable(true);
            mainField.setText("");
            passwordField.setText("");
            mainButton.setVisible(true);
            waitingLabel.setVisible(false);
            setVisible(true);
        });
    }

    // Give access to the button in case some troll wants to attach a listener
    public JButton getMainButton() {
        return mainButton;
    }

    public void playerJoined(RemotePlayer player) {
        waitingLabel.setText("%s joined the game! Starting soon...".formatted(player.getName()));
        try {
            Network.setPlayer(player);

            Thread.sleep(1000);
            Game.getGameFrame().showMenu();
            Game.getPlayerSet().resetPlayers(
                    new HumanPlayer(userNameField.getText()),
                    player
            );
            Game.getBoard().startNewGame();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}