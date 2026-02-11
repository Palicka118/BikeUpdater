// src/main/java/com/pawel/service/BikeService.java
package com.pawel.service;

import com.google.gson.reflect.TypeToken;
import com.pawel.infrastructure.AppPaths;
import com.pawel.infrastructure.JsonStorageService;
import com.pawel.model.Bike;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class BikeService {

    private final JsonStorageService storage;
    private final Path bikesPath;

    public BikeService(JsonStorageService storage, AppPaths paths) {
        this.storage = storage;
        this.bikesPath = paths.bikesJson();
    }

    public List<Bike> loadBikes() {
        Type type = new TypeToken<List<Bike>>() {}.getType();
        List<Bike> bikes = storage.load(bikesPath, type, new ArrayList<>());
        return bikes != null ? bikes : List.of();
    }

    public Map<String, List<Bike>> groupByMake(List<Bike> bikes) {
        if (bikes == null) return Map.of();

        Map<String, List<Bike>> grouped = new TreeMap<>();
        for (Bike b : bikes) {
            if (b == null) continue;

            String make = normalizeMake(b.getMake());
            grouped.computeIfAbsent(make, k -> new ArrayList<>()).add(b);
        }
        return grouped;
    }

    public List<Bike> filterNew(List<Bike> bikes, Set<String> seenKeys) {
        if (bikes == null) return List.of();
        if (seenKeys == null) seenKeys = Set.of();

        Set<String> finalSeenKeys = normalizeKeys(seenKeys);

        return bikes.stream()
                .filter(Objects::nonNull)
                .filter(b -> !finalSeenKeys.contains(norm(b.getId())))
                .toList();
    }

    private Set<String> normalizeKeys(Set<String> keys) {
        return keys.stream()
                .filter(Objects::nonNull)
                .map(this::norm)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    private String norm(String raw) {
        if (raw == null) return "";
        String s = raw.trim();
        if (s.startsWith("//")) s = "https:" + s;
        return s;
    }

    private String normalizeMake(String make) {
        if (make == null || make.isBlank()) return "Unknown";
        String s = make.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
        if (s.isEmpty()) return "Unknown";
        return s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1);
    }
}
