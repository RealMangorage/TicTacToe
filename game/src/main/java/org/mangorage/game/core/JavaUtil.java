package org.mangorage.game.core;

import org.mangorage.game.api.Mod;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class JavaUtil {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Scan all classes on the classpath and filter by annotation
        List<Class<?>> annotatedClasses = getClassesWithAnnotation(Mod.class);

        // Output the annotated classes
        for (Class<?> clazz : annotatedClasses) {
            System.out.println("Found annotated class: " + clazz.getName());
        }
    }

    public static List<Class<?>> getClassesWithAnnotation(Class<? extends Annotation> annotation) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();

        // Get classpath URL (will include all classes)
        String classpath = System.getProperty("java.class.path");
        String[] classpathEntries = classpath.split(File.pathSeparator);

        for (String classpathEntry : classpathEntries) {
            File file = new File(classpathEntry);

            if (file.isDirectory()) {
                // If it's a directory, scan all files
                scanDirectory(file, annotation, classes);
            } else if (classpathEntry.endsWith(".jar")) {
                // If it's a JAR, scan the JAR for classes
                scanJarFile(file, annotation, classes);
            }
        }
        return classes;
    }

    private static void scanDirectory(File directory, Class<? extends Annotation> annotation, List<Class<?>> classes) throws ClassNotFoundException {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                // Recursively scan subdirectories
                scanDirectory(file, annotation, classes);
            } else if (file.getName().endsWith(".class") && !file.getName().equals("module-info.class")) {
                // This file is a .class file, now load it
                String path = file.getPath();
                path = path.substring(path.indexOf("main\\") + 5);

                String className = path.replace("\\", ".").replace(".class", "");

                try {
                    Class<?> clazz = Class.forName(className);

                    // Exclude classes with "Field" in the name
                    if (clazz.isAnnotationPresent(annotation) && !clazz.getName().contains("Field")) {
                        classes.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    // This will be caught if the class isn't found; continue scanning other files
                }
            }
        }
    }

    private static void scanJarFile(File jarFile, Class<? extends Annotation> annotation, List<Class<?>> classes) throws IOException, ClassNotFoundException {
        try (ZipFile zipFile = new ZipFile(jarFile)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();

                // Skip module descriptor files: "META-INF/versions/9/module-info.class" or just "module-info.class"
                if (name.endsWith(".class") && !name.equals("module-info.class") && !name.contains("META-INF/versions")) {
                    // This entry is a .class file, now load it
                    // This file is a .class file, now load it
                    name = name.substring(name.indexOf("main\\") + 5);

                    String className = name.replace("\\", ".").replace(".class", "");
                    System.out.println(className);
                    try {
                        // Check if the class is valid and load it
                        Class<?> clazz = Class.forName(className.replace("/", "."), false, JavaUtil.class.getClassLoader());

                        // Exclude classes with "Field" in the name
                        if (clazz.isAnnotationPresent(annotation) && !clazz.getName().contains("Field")) {
                            classes.add(clazz);
                        }
                    } catch (ClassNotFoundException e) {
                        // This will be caught if the class isn't found; continue scanning other files
                    }
                }
            }
        }
    }

}
