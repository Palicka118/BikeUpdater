package com.pawel.ui;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.stream.Collectors;

import com.pawel.infrastructure.DataUtils;

public class BikeSearchAppController {

    @FXML
    private VBox mainContainer;

    private Set<String> seenBikes = new HashSet<>();

    @FXML
    public void loadAndDisplayMotorcycles() {
        refreshData();
        loadAndDisplayMotorcycles(mainContainer);
    }

    private void refreshData() {
        runPythonScript();
    }

    private void runPythonScript() {
        DataUtils.runPythonScript();
    }

    public void loadAndDisplayMotorcycles(VBox mainContainer) {
        boolean hasAnyDisplayed = false;

        List<Map<String, Object>> jsonData = DataUtils.loadMotorcycleData("motorcycles.json");
        seenBikes = DataUtils.loadSeenBikes("seen_bikes.json");
        Set<String> favoriteBikes = DataUtils.loadFavoriteBikes("favorite_bikes.json");

        if (jsonData == null || jsonData.isEmpty()) {
            displayNoNewBikesMessage(mainContainer);
            return;
        }

        // Normalize + group by make (stable, sorted)
        Map<String, List<Map<String, Object>>> groupedData =
                jsonData.stream()
                        .filter(Objects::nonNull)
                        .filter(item -> item.get("make") != null)
                        .collect(Collectors.groupingBy(
                                item -> normalizeMake(String.valueOf(item.get("make"))),
                                TreeMap::new,
                                Collectors.toList()
                        ));

        mainContainer.getChildren().clear();

        for (Map.Entry<String, List<Map<String, Object>>> entry : groupedData.entrySet()) {
            String make = entry.getKey();
            List<Map<String, Object>> items = entry.getValue();

            VBox makeBox = UIComponents.createMakeBox(make);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            int row = 0;
            int col = 0;

            for (Map<String, Object> item : items) {
                String bikeId = DataUtils.bikeIdFromItem(item);

                // show if unseen OR favorited (favorited always shows)
                if (!seenBikes.contains(bikeId) || favoriteBikes.contains(bikeId)) {
                    VBox itemBox = UIComponents.createItemBox(item);
                    gridPane.add(itemBox, col, row);
                    hasAnyDisplayed = true;

                    // mark seen once it's displayed
                    seenBikes.add(bikeId);

                    col++;
                    if (col == 5) {
                        col = 0;
                        row++;
                    }
                }
            }

            // only add make section if it contains something
            if (!gridPane.getChildren().isEmpty()) {
                makeBox.getChildren().add(gridPane);
                mainContainer.getChildren().add(makeBox);
            }
        }

        if (!hasAnyDisplayed) {
            displayNoNewBikesMessage(mainContainer);
        }

        DataUtils.saveSeenBikes(seenBikes);
    }

    private void displayNoNewBikesMessage(VBox mainContainer) {
        mainContainer.getChildren().clear();
        Label noNewBikesLabel = new Label("No new bikes");
        noNewBikesLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #fff;");
        noNewBikesLabel.setAlignment(Pos.CENTER);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.getChildren().add(noNewBikesLabel);
    }

    private static String normalizeMake(String make) {
        if (make == null) return "Unknown";
        String s = make.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
        if (s.isEmpty()) return "Unknown";
        return s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1);
    }

    @FXML
    public void clearJsonFiles() {
        DataUtils.clearJsonFiles();
        seenBikes.clear();
        mainContainer.getChildren().clear();
    }

    @FXML
    public void clearFavorites() {
        DataUtils.clearFavorites();
        mainContainer.getChildren().clear();
    }
}
