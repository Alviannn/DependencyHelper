package com.github.alviannn.lib.dependencyhelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@SuppressWarnings({"unused", "WeakerAccess"})
public class DependencyHelper {

    private final ClassLoader classLoader;

    public DependencyHelper(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public DependencyHelper(Class<?> clazz) {
        this.classLoader = clazz.getClassLoader();
    }

    /**
     * downloads a dependency file
     *
     * @param fileName the file name
     * @param fileUrl  the file url
     * @param dirPath  the directory path
     * @throws IOException if the download fails
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void download(String fileName, String fileUrl, Path dirPath) throws IOException {
        File dir = dirPath.toFile();
        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(dir, fileName);
        if (file.exists()) {
            return;
        }

        try {
            URL url = new URL(fileUrl);
            try (InputStream stream = url.openStream()) {
                Files.copy(stream, file.toPath());
            }
        } catch (Exception e) {
            throw new IOException("Failed to download " + fileName);
        }
    }

    /**
     * downloads a dependency file
     * <p>
     * without file name parameter, it will automatically take the name from the URL (might not work sometimes)
     *
     * @param fileUrl the file url
     * @param dirPath the directory path
     * @throws IOException if the download fails
     */
    @Deprecated
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void download(String fileUrl, Path dirPath) throws IOException {
        File dir = dirPath.toFile();
        if (!dir.exists()) {
            dir.mkdir();
        }

        String[] strArray = fileUrl.split("/");
        String fileName = strArray[strArray.length - 1];

        File file = new File(dir, fileName);
        if (file.exists()) {
            return;
        }

        try {
            URL url = new URL(fileUrl);
            try (InputStream stream = url.openStream()) {
                Files.copy(stream, file.toPath());
            }
        } catch (Exception e) {
            throw new IOException("Failed to download " + fileName);
        }
    }

    /**
     * loads the dependency file
     *
     * @param fileName the name
     * @param fileUrl  the file url
     * @param dirPath  the directory path
     * @throws IOException  if the dependency file doesn't exists
     * @throws IllegalAccessException if the dependency file isn't a .jar
     */
    public void load(String fileName, String fileUrl, Path dirPath) throws IOException, IllegalAccessException {
        File dir = dirPath.toFile();
        if (!dir.exists()) {
            return;
        }

        File file = new File(dir, fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("Cannot file " + fileName + "!");
        }
        if (!file.getName().endsWith(".jar")) {
            throw new IllegalAccessException("Cannot load file that aren't .jar(s)!");
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
                throw new RuntimeException("Failed to load " + fileName + "!");
            }
        }
        else {
            Thread.currentThread().setContextClassLoader(previousLoader);
            throw new RuntimeException("Failed to cast class loader!");
        }
        Thread.currentThread().setContextClassLoader(previousLoader);
    }

    /**
     * loads the dependency file
     *
     * @param file the file
     * @throws IOException  if the dependency file doesn't exists
     * @throws IllegalAccessException if the dependency file isn't a .jar
     */
    public void load(File file) throws IOException, IllegalAccessException {
        if (!file.exists()) {
            throw new FileNotFoundException("Cannot load file " + file.getName() + "!");
        }
        if (!file.getName().endsWith(".jar")) {
            throw new IllegalAccessException("Cannot load file that aren't .jar(s)!");
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
                throw new RuntimeException("Failed to load " + file.getName() + "!");
            }
        }
        else {
            Thread.currentThread().setContextClassLoader(previousLoader);
            throw new RuntimeException("Failed to cast class loader!");
        }
        Thread.currentThread().setContextClassLoader(previousLoader);
    }

    /**
     * loads all dependencies on a specific directory
     *
     * @param dirPath the directory path
     * @throws IOException  if the dependency file doesn't exists
     * @throws IllegalAccessException if the dependency file isn't a .jar
     */
    @SuppressWarnings("ConstantConditions")
    public void loadDir(Path dirPath) throws IOException, IllegalAccessException {
        File dir = dirPath.toFile();
        if (!dir.isDirectory()) {
            return;
        }

        for (File file : dir.listFiles()) {
            if (!file.getName().endsWith(".jar")) {
                continue;
            }
            this.load(file);
        }
    }

    /**
     * loads the dependencies
     *
     * @param dependencies the dependencies
     * @param dirPath      the directory path
     * @throws IOException  if the dependency file doesn't exists
     * @throws IllegalAccessException if the dependency file isn't a .jar
     */
    public void load(Map<String, String> dependencies, Path dirPath) throws IOException, IllegalAccessException {
        for (Map.Entry<String, String> entry : dependencies.entrySet()) {
            this.load(entry.getKey(), entry.getValue(), dirPath);
        }
    }

    /**
     * downloads the dependencies
     *
     * @param dependencies the dependencies
     * @param dirPath      the directory path
     * @throws IOException if the download fails
     */
    public void download(Map<String, String> dependencies, Path dirPath) throws IOException {
        for (Map.Entry<String, String> entry : dependencies.entrySet()) {
            this.download(entry.getKey(), entry.getValue(), dirPath);
        }
    }

}
