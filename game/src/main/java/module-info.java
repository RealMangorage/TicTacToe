module org.mangorage.tictactoe {
    requires java.desktop;
    requires org.reflections;
    requires netty.buffer;
    requires org.slf4j;

    exports org.mangorage.game.api;
    exports org.mangorage.game.players;
}