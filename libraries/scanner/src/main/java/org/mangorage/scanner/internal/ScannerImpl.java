package org.mangorage.scanner.internal;

import org.mangorage.scanner.api.Resource;
import org.mangorage.scanner.api.Scanner;
import org.mangorage.scanner.api.ScannerBuilder;

import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class ScannerImpl implements Scanner {
    private final Map<ClassLoader, List<Path>> paths;
    private final ScanCache cache = ScanCache.createSync();


    ScannerImpl(final Map<ClassLoader, List<Path>> paths) {
        final Map<ClassLoader, List<Path>> map = new HashMap<>();

        paths.forEach((k, v) -> {
            map.put(k, List.copyOf(v));
        });

        this.paths = Map.copyOf(map);
    }

    @Override
    public void commitScan() {
        var initial = System.currentTimeMillis();
        List<Runnable> tasks = new CopyOnWriteArrayList<>();
        paths.entrySet()
                .stream()
                .parallel()
                .forEach(set -> {
                    final var cl = set.getKey();
                    set.getValue()
                            .stream()
                            .parallel()
                            .forEach(path -> {
                                tasks.add(() -> ScannerUtil.scanPath(cache, path, cl));
                            });
                });

        // Stage 1
        ScannerUtil.executeTasks(tasks);

        // Stage 2
        ScannerUtil.executeTasks();

        var total = System.currentTimeMillis() - initial;
        System.out.println("Took %s ms".formatted(total));
    }

    @Override
    public List<Class<?>> findClassesWithAnnotation(final Class<? extends Annotation> annotation) {
        return cache.classes()
                .stream()
                .parallel()
                .filter(clz -> clz.isAnnotationPresent(annotation))
                .toList();
    }

    @Override
    public List<Class<?>> findClassesWithPredicate(final Predicate<Class<?>> predicate) {
        return cache.classes()
                .stream()
                .parallel()
                .filter(predicate)
                .toList();
    }

    @Override
    public Stream<Resource> findResource(final Predicate<String> predicate) {
        return cache.data()
                .stream()
                .parallel()
                .filter(resource -> predicate.test(resource.path()))
                .map(UnbakedResource::bake);
    }

    public static final class Builder implements ScannerBuilder {
        public static ScannerBuilder of() {
            return new Builder();
        }

        private final Map<ClassLoader, List<Path>> paths_per_loader = new HashMap<>();

        Builder() {}

        public Builder addPath(final ClassLoader classLoader, final Path path) {
            paths_per_loader.computeIfAbsent(classLoader, l -> new ArrayList<>()).add(path);
            return this;
        }

        public Builder addClassloader(final URLClassLoader loader) {
            for (URL url : loader.getURLs()) {
                try {
                    addPath(loader, Path.of(url.toURI()));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
            return this;
        }

        public Builder addClasspath(final ClassLoader loader) {
            var classpath = System.getProperty("java.class.path");
            var paths = Arrays.stream(classpath.split("\\;"))
                    .map(Path::of)
                    .toList();
            paths.forEach(p -> {
                addPath(loader, p);
            });
            return this;
        }

        public Scanner build() {
            return new ScannerImpl(paths_per_loader);
        }
    }

}
