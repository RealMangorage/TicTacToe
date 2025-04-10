package org.mangorage.scanner.api;

import java.lang.annotation.Annotation;
import java.util.List;

public interface Scanner {
    List<Class<?>> findClassesWithAnnotation(Class<? extends Annotation> annotation);
}
