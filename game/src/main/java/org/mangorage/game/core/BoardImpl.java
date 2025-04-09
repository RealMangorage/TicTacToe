package org.mangorage.game.core;

import org.mangorage.game.Game;
import org.mangorage.game.api.Board;
import org.mangorage.game.api.Result;
import org.mangorage.game.players.Player;

import java.util.Arrays;

public final class BoardImpl implements Board {
    private final int[] board;

    private boolean gameOver = true;
    private String finalMessage = null;

    public BoardImpl() {
        this.board = new int[9];
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getFinalMessage() {
        return finalMessage;
    }

    public int getPlayer(int position) {
        return board[position];
    }

    public String[] getBoard() {
        String[] boardRepresentation = new String[9];

        // Iterate through the board array and populate boardRepresentation with the correct symbols
        for (int i = 0; i < 9; i++) {
            int playerId = board[i];  // This will either be 0 (empty), or a player ID

            if (playerId == 0) {
                boardRepresentation[i] = ""; // Empty space
            } else {
                // Lookup the symbol for the player using the player ID
                boardRepresentation[i] = Game.getPlayerSet().getPlayerSymbolById(playerId);
            }
        }

        return boardRepresentation;
    }

    public Player getPlayerById(int playerId) {
        return Game.getPlayerSet().getPlayerById(playerId);
    }

    public String getActivePlayerSymbol() {
        return Game.getPlayerSet().getActiveSymbol();
    }

    public Player getActivePlayer() {
        return Game.getPlayerSet().getActivePlayer();
    }

    public Result setPosition(Player player, int position) {
        if (isGameOver()) return Result.GAME_OVER;
        if (Game.getPlayerSet().getActivePlayer() == player) {
            if (board[position] != 0) return Result.TRY_AGAIN;
            board[position] = Game.getPlayerSet().getPlayerId(player);

            Game.getPlayerSet().nextPlayer();

            // Update everything
            update(true);

            // if we havent won, tell the new active player that his turn has started...
            if (!isGameOver()) // Continue the game...
                getActivePlayer().commitTurn(this);

            update(false);
            return Result.SUCCESS;
        }
        return Result.FAILED;
    }

    public void startNewGame() {
        if (gameOver) {
            Arrays.fill(this.board, 0);

            // Let the player know his turn is up!
            // More useful for notifications (Atleast for human players)
            Game.getPlayerSet().getAll().forEach(Scoreboard.INSTANCE::prepareScore);

            this.gameOver = false;

            Game.getGameFrame().update(this);
            getActivePlayer().commitTurn(this);
        }
    }

    public void endGame() {
        this.gameOver = true;
    }

    public void update(boolean checkWinner) {
        if (isGameOver()) return;
        if (checkWinner) {
            var winner = findWinner();
            if (winner != null)
                Scoreboard.INSTANCE.addScore(winner);
            if (gameOver)
                Scoreboard.INSTANCE.incrementGameCount();
        }

        Game.getPlayerSet().updateAll(this);
        Game.getGameFrame().update(this);

        if (isGameOver()) {
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {}
                startNewGame();
            }).start();
        }
    }

    public Player findWinner() {
        // Check rows for a winner
        for (int i = 0; i < 3; i++) {
            int start = i * 3;  // Start of the row
            if (board[start] != 0 && board[start] == board[start + 1] && board[start] == board[start + 2]) {
                Player winningPlayer = getPlayerById(board[start]);
                finalMessage = winningPlayer.getName() + " won!"; // Player in this row wins
                gameOver = true;
                return winningPlayer;  // Exit after we find a winner
            }
        }

        // Check columns for a winner
        for (int i = 0; i < 3; i++) {
            if (board[i] != 0 && board[i] == board[i + 3] && board[i] == board[i + 6]) {
                Player winningPlayer = getPlayerById(board[i]);
                finalMessage = winningPlayer.getName() + " won!"; // Player in this column wins
                gameOver = true;
                return winningPlayer;  // Exit after we find a winner
            }
        }

        // Check diagonals for a winner
        if (board[0] != 0 && board[0] == board[4] && board[0] == board[8]) {
            Player winningPlayer = getPlayerById(board[0]);
            finalMessage = winningPlayer.getName() + " won!"; // Player in this diagonal wins
            gameOver = true;
            return winningPlayer;  // Exit after we find a winner
        }

        if (board[2] != 0 && board[2] == board[4] && board[2] == board[6]) {
            Player winningPlayer = getPlayerById(board[2]);
            finalMessage = winningPlayer.getName() + " won!"; // Player in this diagonal wins
            gameOver = true;
            return winningPlayer;  // Exit after we find a winner
        }

        // If no winner is found, check if the board is completely filled
        boolean filled = true;
        for (int i : board) {
            if (i == 0) {  // If any position is empty, the board isn't filled
                filled = false;
                break;  // No need to continue checking
            }
        }

        // If the board is filled but there's no winner, it's a draw
        if (filled) {
            finalMessage = "The game is a draw!";  // Mark the game as a draw
            gameOver = true;
        }

        return null;
    }
}
