package org.mangorage.scanner.internal;

import java.util.List;

public record ScanCache(List<Class<?>> classes, List<UnbakedResource> data) {}
