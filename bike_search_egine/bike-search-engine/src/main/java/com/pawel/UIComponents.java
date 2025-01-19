package com.pawel;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.awt.Desktop;
import java.net.URI;
import java.util.Map;

/**
 * Utility class for creating UI components for the Bike Search App.
 */
public class UIComponents {

    /**
     * Creates a VBox for a motorcycle make.
     *
     * @param make the make of the motorcycle
     * @return a VBox containing the make label
     */
    public static VBox createMakeBox(String make) {
        VBox makeBox = new VBox();
        makeBox.setSpacing(10);
        makeBox.setStyle("-fx-background-color: #444; -fx-padding: 10; -fx-border-color: #555; -fx-border-radius: 5px;");

        Label makeLabel = new Label(make);
        makeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #fff;");
        makeBox.getChildren().add(makeLabel);

        return makeBox;
    }

    /**
     * Opens a URL in the default web browser.
     *
     * @param url the URL to open
     */
    private static void openUrl(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a VBox for a motorcycle item.
     *
     * @param item a map containing the motorcycle item details
     * @return a VBox containing the item details
     */
    public static VBox createItemBox(Map<String, Object> item) {
        VBox itemBox = new VBox();
        itemBox.setSpacing(10);
        itemBox.setPrefSize(180, 300); // Uniform size for all listings
        itemBox.setAlignment(Pos.CENTER); // Center the content

        // Create ImageView for the picture
        String image_url = (String) item.get("image_url");
        if (image_url != null && !image_url.isEmpty()) {
            // Remove leading "//" from the URL
            if (image_url.startsWith("//")) {
                image_url = "https:" + image_url;
            }
            ImageView imageView = new ImageView(new Image(image_url));
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

        String url = (String) item.get("url");
        itemBox.setOnMouseClicked(_ -> openUrl(url));

        return itemBox;
    }
}
