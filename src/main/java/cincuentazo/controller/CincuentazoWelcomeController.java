package cincuentazo.controller;

import cincuentazo.view.CincuentazoGameStage;
import cincuentazo.view.CincuentazoHelpStage;
import cincuentazo.view.CincuentazoWelcomeStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller class for the Cincuentazo welcome screen.
 * Handles user interaction for selecting number of players,
 * starting the game, and accessing the help view.
 */
public class CincuentazoWelcomeController {

    /**
     * Pane that contains the player selection options.
     * This Pane is hidden by default and becomes visible when "Play" is pressed.
     */
    @FXML
    private Pane seleccionnumerojugadores;

    /**
     * Initializes the controller class.
     * Hides the player selection panel by default.
     */
    @FXML
    public void initialize() {
        if (seleccionnumerojugadores != null) {
            seleccionnumerojugadores.setVisible(false);
        }
    }

    /**
     * Handles the action of pressing the "Play" button.
     * Shows the player selection pane.
     */
    @FXML
    private void handlePlay() {
        if (seleccionnumerojugadores != null) {
            seleccionnumerojugadores.setVisible(true);
        }
    }

    /**
     * Handles the action of pressing the "Back" button.
     * Hides the player selection pane and returns to the main menu.
     */
    @FXML
    private void handleBack() {
        if (seleccionnumerojugadores != null) {
            seleccionnumerojugadores.setVisible(false);
        }
    }

    /**
     * Handles the action of pressing the "Exit" button.
     * Closes the welcome stage and exits the application.
     *
     * @param event the ActionEvent triggered by clicking the exit button.
     */
    @FXML
    void handleExit(ActionEvent event) {
        CincuentazoWelcomeStage.deleteInstance();
        System.exit(0);
    }

    /**
     * Handles the action of pressing the "Help" button.
     * Opens the help screen with game rules.
     *
     * @param event the ActionEvent triggered by clicking the help button.
     */
    @FXML
    void handleHelp(ActionEvent event) {
        try {
            CincuentazoHelpStage.getInstance();
            CincuentazoWelcomeStage.deleteInstance();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open help screen.");
        }
    }

    /**
     * Handles the action of selecting one player.
     */
    @FXML
    private void handleOnePlayer() {
        startGameWithPlayers(1);
    }

    /**
     * Handles the action of selecting two players.
     */
    @FXML
    private void handleTwoPlayers() {
        startGameWithPlayers(2);
    }

    /**
     * Handles the action of selecting three players.
     */
    @FXML
    private void handleThreePlayers() {
        startGameWithPlayers(3);
    }

    /**
     * Starts the game with the specified number of players.
     *
     * @param numPlayers the number of players selected.
     */
    private void startGameWithPlayers(int numPlayers) {
        try {
            System.out.println("Starting game with " + numPlayers + " players...");
            CincuentazoGameStage gameStage = CincuentazoGameStage.getInstance();
            gameStage.getController().initializeGame(numPlayers);
            CincuentazoWelcomeStage.deleteInstance();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to start the game.");
        }
    }

    /**
     * Displays an alert dialog with the specified title and message.
     *
     * @param title   the title of the alert dialog.
     * @param message the message to display in the alert.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
