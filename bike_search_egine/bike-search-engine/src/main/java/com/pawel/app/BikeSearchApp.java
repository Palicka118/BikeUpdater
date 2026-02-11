package com.pawel.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The BikeSearchApp class extends the Application class and serves as the entry point for the Bike Search App.
 * It initializes and displays the primary stage with the specified FXML layout.
 *
 * <p>This class performs the following tasks:</p>
 * <ul>
 *   <li>Loads the FXML file for the user interface layout.</li>
 *   <li>Sets the scene with the specified dimensions (1200x900).</li>
 *   <li>Sets the title of the primary stage to "Bike Search App".</li>
 *   <li>Makes the window non-resizable.</li>
 *   <li>Displays the primary stage.</li>
 * </ul>
 *
 * <p>If an exception occurs during the loading of the FXML file, it is caught and the stack trace is printed.</p>
 *
 * @see javafx.application.Application
 * @see javafx.stage.Stage
 * @see javafx.fxml.FXMLLoader
 * @see javafx.scene.Parent
 * @see javafx.scene.Scene
 */
public class BikeSearchApp extends Application {

    /**
     * The main method to launch the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pawel/BikeSearchApp.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1200, 900); // Adjust scene size for padding
            primaryStage.setTitle("Bike Search App");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false); // Make the window not resizable
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}