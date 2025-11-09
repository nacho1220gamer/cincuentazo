package cincuentazo.view;

import cincuentazo.controller.CincuentazoHelpController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * View class for the help/instructions screen.
 * Displays game rules and how to play.
 */
public class HelpStage extends Stage {

    private CincuentazoHelpController controller;

    /**
     * Constructor that initializes the help screen.
     */
    public HelpStage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cincuentazo/cincuentazo-help-view.fxml"));
            Parent root = loader.load();

            controller = loader.getController();
            controller.setStage(this);

            Scene scene = new Scene(root);
            this.setScene(scene);
            this.setTitle("Cincuentazo - How to Play");
            this.setResizable(false);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load help view", e);
        }
    }

    /**
     * Gets the controller for this stage.
     * @return the help controller
     */
    public CincuentazoHelpController getController() {
        return controller;
    }
}