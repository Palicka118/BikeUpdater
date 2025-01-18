package com.pawel;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import java.awt.Desktop;
import java.net.URI;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BikeSearchAppController {

    @FXML
    private TextArea textArea;

    @FXML
    private VBox mainContainer; // Add this line to reference the main container

    @FXML
    public void loadAndDisplayMotorcycles() {
        refreshData();
        loadAndDisplayMotorcycles(mainContainer);
    }
    
    private void refreshData() {
        copyPythonScript();
        runPythonScript();
    }

    private void copyPythonScript() {
        try {
            String externalScriptPath = "data/bike_search_engine.py";
            File externalScriptFile = new File(externalScriptPath);
            if (!externalScriptFile.exists()) {
                InputStream scriptStream = getClass().getResourceAsStream("/com/pawel/bike_search_engine.py");
                if (scriptStream == null) {
                    throw new RuntimeException("Python script not found in resources");
                }
                Files.createDirectories(Paths.get("data"));
                try (FileOutputStream outputStream = new FileOutputStream(externalScriptPath)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = scriptStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runPythonScript() {
        try {
            ProcessBuilder pb = new ProcessBuilder("python", "data/bike_search_engine.py");
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

    public void loadAndDisplayMotorcycles(VBox mainContainer) {
        List<Map<String, Object>> jsonData = loadMotorcycleData("data/motorcycles.json");

        if (jsonData == null || jsonData.isEmpty()) {
            System.out.println("No data loaded from JSON file.");
            return;
        }

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
            VBox makeBox = new VBox();
            makeBox.setSpacing(10);
            makeBox.setStyle("-fx-background-color: #444; -fx-padding: 10; -fx-border-color: #555; -fx-border-radius: 5px;");

            Label makeLabel = new Label(make);
            makeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #fff;");
            makeBox.getChildren().add(makeLabel);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            int row = 0;
            int col = 0;

            for (Map<String, Object> item : groupedData.get(make)) {
                VBox itemBox = new VBox();
                itemBox.setSpacing(10);
                itemBox.setPrefSize(180, 300); // Uniform size for all listings
                itemBox.setAlignment(Pos.CENTER); // Center the content

                // Create ImageView for the picture
                @SuppressWarnings("unchecked")
                List<String> photos = (List<String>) item.get("photos");
                if (!photos.isEmpty()) {
                    ImageView imageView = new ImageView(new Image(photos.get(0)));
                    imageView.setFitWidth(180);
                    imageView.setFitHeight(150); // Adjust height for labels
                    imageView.setPreserveRatio(true);
                    itemBox.getChildren().add(imageView);
                }

                // Create Label for the name
                Label nameLabel = new Label((String) item.get("name"));
                nameLabel.setStyle("-fx-text-fill: #fff;"); // Set text color to white
                itemBox.getChildren().add(nameLabel);

                // Create Label for the price
                Label priceLabel = new Label((String) item.get("price"));
                priceLabel.setStyle("-fx-text-fill: #fff;"); // Set text color to white
                itemBox.getChildren().add(priceLabel);

                // Create Label for the year
                Label yearLabel = new Label((String) item.get("year"));
                yearLabel.setStyle("-fx-text-fill: #fff;"); // Set text color to white
                itemBox.getChildren().add(yearLabel);

                // Create Label for the mileage
                Label mileageLabel = new Label((String) item.get("mileage"));
                mileageLabel.setStyle("-fx-text-fill: #fff;"); // Set text color to white
                itemBox.getChildren().add(mileageLabel);

                // Add click event to open URL
                String url = (String) item.get("url");
                itemBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            Desktop.getDesktop().browse(new URI(url));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                gridPane.add(itemBox, col, row);

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

    private List<Map<String, Object>> loadMotorcycleData(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
            return new Gson().fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}