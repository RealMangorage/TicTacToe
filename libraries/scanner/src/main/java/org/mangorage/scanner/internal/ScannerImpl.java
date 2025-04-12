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
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class ScannerImpl implements Scanner {
    private final Map<ClassLoader, List<Path>> paths_per_loader;
    private final ScanCache cache = new ScanCache(new ArrayList<>(), new ArrayList<>());


    ScannerImpl(final Map<ClassLoader, List<Path>> paths_per_loader) {
        final Map<ClassLoader, List<Path>> map = new HashMap<>();

        paths_per_loader.forEach((k, v) -> {
            map.put(k, List.copyOf(v));
        });

        this.paths_per_loader = Map.copyOf(map);
    }

    @Override
    public void commitScan() {
        paths_per_loader.forEach((k, v) -> {
            for (final Path path : v) {
                ScannerUtil.scanPath(cache, path, k);
            }
        });
    }

    @Override
    public List<Class<?>> findClassesWithAnnotation(final Class<? extends Annotation> annotation) {
        return cache.classes()
                .stream()
                .filter(clz -> clz.isAnnotationPresent(annotation))
                .toList();
    }

    @Override
    public List<Class<?>> findClassesWithPredicate(final Predicate<Class<?>> predicate) {
        return cache.classes()
                .stream()
                .filter(predicate)
                .toList();
    }

    @Override
    public Stream<Resource> findResource(final Predicate<String> predicate) {
        return cache.data()
                .stream()
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
