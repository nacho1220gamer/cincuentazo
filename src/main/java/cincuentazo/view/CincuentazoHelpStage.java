package cincuentazo.view;

import cincuentazo.controller.CincuentazoHelpController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Represents the help stage (window) of the Cincuentazo application.
 * <p>
 * This class loads and displays the "Help" screen defined in the FXML file.
 * It implements the Singleton design pattern to ensure that only one instance
 * of this stage is created at a time.
 */
public class CincuentazoHelpStage extends Stage {
    private CincuentazoHelpController controller;

    /**
     * Private constructor that initializes the help stage by loading the FXML layout,
     * setting up the scene, window title, icon, and other properties.
     *
     * @throws IOException if the FXML file cannot be loaded.
     */
    private CincuentazoHelpStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/miniproyecto3/fxml/cincuentazo-help-view.fxml")
        );
        Parent root = loader.load();
        controller = loader.getController();

        Scene scene = new Scene(root);
        setScene(scene);
        setTitle("Cincuentazo - Ayuda");
        setResizable(false);
        getIcons().add(
                new Image(String.valueOf(getClass().getResource("/com/example/miniproyecto3/images/logo.png")))
        );
        show();
    }

    /**
     * Returns the controller associated with this stage.
     *
     * @return the {@link CincuentazoHelpController} managing this view.
     */
    public CincuentazoHelpController getController() {
        return controller;
    }

    /**
     * Holder class for the Singleton instance.
     * This pattern ensures lazy initialization and thread safety.
     */
    private static class Holder {
        private static CincuentazoHelpStage INSTANCE = null;
    }

    /**
     * Returns the single instance of {@code CincuentazoHelpStage}.
     * If no instance exists, a new one will be created.
     *
     * @return the single instance of {@code CincuentazoHelpStage}.
     * @throws IOException if the FXML file cannot be loaded when creating the instance.
     */
    public static CincuentazoHelpStage getInstance() throws IOException {
        CincuentazoHelpStage.Holder.INSTANCE = CincuentazoHelpStage.Holder.INSTANCE != null ?
                CincuentazoHelpStage.Holder.INSTANCE : new CincuentazoHelpStage();
        return CincuentazoHelpStage.Holder.INSTANCE;
    }

    /**
     * Deletes the current instance of the help stage by closing it
     * and setting the Singleton reference to {@code null}.
     * <p>
     * This allows the stage to be re-created later if needed.
     */
    public static void deleteInstance() {
        if (CincuentazoHelpStage.Holder.INSTANCE != null) {
            CincuentazoHelpStage.Holder.INSTANCE.close();
            CincuentazoHelpStage.Holder.INSTANCE = null;
        }
    }
}