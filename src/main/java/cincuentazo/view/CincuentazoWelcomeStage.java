package cincuentazo.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A singleton Stage for the welcome/menu window of Cincuentazo game.
 * This class ensures that only one instance of the welcome window can exist.
 */
public class CincuentazoWelcomeStage extends Stage {

    /**
     * Private constructor to enforce the singleton pattern.
     * Loads the FXML view, sets up the scene, and configures stage properties.
     *
     * @throws IOException if the FXML file cannot be loaded.
     */
    private CincuentazoWelcomeStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/miniproyecto3/fxml/cincuentazo-menu-view.fxml")
        );
        Parent root = loader.load();

        Scene scene = new Scene(root);
        setScene(scene);
        setTitle("Cincuentazo - 50zo");
        setResizable(false);
        show();
    }

    /**
     * Inner static class to hold the singleton instance (lazy initialization).
     */
    private static class Holder {
        private static CincuentazoWelcomeStage INSTANCE = null;
    }

    /**
     * Provides global access to the singleton CincuentazoWelcomeStage instance.
     * Creates the instance if it doesn't exist yet.
     *
     * @return The single instance of CincuentazoWelcomeStage.
     * @throws IOException if the FXML file cannot be loaded during first creation.
     */
    public static CincuentazoWelcomeStage getInstance() throws IOException {
        Holder.INSTANCE = Holder.INSTANCE != null ?
                Holder.INSTANCE : new CincuentazoWelcomeStage();
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