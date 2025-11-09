package cincuentazo.view;

import cincuentazo.controller.CincuentazoGameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A singleton Stage for the main Cincuentazo game window.
 * This class ensures that only one instance of the game window can exist.
 */
public class CincuentazoGameStage extends Stage {
    private CincuentazoGameController controller;

    /**
     * Private constructor to enforce the singleton pattern. It loads the FXML view,
     * sets up the scene, and configures the stage properties.
     *
     * @throws IOException if the FXML file cannot be loaded.
     */
    private CincuentazoGameStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/miniproyecto3/fxml/cincuentazo-game-view.fxml")
        );
        Parent root = loader.load();
        controller = loader.getController();

        Scene scene = new Scene(root);
        setScene(scene);
        setTitle("Cincuentazo - Juego");
        setResizable(false);
        getIcons().add(
                new Image(String.valueOf(getClass().getResource("/com/example/miniproyecto3/images/logo.png")))
        );
        show();
    }

    /**
     * Returns the controller associated with this stage's view.
     *
     * @return The CincuentazoGameController instance.
     */
    public CincuentazoGameController getController() {
        return controller;
    }

    /**
     * Inner static class to hold the singleton instance (lazy initialization).
     */
    private static class Holder {
        private static CincuentazoGameStage INSTANCE = null;
    }

    /**
     * Provides global access to the singleton CincuentazoGameStage instance.
     * Creates the instance if it doesn't exist yet.
     *
     * @return The single instance of CincuentazoGameStage.
     * @throws IOException if the FXML file cannot be loaded during the first creation.
     */
    public static CincuentazoGameStage getInstance() throws IOException {
        CincuentazoGameStage.Holder.INSTANCE = CincuentazoGameStage.Holder.INSTANCE != null ?
                CincuentazoGameStage.Holder.INSTANCE : new CincuentazoGameStage();
        return CincuentazoGameStage.Holder.INSTANCE;
    }

    /**
     * Closes the stage, effectively deleting the instance from view.
     */
    public static void deleteInstance() {
        if (CincuentazoGameStage.Holder.INSTANCE != null) {
            CincuentazoGameStage.Holder.INSTANCE.close();
            Holder.INSTANCE = null;
        }
    }
}