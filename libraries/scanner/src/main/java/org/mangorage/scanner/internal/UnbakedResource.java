package org.mangorage.scanner.internal;

import org.mangorage.scanner.api.Resource;

public record UnbakedResource(ClassLoader loader, String path) {
    public Resource bake() {
        return new ResourceImpl(loader, path);
    }
}
