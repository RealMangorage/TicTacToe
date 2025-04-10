package org.mangorage.scanner.internal;

import org.mangorage.scanner.api.Scanner;

import java.lang.annotation.Annotation;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ScannerImpl implements Scanner {

    private final List<Path> paths;
    private final Map<ClassLoader, List<Path>> paths_per_loader;


    public ScannerImpl(final List<Path> paths, final Map<ClassLoader, List<Path>> paths_per_loader) {
        this.paths = paths;

        Map<ClassLoader, List<Path>> map = new HashMap<>();

        paths_per_loader.forEach((k, v) -> {
            map.put(k, List.copyOf(v));
        });

        this.paths_per_loader = Map.copyOf(map);
    }

    @Override
    public List<Class<?>> findClassesWithAnnotation(Class<? extends Annotation> annotation) {

        List<Class<?>> classes = new ArrayList<>();

        for (Path path : paths) {
            classes.addAll(ScannerUtil.scanPath(path, ScannerUtil.class.getClassLoader()));
        }

        paths_per_loader.forEach((k, v) -> {
            for (Path path : v) {
                classes.addAll(ScannerUtil.scanPath(path, k));
            }
        });


        return classes.stream()
                .filter(clz -> clz.isAnnotationPresent(annotation))
                .toList();
    }
}
