package cincuentazo.controller;

import cincuentazo.view.GameStage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the welcome/menu screen.
 * Handles player selection and navigation to game or help screens.
 */
public class CincuentazoWelcomeController {

    @FXML
    private Pane seleccionnumerojugadores;

    private Stage stage;

    /**
     * Sets the stage for this controller.
     * @param stage the main application stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Handles the "Play" button click.
     * Shows the player selection panel.
     */
    @FXML
    private void handlePlay() {
        seleccionnumerojugadores.setVisible(true);
    }

    /**
     * Handles the "Help" button click.
     * Opens the help screen.
     */
    @FXML
    private void handleHelp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cincuentazo/cincuentazo-help-view.fxml"));
            Parent root = loader.load();

            CincuentazoHelpController helpController = loader.getController();
            helpController.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Exit" button click.
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        stage.close();
    }

    /**
     * Handles selection of 1 CPU player.
     */
    @FXML
    private void handleOnePlayer() {
        startGame(1);
    }

    /**
     * Handles selection of 2 CPU players.
     */
    @FXML
    private void handleTwoPlayers() {
        startGame(2);
    }

    /**
     * Handles selection of 3 CPU players.
     */
    @FXML
    private void handleThreePlayers() {
        startGame(3);
    }

    /**
     * Handles the "Back" button from player selection.
     * Hides the player selection panel.
     */
    @FXML
    private void handleBack() {
        seleccionnumerojugadores.setVisible(false);
    }

    /**
     * Starts the game with the specified number of CPU players.
     * @param numCPUs number of CPU players (1-3)
     */
    private void startGame(int numCPUs) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cincuentazo/cincuentazo-game-view.fxml"));
            Parent root = loader.load();

            CincuentazoGameController gameController = loader.getController();
            gameController.setStage(stage);
            gameController.initializeGame(numCPUs);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Cincuentazo - Playing against " + numCPUs + " CPU(s)");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}