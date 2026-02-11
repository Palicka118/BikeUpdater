// src/main/java/com/pawel/UIComponents.java
package com.pawel.ui;

import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.net.URI;
import java.util.Map;
import java.util.Set;

import com.pawel.infrastructure.DataUtils;

public class UIComponents {

    public static VBox createMakeBox(String make) {
        VBox makeBox = new VBox();
        makeBox.setSpacing(10);
        makeBox.setStyle("-fx-background-color: #444; -fx-padding: 10; -fx-border-color: #555; -fx-border-radius: 5px;");

        Label makeLabel = new Label(make);
        makeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #fff;");
        makeBox.getChildren().add(makeLabel);

        return makeBox;
    }

    private static void openUrl(String url) {
        try {
            String normalized = DataUtils.normalizeUrl(url);
            if (normalized.isEmpty()) return;
            Desktop.getDesktop().browse(new URI(normalized));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ContextMenu createContextMenu(Map<String, Object> item, VBox itemBox) {
        ContextMenu contextMenu = new ContextMenu();

        String urlRaw = item.get("url") == null ? "" : String.valueOf(item.get("url"));
        String url = DataUtils.normalizeUrl(urlRaw);
        String bikeId = DataUtils.bikeIdFromUrl(url);

        Set<String> favorites = DataUtils.loadFavoriteBikes("favorite_bikes.json");

        MenuItem openLink = new MenuItem("Open Link");
        openLink.setOnAction(event -> openUrl(url));

        MenuItem copyLink = new MenuItem("Copy Link");
        copyLink.setOnAction(event -> Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(url), null));

        MenuItem favorite = new MenuItem(favorites.contains(bikeId) ? "Unfavorite" : "Favorite");
        favorite.setOnAction(event -> {
            if (favorites.contains(bikeId)) {
                DataUtils.removeFromFavorites(bikeId);
                itemBox.setStyle(null);
                favorite.setText("Favorite");
            } else {
                DataUtils.addToFavorites(bikeId, item);
                itemBox.setStyle("-fx-border-color: yellow; -fx-border-width: 2px;");
                favorite.setText("Unfavorite");
            }
        });

        contextMenu.getItems().addAll(openLink, copyLink, favorite);
        return contextMenu;
    }

    public static VBox createItemBox(Map<String, Object> item) {
        VBox itemBox = new VBox();
        itemBox.setSpacing(10);
        itemBox.setPrefSize(180, 300);
        itemBox.setAlignment(Pos.CENTER);

        String urlRaw = item.get("url") == null ? "" : String.valueOf(item.get("url"));
        String url = DataUtils.normalizeUrl(urlRaw);
        String bikeId = DataUtils.bikeIdFromUrl(url);

        Set<String> favorites = DataUtils.loadFavoriteBikes("favorite_bikes.json");
        if (favorites.contains(bikeId)) {
            itemBox.setStyle("-fx-border-color: yellow; -fx-border-width: 2px;");
        }

        String imageUrlRaw = item.get("image_url") == null ? "" : String.valueOf(item.get("image_url"));
        String imageUrl = DataUtils.normalizeUrl(imageUrlRaw);
        if (!imageUrl.isEmpty()) {
            ImageView imageView = new ImageView(new Image(imageUrl));
            imageView.setFitWidth(180);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);
            itemBox.getChildren().add(imageView);
        }

        Label nameLabel = new Label(item.get("name") == null ? "" : String.valueOf(item.get("name")));
        nameLabel.setStyle("-fx-text-fill: #fff;");
        itemBox.getChildren().add(nameLabel);

        Label priceLabel = new Label(item.get("price") == null ? "" : String.valueOf(item.get("price")));
        priceLabel.setStyle("-fx-text-fill: #fff;");
        itemBox.getChildren().add(priceLabel);

        Label yearLabel = new Label(item.get("year") == null ? "" : String.valueOf(item.get("year")));
        yearLabel.setStyle("-fx-text-fill: #fff;");
        itemBox.getChildren().add(yearLabel);

        Label mileageLabel = new Label(item.get("mileage") == null ? "" : String.valueOf(item.get("mileage")));
        mileageLabel.setStyle("-fx-text-fill: #fff;");
        itemBox.getChildren().add(mileageLabel);

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
