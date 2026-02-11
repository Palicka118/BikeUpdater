// src/main/java/com/pawel/infrastructure/ScraperExecutor.java
package com.pawel.infrastructure;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ScraperExecutor {

    private final AppPaths paths;

    public ScraperExecutor(AppPaths paths) {
        this.paths = paths;
    }

    public int runScraper() {
        Path script = paths.scraperMainPy();

        ProcessBuilder pb = new ProcessBuilder(List.of(
                "python",
                script.toString()
        ));

        // run from project root (parent of scripts dir)
        pb.directory(paths.scriptsDir().getParent().toFile());
        pb.inheritIO();

        try {
            Process p = pb.start();
            return p.waitFor();
        } catch (IOException e) {
            throw new RuntimeException("Failed to start scraper: " + script, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Scraper interrupted", e);
        }
    }
}
