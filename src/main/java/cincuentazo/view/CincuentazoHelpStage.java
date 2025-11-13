package cincuentazo.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import cincuentazo.controller.CincuentazoHelpController;

import java.io.IOException;

/**
 * A singleton Stage for the help/instructions window of Cincuentazo game.
 * This class ensures that only one instance of the help window can exist.
 */
public class CincuentazoHelpStage extends Stage {

    private CincuentazoHelpController controller;

    /**
     * Private constructor to enforce the singleton pattern.
     *
     * @param callerContext the context from which Help was opened ("game" or "welcome")
     * @throws IOException if the FXML file cannot be loaded.
     */
    private CincuentazoHelpStage(String callerContext) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/miniproyecto3/fxml/cincuentazo-help-view.fxml")
        );
        Parent root = loader.load();
        controller = loader.getController();

        // Pass context to controller
        controller.setCallerContext(callerContext);

        Scene scene = new Scene(root);
        setScene(scene);
        setTitle("Cincuentazo - Help");
        setResizable(false);

        // Make it modal (blocks interaction with parent window)
        initModality(Modality.APPLICATION_MODAL);

        // Handle window close event
        setOnCloseRequest(event -> {
            Holder.INSTANCE = null;
        });

        show();
    }

    /**
     * Inner static class to hold the singleton instance (lazy initialization).
     */
    private static class Holder {
        private static CincuentazoHelpStage INSTANCE = null;
    }

    /**
     * Provides access to the singleton instance.
     *
     * @param callerContext where Help was opened from ("game" or "welcome")
     * @return The single instance of CincuentazoHelpStage.
     * @throws IOException if the FXML file cannot be loaded.
     */
    public static CincuentazoHelpStage getInstance(String callerContext) throws IOException {
        if (Holder.INSTANCE == null) {
            Holder.INSTANCE = new CincuentazoHelpStage(callerContext);
        }
        return Holder.INSTANCE;
    }

    /**
     * Convenience method for backwards compatibility.
     */
    public static CincuentazoHelpStage getInstance() throws IOException {
        return getInstance("welcome"); // Default to welcome
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