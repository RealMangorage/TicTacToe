package org.mangorage.scanner.internal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class ScannerUtil {

    public static List<Class<?>> scanPath(Path path, ClassLoader classLoader) {
        if (Files.isDirectory(path)) {
            return scanDirectoryForClasses(path, classLoader);
        }

        if (path.toString().endsWith(".jar")) {
            return scanJarForClasses(path, classLoader);
        }


        return List.of();
    }


    // Method to scan a directory for .class files
    public static List<Class<?>> scanDirectoryForClasses(Path directory, ClassLoader loader) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            Files.walk(directory)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".class"))
                    .filter(path -> !path.toString().contains("module-info.class"))
                    .filter(path -> !path.toString().contains("META-INF"))
                    .forEach(path -> {
                        try {
                            String className = getClassNameFromPath(directory, path);
                            Class<?> clazz = Class.forName(className, false, loader);
                            classes.add(clazz);
                        } catch (ClassNotFoundException e) {
                            // Handle the exception (class may not be found in the classpath)
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    // Helper method to convert file path to class name
    private static String getClassNameFromPath(Path baseDirectory, Path classFile) {
        String className = baseDirectory.relativize(classFile).toString();
        className = className.replace(File.separator, ".").replace(".class", "");
        return className;
    }

    // Method to scan a JAR file for .class files
    public static List<Class<?>> scanJarForClasses(Path jar, ClassLoader classLoader) {
        List<Class<?>> classes = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile(jar.toFile())) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.endsWith(".class") && !name.endsWith("module-info.class") && !name.contains("META-INF")) {
                    String className = name.replace("/", ".").substring(0, name.length() - 6); // remove .class
                    try {
                        Class<?> clazz = Class.forName(className, false, classLoader);
                        classes.add(clazz);
                    } catch (ClassNotFoundException e) {
                        // Handle the exception (class may not be found in the classpath)
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }
}
