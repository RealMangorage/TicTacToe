package org.mangorage.scanner.internal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class ScannerUtil {

    static void scanPath(final ScanCache cache, final Path path, final ClassLoader classLoader) {
        if (Files.isDirectory(path)) {
            scanDirectoryForClasses(cache, path, classLoader);
            return;
        }

        if (path.toString().endsWith(".jar")) {
            scanJarForClasses(cache, path, classLoader);
        }
    }

    // Method to scan a directory for .class files
    // Only needed in Dev Mode...
    static void scanDirectoryForClasses(final ScanCache cache, final Path directory, final ClassLoader loader) {
        try (var walk = Files.walk(directory)) {
            var paths = walk
                    .filter(Files::isRegularFile)
                    .toArray(Path[]::new);
            for (Path path : paths) {
                handleClass(cache, path, loader);
            }
        } catch (IOException ignored) {}
    }

    // Method to scan a JAR file for .class files
    static void scanJarForClasses(final ScanCache cache, final Path jar, final ClassLoader classLoader) {
        try (ZipFile zipFile = new ZipFile(jar.toFile())) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                handleClass(cache, entry.getName(), classLoader);
            }
        } catch (IOException ignored) {}
    }

    // Do any extra checks here, as its directory, and not in a jar...
    static void handleClass(final ScanCache cache, final Path path, final ClassLoader loader) {
        handleClass(cache, path.toString(), loader);
    }

    static void handleClass(final ScanCache cache, final String path, final ClassLoader loader) {
        // TODO [Scanner]: Make it so we can cache these too!
        if (path.contains("module-info.class") || path.contains("package-info.class")) return; // Module or Package info?

        try {
            cache.classes().add(
                    Class.forName(
                            formatPath(path, true),
                            false,
                            loader
                    )
            );
        } catch (Throwable ignored) {
            System.out.println(path);
            cache.data().add(
                    new UnbakedResource(loader, formatPath(path, false))
            );
        }
    }

    static String formatPath(final String path, final boolean isClass) {
        if (path.contains("java") || isClass) {
            return path.replaceAll("^.*?java[/\\\\][^/\\\\]+[/\\\\]|\\.class$", "").replaceAll("[/\\\\]", ".");
        }

        if (path.contains("resources")) {
            return path.replace("\\", "/").replaceFirst("^.*?resources/[^/]+/", "");
        }

        return path;
    }

}
