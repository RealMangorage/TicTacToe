package org.mangorage.scanner.api;

import org.mangorage.scanner.internal.ScannerImpl;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ScannerBuilder {
    public static void main(String[] args) {
        ScannerBuilder builder = new ScannerBuilder();

        builder
                .addPath(
                        Path.of("someJar")
                )
                .addPath(
                        Path.of(
                                "someDirectory"
                        )
                );
    }

    private final List<Path> paths = new ArrayList<>(); // Paths to scan
    private final Map<ClassLoader, List<Path>> paths_per_loader = new HashMap<>();


    public ScannerBuilder addPath(Path path) {
        paths.add(path);
        return this;
    }

    public ScannerBuilder addPath(List<Path> paths) {
        paths.forEach(this::addPath);
        return this;
    }

    public ScannerBuilder addPath(ClassLoader classLoader, Path path) {
        paths_per_loader.computeIfAbsent(classLoader, l -> new ArrayList<>()).add(path);
        return this;
    }

    public ScannerBuilder addPath(ClassLoader classLoader, List<Path> path) {
        path.forEach(p -> addPath(classLoader, p));
        return this;
    }

    public Scanner build() {
        return new ScannerImpl(paths, paths_per_loader);
    }
}
