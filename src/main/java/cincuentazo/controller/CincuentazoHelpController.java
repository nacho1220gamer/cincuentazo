package cincuentazo.controller;

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

    /**
     * Handles the action event triggered when the user clicks the "Back" button.
     * This method retrieves the current window (Stage) from the event source
     * and closes it, effectively returning the user to the previous screen.
     *
     * @param event the {@link ActionEvent} triggered by the button click.
     */
    @FXML
    private void handleBack(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
