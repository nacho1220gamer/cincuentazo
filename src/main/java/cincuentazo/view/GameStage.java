package cincuentazo.view;

import cincuentazo.controller.CincuentazoGameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * View class for the main game screen.
 * Handles the game board, cards, and player interactions.
 */
public class GameStage extends Stage {

    private CincuentazoGameController controller;

    /**
     * Constructor that initializes the game screen.
     * @param numCPUs number of CPU players (1-3)
     */
    public GameStage(int numCPUs) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cincuentazo/cincuentazo-game-view.fxml"));
            Parent root = loader.load();

            controller = loader.getController();
            controller.setStage(this);
            controller.initializeGame(numCPUs);

            Scene scene = new Scene(root);
            this.setScene(scene);
            this.setTitle("Cincuentazo - Playing against " + numCPUs + " CPU(s)");
            this.setResizable(false);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load game view", e);
        }
    }

    /**
     * Gets the controller for this stage.
     * @return the game controller
     */
    public CincuentazoGameController getController() {
        return controller;
    }

    /**
     * Stops the game thread when closing.
     */
    @Override
    public void close() {
        if (controller != null) {
            controller.stopGame();
        }
        super.close();
    }
}