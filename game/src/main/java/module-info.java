module org.mangorage.tictactoe {
    requires java.desktop;
    requires org.mangorage.scanner;
    requires org.mangorage.networking;

    exports org.mangorage.game.api;
    exports org.mangorage.game.players;
}