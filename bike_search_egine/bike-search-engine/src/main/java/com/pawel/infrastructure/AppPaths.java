package com.pawel.infrastructure;

import java.nio.file.Files;
import java.nio.file.Path;

public class AppPaths {

    private final Path projectRoot;

    public AppPaths(Path projectRoot) {
        this.projectRoot = projectRoot;
    }

    public static AppPaths fromWorkingDir() {
        return new AppPaths(Path.of("").toAbsolutePath().normalize());
    }

    public Path scriptsDir() {
        Path a = projectRoot.resolve("scripts");
        if (Files.isDirectory(a)) return a;

        Path b = projectRoot.resolve("bike-search-engine").resolve("scripts");
        if (Files.isDirectory(b)) return b;

        return a; // fallback
    }

    public Path dataDir() {
        Path a = projectRoot.resolve("data");
        if (Files.isDirectory(a)) return a;

        Path b = projectRoot.resolve("bike-search-engine").resolve("data");
        if (Files.isDirectory(b)) return b;

        return a; // fallback
    }

    public Path bikesJson() {
        return dataDir().resolve("bikes.json");
    }

    public Path seenJson() {
        return dataDir().resolve("seen.json");
    }

    public Path favoritesJson() {
        return dataDir().resolve("favorites.json");
    }

    public Path scraperMainPy() {
        return scriptsDir().resolve("main.py");
    }
}
