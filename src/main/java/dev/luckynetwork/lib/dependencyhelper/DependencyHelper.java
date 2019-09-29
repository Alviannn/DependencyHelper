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

@SuppressWarnings("unused")
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
     * @throws Exception if the dependency file failed to download
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
     * downloads a dependency file
     *
     * @param name the file name
     * @param fileUrl the file url
     * @param dirPath the directory path
     * @throws Exception if the dependency file failed to download
     */
    public void download(String name, String fileUrl, Path dirPath) throws Exception {
        this.download(new DependencyProfile(name, fileUrl), dirPath);
    }

    /**
     * loads the dependency file
     *
     * @param name the name
     * @param fileUrl the file url
     * @param dirPath the directory path
     * @throws Exception if the dependency failed to load
     */
    public void load(String name, String fileUrl, Path dirPath) throws Exception {
        this.load(new DependencyProfile(name, fileUrl), dirPath);
    }

    /**
     * handles dependency loading
     *
     * @param dependency the dependency profile
     * @param dirPath the directory path
     * @throws Exception if the dependency failed to load
     */
    public void load(DependencyProfile dependency, Path dirPath) throws Exception {
        File dir = dirPath.toFile();

        if (!dir.exists()) {
            return;
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
