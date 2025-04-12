package org.mangorage.scanner.api;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public sealed interface Scanner permits org.mangorage.scanner.internal.ScannerImpl {
    // Tell the scanner to make an attempt at scanning...
    void commitScan();

    // find the classes with said annotation from the cache
    List<Class<?>> findClassesWithAnnotation(final Class<? extends Annotation> annotation);

    List<Class<?>> findClassesWithPredicate(final Predicate<Class<?>> predicate);

    Stream<Resource> findResource(final Predicate<String> predicate);
}
