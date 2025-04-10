module org.mangorage.tictactoe {
    requires java.desktop;
    requires netty.buffer;
    requires org.mangorage.scanner;

    exports org.mangorage.game.api;
    exports org.mangorage.game.players;
}