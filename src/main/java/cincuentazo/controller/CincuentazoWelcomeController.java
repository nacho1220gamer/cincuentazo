package cincuentazo.controller;

import cincuentazo.view.CincuentazoGameStage;
import cincuentazo.view.CincuentazoHelpStage;
import cincuentazo.view.CincuentazoWelcomeStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.application.Platform;

import java.io.IOException;

/**
 * Controller for the welcome/menu view of Cincuentazo game.
 * Handles navigation between different views and game initialization.
 */
public class CincuentazoWelcomeController {

    @FXML
    private Pane playerQuantitySelector;

    private int selectedPlayers = 0;

    /**
     * Initializes the controller.
     * Called automatically after FXML loading.
     */
    @FXML
    public void initialize() {
        // Ensure player selector is hidden initially
        if (playerQuantitySelector != null) {
            playerQuantitySelector.setVisible(false);
        }
    }

    /**
     * Shows the player quantity selector panel.
     * Triggered when the "Play" button is clicked.
     *
     * @param event the action event
     */
    @FXML
    private void handlePlay(ActionEvent event) {
        if (playerQuantitySelector != null) {
            playerQuantitySelector.setVisible(true);
        }
    }

    /**
     * Starts a game with 1 machine player.
     *
     * @param event the action event
     */
    @FXML
    private void handleOnePlayer(ActionEvent event) {
        selectedPlayers = 1;
        startGame();
    }

    /**
     * Starts a game with 2 machine players.
     *
     * @param event the action event
     */
    @FXML
    private void handleTwoPlayers(ActionEvent event) {
        selectedPlayers = 2;
        startGame();
    }

    /**
     * Starts a game with 3 machine players.
     *
     * @param event the action event
     */
    @FXML
    private void handleThreePlayers(ActionEvent event) {
        selectedPlayers = 3;
        startGame();
    }

    /**
     * Initializes and displays the game stage.
     * Closes the welcome stage.
     */
    private void startGame() {
        try {
            // First, get the game stage instance
            CincuentazoGameStage gameStage = CincuentazoGameStage.getInstance();

            // Then, initialize the game with the selected number of players
            gameStage.getController().initializeGame(selectedPlayers);

            // Finally, close the welcome stage
            CincuentazoWelcomeStage.deleteInstance();

        } catch (IOException e) {
            System.err.println("Error starting game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Hides the player quantity selector panel.
     * Returns to the main menu view.
     *
     * @param event the action event
     */
    @FXML
    private void handleBack(ActionEvent event) {
        if (playerQuantitySelector != null) {
            playerQuantitySelector.setVisible(false);
        }
    }

    /**
     * Opens the help/instructions window.
     *
     * @param event the action event
     */
    @FXML
    private void handleHelp(ActionEvent event) {
        try {
            CincuentazoHelpStage.getInstance("welcome");
        } catch (IOException e) {
            System.err.println("Error opening help window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Exits the application.
     *
     * @param event the action event
     */
    @FXML
    private void handleExit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
}