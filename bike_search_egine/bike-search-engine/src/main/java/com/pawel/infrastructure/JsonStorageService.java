// src/main/java/com/pawel/infrastructure/JsonStorageService.java
package com.pawel.infrastructure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonStorageService {

    private final Gson gson;

    public JsonStorageService() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public <T> T load(Path path, Type type, T defaultValue) {
        try {
            if (!Files.exists(path)) return defaultValue;
            try (Reader r = Files.newBufferedReader(path)) {
                T val = gson.fromJson(r, type);
                return val != null ? val : defaultValue;
            }
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public void save(Path path, Object data) {
        try {
            Files.createDirectories(path.getParent());
            try (Writer w = Files.newBufferedWriter(path)) {
                gson.toJson(data, w);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to save JSON: " + path, e);
        }
    }
}
