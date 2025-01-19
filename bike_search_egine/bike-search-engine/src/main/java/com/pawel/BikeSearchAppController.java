package com.pawel;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javafx.geometry.Pos;

/**
 * Controller class for the Bike Search App.
 * Handles the user interface interactions and data loading/displaying logic.
 */
public class BikeSearchAppController {

    @FXML
    private TextArea textArea;

    @FXML
    private VBox mainContainer; // Add this line to reference the main container

    @SuppressWarnings("unused")
    private Set<String> seenBikes;

    /**
     * Loads and displays motorcycles when the "Load Motorcycles" button is clicked.
     */
    @FXML
    public void loadAndDisplayMotorcycles() {
        System.out.println("Loading and displaying motorcycles...");
        refreshData();
        loadAndDisplayMotorcycles(mainContainer);
    }

    /**
     * Refreshes the data by creating the data directory and running the Python script.
     */
    private void refreshData() {
        System.out.println("Refreshing data...");
        runPythonScript();
    }
    /**
     * Runs the Python script to fetch motorcycle data.
     */
    private void runPythonScript() {
        System.out.println("Running Python script...");
        try {
            ProcessBuilder pb = new ProcessBuilder("python", "bike_search_egine/bike-search-engine/scripts/main.py");
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads and displays motorcycles in the main container.
     *
     * @param mainContainer the main container to display the motorcycles
     */
    public void loadAndDisplayMotorcycles(VBox mainContainer) {
        boolean hasNewBikes = false;

        System.out.println("Loading motorcycle data from JSON...");
        List<Map<String, Object>> jsonData = DataUtils.loadMotorcycleData("bike_search_egine/bike-search-engine/scripts/motorcycles.json");
        seenBikes = DataUtils.loadSeenBikes("bike_search_egine/bike-search-engine/scripts/seen_bikes.json");

        if (jsonData == null || jsonData.isEmpty()) {
            System.out.println("No data loaded from JSON file.");
            System.out.println("hasNewBikes: " + hasNewBikes);
            if (!hasNewBikes) {
                System.out.println("No new bikes found.");
                mainContainer.getChildren().clear(); // Clear previous content
                Label noNewBikesLabel = new Label("No new bikes");
                noNewBikesLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #fff;");
                noNewBikesLabel.setAlignment(Pos.CENTER);
                mainContainer.setAlignment(Pos.CENTER);
                mainContainer.getChildren().add(noNewBikesLabel);
            }
            return;
        }
        System.out.println("New bikes were found.");

        mainContainer.getChildren().clear(); // Clear previous content

        Map<String, List<Map<String, Object>>> groupedData = jsonData.stream()
            .filter(item -> item.get("make") != null) // Filter out items without a "make" key
            .collect(Collectors.groupingBy(item -> (String) item.get("make")));

        if (groupedData.isEmpty()) {
            System.out.println("No data grouped by make.");
            return;
        }
        for (String make : groupedData.keySet()) {
            System.out.println("Processing make: " + make);
            VBox makeBox = UIComponents.createMakeBox(make);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            int row = 0;
            int col = 0;

            for (Map<String, Object> item : groupedData.get(make)) {
                VBox itemBox = UIComponents.createItemBox(item);
                gridPane.add(itemBox, col, row);
                hasNewBikes = true;

                col++;
                if (col == 5) {
                    col = 0;
                    row++;
                }
            }

            makeBox.getChildren().add(gridPane);
            mainContainer.getChildren().add(makeBox);
        }
    }

    /**
     * Clears the JSON files when the "Clear memory" button is clicked.
     */
    @FXML
    public void clearJsonFiles() {
        System.out.println("Clearing JSON files...");
        DataUtils.clearJsonFiles();
        System.out.println("JSON files cleared.");
    }
}