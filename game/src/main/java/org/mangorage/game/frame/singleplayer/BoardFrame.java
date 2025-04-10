package org.mangorage.game.frame.singleplayer;

import org.mangorage.game.Game;
import org.mangorage.game.core.BoardImpl;
import org.mangorage.game.core.Scoreboard;
import org.mangorage.game.players.HumanPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class BoardFrame extends JFrame implements KeyListener {
    private final JTextArea scoreLabel;
    private final JLabel statusLabel;
    private final JPanel gridPanel;  // Panel to hold the Tic-Tac-Toe grid

    public BoardFrame() {
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

    // Update the board display and player status
    public void updatePlayer(String active, String[] board) {
        // Update the status label with the active player's symbol
        statusLabel.setText(active);

        // Update the grid with the current board state
        for (int i = 0; i < 9; i++) {
            JLabel label = (JLabel) gridPanel.getComponent(i);
            label.setText(board[i]);
            // Change the label color based on X or O
            if (board[i].equals("X")) {
                label.setForeground(Color.RED);
            } else if (board[i].equals("O")) {
                label.setForeground(Color.BLUE);
            }
        }

        // Revalidate and repaint to update the JFrame with the new board
        revalidate();
        repaint();
    }

    // Handle when a player clicks on a cell
    private void onCellClicked(int index) {
        var gameBoard = Game.getBoard();
        if (gameBoard.getActivePlayer() instanceof HumanPlayer humanPlayer) {
            gameBoard.setPosition(humanPlayer, index);
        }
    }

    public void update(BoardImpl board) {
        var activePlayer = board.getActivePlayer();
        var symbol = board.getActivePlayerSymbol();

        if (!board.isGameOver()) {
            updatePlayer(
                    "(%s) %s".formatted(symbol, activePlayer.getName()),  // Replace this with logic to get the active player
                    board.getBoard()
            );
        } else {
            updatePlayer(
                    board.getFinalMessage(),
                    board.getBoard()
            );
        }

        updateScores();
    }

    public void updateScores() {
        Scoreboard scoreboard = Scoreboard.INSTANCE;

        StringBuilder builder = new StringBuilder();
        builder.append("Total Games: " + scoreboard.getTotalGames());
        builder.append("\nScores:");
        scoreboard.getScore().forEach((player, integer) -> {
            builder.append("\n%s -> %s".formatted(player.getName(), integer));
        });

        scoreLabel.setText(builder.toString());
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F5) {
            setVisible(false);
            Game.getBoard().endGame();
            Game.getMainMenuFrame().setVisible(true);
        }
    }
}
