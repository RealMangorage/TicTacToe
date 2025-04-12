package org.mangorage.game.core;

import org.mangorage.game.Main;
import org.mangorage.game.api.Mod;
import org.mangorage.game.players.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public final class PlayerType {
    private static final List<PlayerType> REGISTRY_WRITE = new ArrayList<>();
    public static final List<PlayerType> REGISTRY = Collections.unmodifiableList(REGISTRY_WRITE);

    private static boolean loaded = false;
    private static boolean frozen = true;

    public static void register(String id, Function<String, Player> ctor) {
        if (frozen) return;
        REGISTRY_WRITE.add(
                new PlayerType(
                        id,
                        ctor
                )
        );
    }

    public static PlayerType[] values() {
        return REGISTRY.stream().toArray(PlayerType[]::new);
    }

    public static void init() {
        if (loaded) return;

        frozen = false;
        Main.getScanner().findClassesWithPredicate(clz -> clz.isAnnotationPresent(Mod.class)).forEach(clz -> {
            try {
                clz.getConstructor().newInstance();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });

        // Finish up loading PlayerTypes, no more need for registering...
        frozen = true;
        loaded = true;
    }

    private final String id;
    private final Function<String, Player> function;

    PlayerType(String id, Function<String, Player> function) {
        this.id = id;
        this.function = function;
    }

    public Player create(String name) {
        return function.apply(name);
    }

    @Override
    public String toString() {
        return id;
    }
}
