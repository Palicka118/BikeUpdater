// src/main/java/com/pawel/service/SeenService.java
package com.pawel.service;

import com.google.gson.reflect.TypeToken;
import com.pawel.infrastructure.AppPaths;
import com.pawel.infrastructure.JsonStorageService;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class SeenService {

    private final JsonStorageService storage;
    private final Path seenPath;

    private Set<String> seen = new HashSet<>();

    public SeenService(JsonStorageService storage, AppPaths paths) {
        this.storage = storage;
        this.seenPath = paths.seenJson();
        load();
    }

    public void load() {
        Type type = new TypeToken<Set<String>>() {}.getType();
        this.seen = storage.load(seenPath, type, new HashSet<>());
        normalizeInPlace();
    }

    public void save() {
        normalizeInPlace();
        storage.save(seenPath, seen);
    }

    public boolean isSeen(String bikeId) {
        String id = norm(bikeId);
        return !id.isEmpty() && seen.contains(id);
    }

    public void markSeen(String bikeId) {
        String id = norm(bikeId);
        if (id.isEmpty()) return;
        seen.add(id);
        save();
    }

    public void clear() {
        seen.clear();
        save();
    }

    public Set<String> snapshot() {
        normalizeInPlace();
        return new HashSet<>(seen);
    }

    private void normalizeInPlace() {
        Set<String> normalized = new HashSet<>();
        for (String s : seen) {
            String n = norm(s);
            if (!n.isEmpty()) normalized.add(n);
        }
        this.seen = normalized;
    }

    private String norm(String raw) {
        if (raw == null) return "";
        String s = raw.trim();
        if (s.startsWith("//")) s = "https:" + s; // migrate old stored keys
        return s;
    }
}
