package org.mangorage.scanner.internal;

import org.mangorage.scanner.api.Resource;

import java.io.InputStream;
import java.net.URL;

public record ResourceImpl(ClassLoader loader, String path) implements Resource {

    @Override
    public URL get() {
        return loader.getResource(path);
    }

    @Override
    public InputStream getAsStream() {
        return loader.getResourceAsStream(path);
    }
}
