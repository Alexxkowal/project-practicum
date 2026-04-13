package ru.tbank.practicum.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class FileUtils {
    public static String getFileAsString(String path) throws IOException {
        return getFileAsString(path, Charset.forName("UTF-8"));
    }

    public static String getFileAsString(String path, Charset charset) throws IOException {
        File file = getFileFromResource(path);
        return Files.readString(file.toPath(), charset);
    }

    private static File getFileFromResource(String path) {
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource == null) {
            throw new IllegalArgumentException("File not found: " + path);
        }
        try {
            return new File(resource.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
