package cincuentazo;

import cincuentazo.view.WelcomeStage;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main entry point of the Cincuentazo card game application.
 * <p>
 * This class extends {@link javafx.application.Application} and is responsible
 * for launching the JavaFX application and opening the welcome stage.
 * </p>
 */
public class Main extends Application {

    /**
     * The main method launches the JavaFX application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the JavaFX application.
     * <p>
     * Opens the {@link WelcomeStage} when the application launches.
     * </p>
     *
     * @param primaryStage the primary stage for this application
     * @throws IOException if the welcome stage fails to load
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        WelcomeStage.getInstance();
    }
}