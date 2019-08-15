package dev.luckynetwork.lib.dependencyhelper;

import dev.luckynetwork.lib.dependencyhelper.profile.DependencyProfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;

public class DependencyHelper {

    private final ClassLoader classLoader;

    public DependencyHelper(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * handles dependency file downloading
     *
     * @param dependency the dependency profile
     * @param dirPath the directory path
     * @throws Exception if fails to download dependency file
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void download(DependencyProfile dependency, Path dirPath) throws Exception {
        File dir = dirPath.toFile();
        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(dir, dependency.getFileName());
        if (file.exists()) {
            return;
        }

        try {
            URL url = new URL(dependency.getFileUrl());
            try (InputStream is = url.openStream()) {
                Files.copy(is, file.toPath());
            }
        } catch (Exception e) {
            throw new IOException("Failed to download " + dependency.getFileName());
        }
    }

    /**
     * handles dependency loading
     *
     * @param dependency the dependency
     * @param dirPath the directory path
     * @throws Exception if fails to load dependency
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void load(DependencyProfile dependency, Path dirPath) throws Exception {
        File dir = dirPath.toFile();
        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(dir, dependency.getFileName());
        if (!file.exists()) {
            throw new FileNotFoundException("Cannot file " + dependency.getFileName() + "!");
        }

        ClassLoader previousLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);

        if (classLoader instanceof URLClassLoader) {
            URLClassLoader loader = (URLClassLoader) classLoader;
            try {
                Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                addURL.setAccessible(true);
                addURL.invoke(loader, file.toURI().toURL());
            } catch (Exception e) {
                Thread.currentThread().setContextClassLoader(previousLoader);
                throw new RuntimeException("Failed to load " + dependency.getFileName() + "!");
            }
        }
        else {
            Thread.currentThread().setContextClassLoader(previousLoader);
            throw new RuntimeException("Failed to cast class loader!");
        }
        Thread.currentThread().setContextClassLoader(previousLoader);
    }

}
