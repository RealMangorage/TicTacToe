package org.mangorage.scanner.api;

import java.io.InputStream;
import java.net.URL;

public sealed interface Resource permits org.mangorage.scanner.internal.ResourceImpl {
    URL get();
    InputStream getAsStream();
}
