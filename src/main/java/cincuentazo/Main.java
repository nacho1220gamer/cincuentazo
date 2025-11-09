package cincuentazo.view;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main entry point for the Cincuentazo game application.
 * Initializes and displays the welcome/menu screen.
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application.
     * @param primaryStage the primary stage for the application
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            CincuentazoWelcomeStage.getInstance();
        } catch (IOException e) {
            System.err.println("Error loading main view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Main method to launch the application.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
