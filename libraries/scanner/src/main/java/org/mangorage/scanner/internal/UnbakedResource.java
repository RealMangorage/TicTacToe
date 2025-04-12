package org.mangorage.scanner.internal;

public record UnbakedResource(ClassLoader loader, String path) {
    public ResourceImpl bake() {
        return new ResourceImpl(loader, path);
    }
}
