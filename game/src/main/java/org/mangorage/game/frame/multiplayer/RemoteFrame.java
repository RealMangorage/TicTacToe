package org.mangorage.game.frame.multiplayer;

import org.mangorage.game.Game;
import org.mangorage.game.network.Network;
import org.mangorage.game.network.packets.serverbound.C2SSelectGridPacket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Used for when we are connecting to a server
 *
 * Since we dont actually control the game
 * logic on the client anyways,
 * so all we need to do is have a general 3x3 grid
 * and all the scores/winner
 */
public class RemoteFrame extends JFrame implements KeyListener {
    private final JTextArea scoreLabel;
    private final JLabel statusLabel;
    private final JPanel gridPanel;  // Panel to hold the Tic-Tac-Toe grid

    public RemoteFrame() {
        setTitle("Tic-Tac-Toe Board");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setFocusable(true);
        addKeyListener(this);

        // Panel to hold the Tic-Tac-Toe grid
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(3, 3));

        // Adding labels as cells in the grid with mouse listeners
        for (int i = 0; i < 9; i++) {
            int finalI = i;  // Capture i for use in lambda
            JLabel label = new JLabel("", SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.PLAIN, 60));
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.setOpaque(true);  // Make the label fill the background
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onCellClicked(finalI);
                }
            });
            gridPanel.add(label);
        }

        // Status panel to show current player
        JPanel statusPanel = new JPanel();

        // Scores
        scoreLabel = new JTextArea(
                """
                No Scores
                """
        );
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        scoreLabel.setEditable(false);

        // Status
        statusLabel = new JLabel("(O) Player", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        // Add them!
        statusPanel.add(scoreLabel);
        statusPanel.add(statusLabel);

        // Add status panel at the top and grid panel below it
        setLayout(new BorderLayout());
        add(scoreLabel, BorderLayout.SOUTH);
        add(statusPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
    }

    public void update(final int[] board, final String scoresMessage, final String statusMessage, final String playerA, final String playerB) {
        if (!isVisible()) {
            Game.getJoinServerFrame().setVisible(false);
            setVisible(true);
        }

        // Update the grid with the current board state
        for (int i = 0; i < 9; i++) {
            JLabel label = (JLabel) gridPanel.getComponent(i);
            if (board[i] == 1) {
                label.setForeground(Color.RED);
                label.setText("X");
            } else if (board[i] == 2) {
                label.setForeground(Color.BLUE);
                label.setText("O");
            } else {
                label.setText("");
            }
        }

        scoreLabel.setText(scoresMessage);
        statusLabel.setText(statusMessage);
    }

    public void onCellClicked(final int position) {
        Network.getPlayerConnection().send(new C2SSelectGridPacket(position));
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
