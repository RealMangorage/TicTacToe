package org.mangorage.scanner.internal;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public record ScanCache(List<Class<?>> classes, List<UnbakedResource> data) {
    static ScanCache createSync() {
        return new ScanCache(
                new CopyOnWriteArrayList<>(),
                new CopyOnWriteArrayList<>()
        );
    }
}
