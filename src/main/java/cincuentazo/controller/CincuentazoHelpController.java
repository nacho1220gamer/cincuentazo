package cincuentazo.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the help/instructions screen.
 * Displays game rules and allows navigation back to menu.
 */
public class CincuentazoHelpController {

    private Stage stage;

    /**
     * Sets the stage for this controller.
     * @param stage the main application stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Handles the "Back" button click.
     * Returns to the main menu.
     */
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cincuentazo/cincuentazo-menu-view.fxml"));
            Parent root = loader.load();

            CincuentazoWelcomeController welcomeController = loader.getController();
            welcomeController.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Cincuentazo");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}