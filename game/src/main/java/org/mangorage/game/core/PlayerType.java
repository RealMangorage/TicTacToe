package org.mangorage.game.core;

import org.mangorage.game.api.Mod;
import org.mangorage.game.players.Player;
import org.mangorage.scanner.api.Scanner;
import org.mangorage.scanner.api.ScannerBuilder;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
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

        URLClassLoader playerClassloader = new URLClassLoader(getFilesInFolder(Path.of("players")));

        Scanner scanner = ScannerBuilder.of()
                .addClasspath(PlayerType.class.getClassLoader())
                .addClassloader(playerClassloader)
                .build();

        scanner.commitScan();

        frozen = false;
        scanner.findClassesWithPredicate(clz -> clz.isAnnotationPresent(Mod.class)).forEach(clz -> {
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

    @SuppressWarnings("all")
    public static URL[] getFilesInFolder(Path folderPath) {
        if (!Files.isDirectory(folderPath)) return new URL[]{};
        return Arrays.stream(folderPath.toFile().listFiles())
                .map(file -> {
                    try {
                        return file.toURI().toURL();
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toArray(URL[]::new);
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
