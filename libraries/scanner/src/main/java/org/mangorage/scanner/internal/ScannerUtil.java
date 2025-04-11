package org.mangorage.scanner.internal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class ScannerUtil {

    static void scanPath(final List<Class<?>> classes, final Path path, final ClassLoader classLoader) {
        if (Files.isDirectory(path)) {
            scanDirectoryForClasses(classes, path, classLoader);
            return;
        }

        if (path.toString().endsWith(".jar")) {
            scanJarForClasses(classes, path, classLoader);
        }
    }

    // Method to scan a directory for .class files
    static void scanDirectoryForClasses(final List<Class<?>> classes, final Path directory, final ClassLoader loader) {
        try (var walk = Files.walk(directory)) {
            var paths = walk
                    .filter(Files::isRegularFile)
                    .toArray(Path[]::new);
            for (Path path : paths) {
                handleClass(classes, path, loader);
            }
        } catch (IOException ignored) {}
    }

    // Method to scan a JAR file for .class files
    static void scanJarForClasses(final List<Class<?>> classes, final Path jar, final ClassLoader classLoader) {
        try (ZipFile zipFile = new ZipFile(jar.toFile())) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                handleClass(classes, entry.getName(), classLoader);
            }
        } catch (IOException ignored) {}
    }

    // Do any extra checks here, as its directory, and not in a jar...
    static void handleClass(final List<Class<?>> classes, final Path path, final ClassLoader loader) {
        handleClass(classes, path.toString(), loader);
    }

    static void handleClass(final List<Class<?>> classes, String path, final ClassLoader loader) {
        if (!path.contains(".class") || path.contains("META-INF/")) return;
        if (path.contains("module-info.class") || path.contains("package-info.class")) return;

        // This is most likely dev env
        if (path.contains("java\\main"))
            path = path.substring(path.indexOf("java\\main") + 10);

        // Now get it in a reference-able state, for Class.forName()
        path = path.replace("\\", ".").replace("/", ".").replace(".class", "");

        try {
            classes.add(
                    Class.forName(
                            path,
                            false,
                            loader
                    )
            );
        } catch (Throwable ignored) {}
    }

}
