package com.pawel;

import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseButton;
import java.awt.Desktop;
import java.net.URI;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for creating UI components for the Bike Search App.
 */
public class UIComponents {

    private static Set<String> favoriteBikes = DataUtils.loadFavoriteBikes("scripts/favorite_bikes.json");

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
     * Adds a bike to the favorites list and removes it from the seen bikes file.
     *
     * @param bikeId the unique identifier of the bike
     * @param itemBox the VBox containing the motorcycle item details
     * @param item a map containing the motorcycle item details
     */
    private static void addToFavorites(String bikeId, VBox itemBox, Map<String, Object> item) {
        favoriteBikes.add(bikeId);
        itemBox.setStyle("-fx-border-color: yellow; -fx-border-width: 2px;");
        System.out.println("Bike " + bikeId + " added to favorites.");
        DataUtils.addToFavorites(bikeId, item);
    }

    /**
     * Removes a bike from the favorites list.
     *
     * @param bikeId the unique identifier of the bike
     * @param itemBox the VBox containing the motorcycle item details
     */
    private static void removeFromFavorites(String bikeId, VBox itemBox) {
        favoriteBikes.remove(bikeId);
        itemBox.setStyle(null); // Remove the yellow border
        System.out.println("Bike " + bikeId + " removed from favorites.");
        DataUtils.removeFromFavorites(bikeId);
    }

    /**
     * Creates a context menu for a motorcycle item.
     *
     * @param item a map containing the motorcycle item details
     * @param itemBox the VBox containing the motorcycle item details
     * @return a ContextMenu with options for the item
     */
    private static ContextMenu createContextMenu(Map<String, Object> item, VBox itemBox) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem openLink = new MenuItem("Open Link");
        openLink.setOnAction(event -> openUrl((String) item.get("url")));

        MenuItem copyLink = new MenuItem("Copy Link");
        copyLink.setOnAction(event -> {
            String url = (String) item.get("url");
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new java.awt.datatransfer.StringSelection(url), null);
        });

        String bikeId = ((String) item.get("url")).split("/")[((String) item.get("url")).split("/").length - 1].split("\\.")[0];
        MenuItem favorite = new MenuItem(favoriteBikes.contains(bikeId) ? "Unfavorite" : "Favorite");
        favorite.setOnAction(event -> {
            if (favoriteBikes.contains(bikeId)) {
                removeFromFavorites(bikeId, itemBox);
                favorite.setText("Favorite");
            } else {
                addToFavorites(bikeId, itemBox, item);
                favorite.setText("Unfavorite");
            }
        });

        contextMenu.getItems().addAll(openLink, copyLink, favorite);
        return contextMenu;
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

        String bikeId = ((String) item.get("url")).split("/")[((String) item.get("url")).split("/").length - 1].split("\\.")[0];
        if (favoriteBikes.contains(bikeId)) {
            itemBox.setStyle("-fx-border-color: yellow; -fx-border-width: 2px;");
        }

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
        itemBox.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                openUrl(url);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                ContextMenu contextMenu = createContextMenu(item, itemBox);
                contextMenu.show(itemBox, event.getScreenX(), event.getScreenY());
            }
        });

        return itemBox;
    }
}
