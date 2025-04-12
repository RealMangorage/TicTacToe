package org.mangorage.game;

import org.mangorage.game.core.PlayerType;
import org.mangorage.game.network.Network;
import org.mangorage.scanner.api.Scanner;
import org.mangorage.scanner.api.ScannerBuilder;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public final class Main {
    private static final Scanner scanner;

    static {
        URLClassLoader playerClassloader = new URLClassLoader(getFilesInFolder(Path.of("players")));

        scanner = ScannerBuilder.of()
                .addClasspath(PlayerType.class.getClassLoader())
                .addClassloader(playerClassloader)
                .build();

        scanner.commitScan();
    }

    public static Scanner getScanner() {
        return scanner;
    }

    @SuppressWarnings("all")
    static URL[] getFilesInFolder(Path folderPath) {
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


    public static void main(final String[] args) {
        PlayerType.init();
        Network.init();

        SwingUtilities.invokeLater(() -> {
            Game.init();
            Game.getMainMenuFrame().setVisible(true);
        });
    }
}
