package org.mangorage.scanner.internal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import java.util.zip.ZipFile;

public final class ScannerUtil {
    private static final ExecutorService service = Executors.newCachedThreadPool();

    private static final List<Runnable> tasks = new CopyOnWriteArrayList<>();

    static void scheduleTask(Runnable runnable) {
        tasks.add(runnable);
    }

    static void executeTasks() {
        executeTasks(tasks);
    }

    static void executeTasks(List<Runnable> tasks) {
        tasks
                .stream()
                .parallel()
                .map(service::submit)
                .forEach(task -> {
                    try {
                        task.get();
                    } catch (Throwable ignored) {}
                });

        tasks.clear(); // cleanup, assuming tasks are one-shot
    }

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
    // Method to scan a directory for .class files
    static void scanDirectoryForClasses(final ScanCache cache, final Path directory, final ClassLoader loader) {
        try (Stream<Path> walk = Files.walk(directory)) {
            walk
                    .parallel()  // Parallelize the stream processing
                    .filter(Files::isRegularFile)
                    .forEach(path -> scheduleTask(() -> {
                        try {
                            handleClass(cache, path, loader);
                        } catch (Exception e) {
                            System.err.println("Error handling file: " + path);
                        }
                    }));
        } catch (IOException e) {
            System.err.println("Error walking directory: " + directory);
        }
    }

    // Method to scan a JAR file for .class files
    static void scanJarForClasses(final ScanCache cache, final Path jar, final ClassLoader classLoader) {
        try (ZipFile zipFile = new ZipFile(jar.toFile())) {
            zipFile.entries()
                    .asIterator()
                    .forEachRemaining(entry -> scheduleTask(() -> {
                        try {
                            handleClass(cache, entry.getName(), classLoader);
                        } catch (Throwable e) {
                            System.err.println("Error handling class: " + entry.getName());
                        }
                    }));
        } catch (IOException e) {
            System.err.println("Error reading JAR file: " + jar);
        }
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
            if (path.contains(".class")) {
                cache.failed_classes()
                        .add(
                                new UnbakedResource(loader, formatPath(path, false))
                        );
            } else {
                cache.data()
                        .add(
                                new UnbakedResource(loader, formatPath(path, false))
                        );
            }
        }
    }

    static String formatPath(final String path, final boolean isClass) {
        return isClass ? path.replaceAll("^.*?java[/\\\\][^/\\\\]+[/\\\\]|\\.class$", "").replaceAll("[/\\\\]", ".") : path.replace("\\", "/").replaceFirst("^.*?resources/[^/]+/", "");
    }

}
