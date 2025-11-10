package cincuentazo.controller;

import cincuentazo.view.CincuentazoWelcomeStage;
import cincuentazo.view.CincuentazoWinnerStage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.event.ActionEvent;

/**
 * Controller class for the Help window in the Cincuentazo application.
 * This class manages user interactions within the Help view,
 * specifically providing functionality to return (close) the Help window
 * when the "Back" button is clicked.
 */
public class CincuentazoWinnerController {

    /**
     * Handles the action event triggered when the user clicks the "Back" button.
     * This method retrieves the current window (Stage) from the event source
     * and closes it, effectively returning the user to the previous screen.
     *
     * @param event the {@link ActionEvent} triggered by the button click.
     */
    @FXML
    private void handleDoor(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void handleHome(ActionEvent event) throws IOException {
        // Cierra la ventana de ganador
        CincuentazoWinnerStage.deleteInstance();

        // Abre la ventana del menú principal
        CincuentazoWelcomeStage.getInstance();
    }

}
