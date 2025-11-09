package cincuentazo.view;

import cincuentazo.controller.CincuentazoFinalController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Represents the final stage (window) of the Cincuentazo application.
 * <p>
 * This class loads and displays the "Final" screen defined in the FXML file.
 * It implements the Singleton design pattern to ensure that only one instance
 * of this stage is created at a time.
 */
public class CincuentazoFinalStage extends Stage {
    private CincuentazoFinalController controller;

    /**
     * Private constructor that initializes the final stage by loading the FXML layout,
     * setting up the scene, window title, icon, and other properties.
     *
     * @throws IOException if the FXML file cannot be loaded.
     */
    private CincuentazoFinalStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/miniproyecto3/fxml/cincuentazo-final-view.fxml")
        );
        Parent root = loader.load();
        controller = loader.getController();

        Scene scene = new Scene(root);
        setScene(scene);
        setTitle("Cincuentazo - Fin del Juego");
        setResizable(false);
        getIcons().add(
                new Image(String.valueOf(getClass().getResource("/com/example/miniproyecto3/images/logo.png")))
        );
        show();
    }

    /**
     * Returns the controller associated with this stage.
     *
     * @return the {@link CincuentazoFinalController} managing this view.
     */
    public CincuentazoFinalController getController() {
        return controller;
    }

    /**
     * Holder class for the Singleton instance.
     * This pattern ensures lazy initialization and thread safety.
     */
    private static class Holder {
        private static CincuentazoFinalStage INSTANCE = null;
    }

    /**
     * Returns the single instance of {@code CincuentazoFinalStage}.
     * If no instance exists, a new one will be created.
     *
     * @return the single instance of {@code CincuentazoFinalStage}.
     * @throws IOException if the FXML file cannot be loaded when creating the instance.
     */
    public static CincuentazoFinalStage getInstance() throws IOException {
        CincuentazoFinalStage.Holder.INSTANCE = CincuentazoFinalStage.Holder.INSTANCE != null ?
                CincuentazoFinalStage.Holder.INSTANCE : new CincuentazoFinalStage();
        return CincuentazoFinalStage.Holder.INSTANCE;
    }

    /**
     * Deletes the current instance of the final stage by closing it
     * and setting the Singleton reference to {@code null}.
     * <p>
     * This allows the stage to be re-created later if needed.
     */
    public static void deleteInstance() {
        if (CincuentazoFinalStage.Holder.INSTANCE != null) {
            CincuentazoFinalStage.Holder.INSTANCE.close();
            CincuentazoFinalStage.Holder.INSTANCE = null;
        }
    }
}