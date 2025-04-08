package org.mangorage.game.players;

import org.mangorage.game.api.Board;
import java.util.ArrayList;
import java.util.List;

public final class BasicAiPlayer implements Player {

    private final String name;
    public BasicAiPlayer(String name) {
        this.name = name;
    }

    @Override
    public void commitTurn(Board board) {
        // Get the list of empty positions on the board
        List<Integer> emptyPositions = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (board.getPlayer(i) == 0) {  // 0 means the spot is empty
                emptyPositions.add(i);
            }
        }

        // If the board is empty (first move), choose a random starting location
        if (emptyPositions.size() == 9) {
            int randomPosition = emptyPositions.get((int) (Math.random() * emptyPositions.size()));
            board.setPosition(this, randomPosition);
            return; // After making the move, exit the method
        }

        // Identify the current player and the opponent
        Player activePlayer = this;
        Player opponent = (activePlayer == board.getPlayerById(1)) ? board.getPlayerById(2) : board.getPlayerById(1);

        // Try to win, block the opponent, or take a strategic position
        int bestMove = -1;

        // Check if the AI (active player) can win on the next move
        bestMove = findBestMove(board, activePlayer);

        // If no winning move, check if opponent can win and block them
        if (bestMove == -1) {
            bestMove = findBestMove(board, opponent);
        }

        // If no winning or blocking move, pick a strategic position (center or corners)
        if (bestMove == -1) {
            bestMove = pickStrategicMove(board);
        }

        // Make the move
        if (bestMove != -1) {
            board.setPosition(activePlayer, bestMove);
        }
    }

    private int findBestMove(Board board, Player player) {
        for (int i = 0; i < 9; i++) {
            if (board.getPlayer(i) == 0) {
                board.setPosition(player, i);
                if (!board.isGameOver()) {
                    board.setPosition(this, i);  // Reset the spot
                    return i;  // This is a winning move
                }
                board.setPosition(this, i);  // Reset the spot
            }
        }
        return -1;
    }

    private int pickStrategicMove(Board board) {
        // Prioritize center and corners
        int[] strategicPositions = {4, 0, 2, 6, 8};  // Center, then corners
        for (int position : strategicPositions) {
            if (board.getPlayer(position) == 0) {
                return position;
            }
        }
        return -1;  // Fallback, should never happen
    }

    @Override
    public String getName() {
        return name;
    }
}
