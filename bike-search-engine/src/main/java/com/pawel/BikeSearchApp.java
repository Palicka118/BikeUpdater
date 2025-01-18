package com.pawel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BikeSearchApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pawel/BikeSearchApp.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1020, 820); // Adjust scene size for padding
            primaryStage.setTitle("Bike Search App");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false); // Make the window not resizable
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}