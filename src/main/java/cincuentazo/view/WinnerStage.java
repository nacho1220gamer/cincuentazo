/*package cincuentazo.view;

//import cincuentazo.controller.CincuentazoWinnerController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * View class for the winner/game over screen.
 * Displays game results and allows navigation to menu or replay.

public class WinnerStage extends Stage {

    private CincuentazoWinnerController controller;

    /**
     * Constructor that initializes the winner screen.
     * @param winnerName name of the winning player
     * @param isHumanWinner true if human player won
     * @param totalTurns total number of turns played
     * @param eliminatedPlayers list of eliminated players in order

    public WinnerStage(String winnerName, boolean isHumanWinner, int totalTurns, String eliminatedPlayers) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cincuentazo/cincuentazo-winner-view.fxml"));
            Parent root = loader.load();

            controller = loader.getController();
            controller.setStage(this);
            controller.setWinnerData(winnerName, isHumanWinner, totalTurns, eliminatedPlayers);

            Scene scene = new Scene(root);
            this.setScene(scene);
            this.setTitle("Cincuentazo - Game Over");
            this.setResizable(false);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load winner view", e);
        }
    }

    /**
     * Gets the controller for this stage.
     * @return the winner controller

    public CincuentazoWinnerController getController() {
        return controller;
    }
}*/