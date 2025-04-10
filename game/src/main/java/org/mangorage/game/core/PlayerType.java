package org.mangorage.game.core;

import org.mangorage.game.api.Mod;
import org.mangorage.game.players.Player;
import org.mangorage.scanner.api.Scanner;
import org.mangorage.scanner.api.ScannerBuilder;

import java.io.File;
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

        var playerJars = getFilesInFolder(Path.of("players"));

        URLClassLoader playerClassloader = new URLClassLoader(
                playerJars
                        .stream()
                        .map(
                                p -> {
                                    try {
                                        return p.toUri().toURL();
                                    } catch (MalformedURLException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                        )
                        .toArray(URL[]::new)
        );

        var classpath = System.getProperty("java.class.path");
        var paths = Arrays.stream(classpath.split("\\;"))
                .map(Path::of)
                .toList();

        Scanner scanner = new ScannerBuilder()
                .addPath(paths)
                .addPath(playerClassloader, playerJars)
                .build();

        frozen = false;
        scanner.findClassesWithAnnotation(Mod.class).forEach(clz -> {
            System.out.println(clz);
            try {
                clz.getConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        });

        // Finish up loading PlayerTypes, no more need for registering...
        frozen = true;
        loaded = true;
    }

    @SuppressWarnings("all")
    public static List<Path> getFilesInFolder(Path folderPath) {
        if (!Files.isDirectory(folderPath) || !Files.exists(folderPath)) return List.of();

        return Arrays.stream(folderPath.toFile().listFiles())
                .map(File::toPath)
                .toList();
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
