// src/main/java/com/pawel/DataUtils.java
package com.pawel.infrastructure;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DataUtils {

    private static final Gson GSON = new Gson();

    private static String resolveScriptPath(String filename) {
        File a = new File("scripts", filename);
        if (a.exists()) return a.getPath();

        File b = new File("bike-search-engine/scripts", filename);
        if (b.exists()) return b.getPath();

        return a.getPath();
    }

    public static void runPythonScript() {
        try {
            String scriptPath = resolveScriptPath("main.py");
            if (!new File(scriptPath).exists()) {
                System.err.println("Python script not found: " + scriptPath);
                return;
            }

            ProcessBuilder pb = new ProcessBuilder("python", scriptPath);
            pb.redirectErrorStream(true);

            Process process = pb.start();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Map<String, Object>> loadMotorcycleData(String filename) {
        String resolvedPath = resolveScriptPath(filename);
        try (FileReader reader = new FileReader(resolvedPath)) {
            Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
            List<Map<String, Object>> data = GSON.fromJson(reader, listType);
            return data != null ? data : List.of();
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public static Set<String> loadSeenBikes(String filename) {
        String resolvedPath = resolveScriptPath(filename);
        try (FileReader reader = new FileReader(resolvedPath)) {
            Type setType = new TypeToken<Set<String>>() {}.getType();
            Set<String> data = GSON.fromJson(reader, setType);
            return data != null ? normalizeIdSet(data) : new HashSet<>();
        } catch (IOException e) {
            return new HashSet<>();
        }
    }

    public static Set<String> loadFavoriteBikes(String filename) {
        String resolvedPath = resolveScriptPath(filename);
        try (FileReader reader = new FileReader(resolvedPath)) {
            Type setType = new TypeToken<Set<String>>() {}.getType();
            Set<String> data = GSON.fromJson(reader, setType);
            return data != null ? normalizeIdSet(data) : new HashSet<>();
        } catch (IOException e) {
            return new HashSet<>();
        }
    }

    public static void saveSeenBikes(Set<String> seenBikes) {
        Set<String> normalized = normalizeIdSet(seenBikes);
        try (FileWriter writer = new FileWriter(resolveScriptPath("seen_bikes.json"))) {
            GSON.toJson(normalized, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addToFavorites(String bikeId, Map<String, Object> item) {
        Set<String> favorites = loadFavoriteBikes("favorite_bikes.json");
        favorites.add(normalizeId(bikeId));
        try (FileWriter writer = new FileWriter(resolveScriptPath("favorite_bikes.json"))) {
            GSON.toJson(favorites, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeFromFavorites(String bikeId) {
        Set<String> favorites = loadFavoriteBikes("favorite_bikes.json");
        favorites.remove(normalizeId(bikeId));
        try (FileWriter writer = new FileWriter(resolveScriptPath("favorite_bikes.json"))) {
            GSON.toJson(favorites, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearJsonFiles() {
        writeFile(resolveScriptPath("motorcycles.json"), "[]");
        writeFile(resolveScriptPath("seen_bikes.json"), "[]");
    }

    public static void clearFavorites() {
        writeFile(resolveScriptPath("favorite_bikes.json"), "[]");
    }

    private static void writeFile(String path, String content) {
        try {
            Path p = Path.of(path);
            Files.createDirectories(p.getParent());
            try (FileWriter writer = new FileWriter(path)) {
                writer.write(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String normalizeUrl(String rawUrl) {
        if (rawUrl == null) return "";
        String s = rawUrl.trim();
        if (s.startsWith("//")) return "https:" + s;
        return s;
    }

    public static String bikeIdFromItem(Map<String, Object> item) {
        if (item == null) return "";
        Object urlObj = item.get("url");
        String url = normalizeUrl(urlObj == null ? "" : String.valueOf(urlObj));
        return bikeIdFromUrl(url);
    }

    public static String bikeIdFromUrl(String url) {
        String u = normalizeUrl(url);
        if (u.isEmpty()) return "";
        String[] parts = u.split("/");
        String last = parts[parts.length - 1];
        // last can be "...-2074320.html" -> strip extension
        int dot = last.indexOf('.');
        return (dot >= 0) ? last.substring(0, dot) : last;
    }

    private static Set<String> normalizeIdSet(Set<String> ids) {
        Set<String> out = new HashSet<>();
        if (ids == null) return out;
        for (String id : ids) {
            if (id == null) continue;
            String s = id.trim();
            if (!s.isEmpty()) out.add(s);
        }
        return out;
    }

    private static String normalizeId(String id) {
        if (id == null) return "";
        return id.trim();
    }
}
