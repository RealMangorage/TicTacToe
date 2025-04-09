package org.mangorage.game.players;

import org.mangorage.game.api.Board;

import java.util.function.Consumer;

public final class RemotePlayer implements Player {


    public final static class Properties {
        private String name;
        private Consumer<Board> commitTurn;

        public Properties() {}

        public String getName() {
            return name;
        }

        public Consumer<Board> getCommitTurn() {
            return commitTurn;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCommitTurn(Consumer<Board> boardConsumer) {
            this.commitTurn = boardConsumer;
        }
    }


    private final Properties properties;

    public RemotePlayer(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void commitTurn(Board board) {
        this.properties.getCommitTurn().accept(board);
    }

    @Override
    public String getName() {
        return this.properties.getName();
    }
}
