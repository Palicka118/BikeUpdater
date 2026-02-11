// src/main/java/com/pawel/service/FavoritesService.java
package com.pawel.service;

import com.google.gson.reflect.TypeToken;
import com.pawel.infrastructure.AppPaths;
import com.pawel.infrastructure.JsonStorageService;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class FavoritesService {

    private final JsonStorageService storage;
    private final Path favoritesPath;

    private Set<String> favorites = new HashSet<>();

    public FavoritesService(JsonStorageService storage, AppPaths paths) {
        this.storage = storage;
        this.favoritesPath = paths.favoritesJson();
        load();
    }

    public void load() {
        Type type = new TypeToken<Set<String>>() {}.getType();
        this.favorites = storage.load(favoritesPath, type, new HashSet<>());
        normalizeInPlace();
    }

    public void save() {
        normalizeInPlace();
        storage.save(favoritesPath, favorites);
    }

    public boolean isFavorite(String bikeId) {
        String id = norm(bikeId);
        return !id.isEmpty() && favorites.contains(id);
    }

    public boolean toggle(String bikeId) {
        String id = norm(bikeId);
        if (id.isEmpty()) return false;

        boolean nowFavorite;
        if (favorites.contains(id)) {
            favorites.remove(id);
            nowFavorite = false;
        } else {
            favorites.add(id);
            nowFavorite = true;
        }
        save();
        return nowFavorite;
    }

    public void clear() {
        favorites.clear();
        save();
    }

    public Set<String> snapshot() {
        normalizeInPlace();
        return new HashSet<>(favorites);
    }

    private void normalizeInPlace() {
        Set<String> normalized = new HashSet<>();
        for (String s : favorites) {
            String n = norm(s);
            if (!n.isEmpty()) normalized.add(n);
        }
        this.favorites = normalized;
    }

    private String norm(String raw) {
        if (raw == null) return "";
        String s = raw.trim();
        if (s.startsWith("//")) s = "https:" + s; // migrate old stored keys
        return s;
    }
}
