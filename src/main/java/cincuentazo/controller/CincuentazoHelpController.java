package cincuentazo.controller;

import cincuentazo.view.CincuentazoHelpStage;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

/**
 * Controller class for the Help window in the Cincuentazo application.
 * This class manages user interactions within the Help view,
 * specifically providing functionality to return (close) the Help window
 * when the "Back" button is clicked.
 */
public class CincuentazoHelpController {

    private String callerContext = "welcome"; // Default context

    /**
     * Sets the context from which Help was opened.
     * This allows the controller to know where it came from.
     *
     * @param context "game" or "welcome"
     */
    public void setCallerContext(String context) {
        this.callerContext = context;
    }

    /**
     * Handles the action event triggered when the user clicks the "Back" button.
     * This method closes the Help window using the Singleton pattern,
     * returning the user to the previous screen (Game or Welcome).
     *
     * @param event the {@link ActionEvent} triggered by the button click.
     */
    @FXML
    private void handleBack(ActionEvent event) {
        CincuentazoHelpStage.deleteInstance();
        // The parent window (Game or Welcome) remains visible behind the modal
    }
}
