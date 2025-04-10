module org.mangorage.tictactoe {
    requires java.desktop;
    requires org.mangorage.scanner;
    requires org.mangorage.buffer;

    exports org.mangorage.game.api;
    exports org.mangorage.game.players;
}