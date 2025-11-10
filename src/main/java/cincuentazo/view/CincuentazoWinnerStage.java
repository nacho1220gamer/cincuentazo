package cincuentazo.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A singleton Stage for the winner/result window of the Cincuentazo game.
 * This class ensures that only one instance of the winner window can exist.
 */
public class CincuentazoWinnerStage extends Stage {

    /**
     * Private constructor to enforce the singleton pattern.
     * Loads the FXML view, sets up the scene, and configures stage properties.
     *
     * @throws IOException if the FXML file cannot be loaded.
     */
    private CincuentazoWinnerStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/miniproyecto3/fxml/cincuentazo-win-view.fxml")
        );
        Parent root = loader.load();

        Scene scene = new Scene(root);
        setScene(scene);
        setTitle("Cincuentazo - ¡Ganador!");
        setResizable(false);
        show();
    }


    /**
     * Inner static class to hold the singleton instance (lazy initialization).
     */
    private static class Holder {
        private static CincuentazoWinnerStage INSTANCE = null;
    }

    /**
     * Provides global access to the singleton CincuentazoWinnerStage instance.
     * Creates the instance if it doesn't exist yet.
     *
     * @return The single instance of CincuentazoWinnerStage.
     * @throws IOException if the FXML file cannot be loaded during first creation.
     */
    public static CincuentazoWinnerStage getInstance() throws IOException {
        Holder.INSTANCE = Holder.INSTANCE != null ?
                Holder.INSTANCE : new CincuentazoWinnerStage();
        return Holder.INSTANCE;
    }

    /**
     * Closes the stage, effectively deleting the instance from view.
     */
    public static void deleteInstance() {
        if (Holder.INSTANCE != null) {
            Holder.INSTANCE.close();
            Holder.INSTANCE = null;
        }
    }
}
