package org.mangorage.game.core;

import org.mangorage.game.api.Mod;
import org.mangorage.game.players.BasicAiPlayer;
import org.mangorage.game.players.HumanPlayer;
import org.mangorage.game.players.Player;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
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

        URLClassLoader playerClassloader = new URLClassLoader(
                getFilesInFolder("players")
        );

        final Reflections reflections = new Reflections(
                ConfigurationBuilder.build()
                        .setClassLoaders(
                                new ClassLoader[]{
                                        playerClassloader
                                }
                        )
                        .setUrls(
                                getFilesInFolder("players")
                        )
        );

        var classes = reflections.getTypesAnnotatedWith(Mod.class);

        // un freeze it, so we can register new player types!
        frozen = false;

        classes.forEach(clz -> {
            try {
                clz.getConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        });

        // Default
        register("player", HumanPlayer::new);
        register("computer", BasicAiPlayer::new);

        // Finish up loading PlayerTypes, no more need for registering...
        frozen = true;
        loaded = true;
    }

    public static URL[] getFilesInFolder(String folderPath) {
        List<URL> urlList = new ArrayList<>();
        File folder = new File(folderPath);

        // Check if the folder exists and is actually a directory
        if (folder.exists() && folder.isDirectory()) {
            File[] fileArray = folder.listFiles();

            if (fileArray != null) {
                for (File file : fileArray) {
                    try {
                        // Convert each file to URL and add to the list
                        urlList.add(file.toURI().toURL());
                    } catch (Exception e) {
                        System.err.println("Error converting file to URL: " + file.getAbsolutePath());
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("Provided path is not a valid folder.");
        }

        // Convert the list to URL[] array and return it
        return urlList.toArray(new URL[0]);
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
