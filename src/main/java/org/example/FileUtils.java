package org.example;

import java.io.InputStream;

public class FileUtils {
    public InputStream getResourcesFile(String path) {
        return this.getClass().getClassLoader().getResourceAsStream(path);
    }
}
