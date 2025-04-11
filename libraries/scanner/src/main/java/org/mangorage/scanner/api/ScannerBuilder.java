package org.mangorage.scanner.api;

import org.mangorage.scanner.internal.ScannerImpl;

import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;

public sealed interface ScannerBuilder permits ScannerImpl.Builder {
    static ScannerBuilder of() {
        return ScannerImpl.Builder.of();
    }

    ScannerBuilder addPath(final ClassLoader classLoader, final Path path);

    default ScannerBuilder addPaths(final ClassLoader classLoader, final List<Path> paths) {
        paths.forEach(p -> addPath(classLoader, p));
        return this;
    }

    ScannerBuilder addClassloader(final URLClassLoader loader);

    ScannerBuilder addClasspath(final ClassLoader loader);

    Scanner build();
}
