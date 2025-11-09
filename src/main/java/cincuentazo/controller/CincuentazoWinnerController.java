/*package cincuentazo.controller;

import cincuentazo.view.CincuentazoGameStage;
import cincuentazo.view.CincuentazoWelcomeStage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Controller for the winner/game over screen.
 * Displays game results and handles navigation to menu or replay.

public class CincuentazoWinnerController {

    @FXML private Label lblWinnerName;
    @FXML private Label lblWinnerTitle;
    @FXML private Label lblTotalTurns;
    @FXML private Label lblEliminatedPlayers;
    @FXML private ImageView imgWinnerAvatar;
    @FXML private ImageView imgTrophy;

    private Stage stage;
    private int lastNumCPUs = 1;

    /**
     * Sets the stage for this controller.
     * @param stage the winner screen stage

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the winner screen with game data.
     * @param winnerName name of the winner
     * @param isHumanWinner true if human won
     * @param totalTurns total turns in the game
     * @param eliminatedPlayers elimination order

    public void setWinnerData(String winnerName, boolean isHumanWinner, int totalTurns, String eliminatedPlayers) {
        if (isHumanWinner) {
            lblWinnerTitle.setText("¡VICTORIA!");
            lblWinnerTitle.setStyle("-fx-text-fill: #FFD700;");
            lblWinnerName.setText("¡Felicitaciones " + winnerName + "!");
            loadWinnerImage("player1.png");
        } else {
            lblWinnerTitle.setText("DERROTA");
            lblWinnerTitle.setStyle("-fx-text-fill: #FF4444;");
            lblWinnerName.setText("Ganador: " + winnerName);

            // Determine which CPU won
            if (winnerName.contains("CPU-1")) {
                loadWinnerImage("player4.png");
            } else if (winnerName.contains("CPU-2")) {
                loadWinnerImage("player2.png");
            } else if (winnerName.contains("CPU-3")) {
                loadWinnerImage("player3.png");
            }
        }

        lblTotalTurns.setText("Turnos jugados: " + totalTurns);
        lblEliminatedPlayers.setText("Orden de eliminación:\n" + eliminatedPlayers);
    }

    /**
     * Loads the winner's avatar image.
     * @param imageName name of the image file

    private void loadWinnerImage(String imageName) {
        try {
            Image image = new Image(getClass().getResourceAsStream("/cincuentazo/images/" + imageName));
            imgWinnerAvatar.setImage(image);
        } catch (Exception e) {
            System.err.println("Failed to load winner image: " + imageName);
        }
    }

    /**
     * Sets the number of CPUs for replay functionality.
     * @Param numCPUs number of CPU players

    public void setLastNumCPUs(int numCPUs) {
        this.lastNumCPUs = numCPUs;
    }

    /**
     * Handles the "Play Again" button.
     * Starts a new game with the same number of CPUs.

    @FXML
    private void handlePlayAgain() {
        stage.close();
        CincuentazoGameStage gameStage = new CincuentazoGameStage(lastNumCPUs);
        gameStage.show();
    }

    /**
     * Handles the "Main Menu" button.
     * Returns to the welcome screen.

    @FXML
    private void handleMainMenu() {
        stage.close();
        CincuentazoWelcomeStage welcomeStage = CincuentazoWelcomeStage.getInstance();
        welcomeStage.show();
    }

    /**
     * Handles the "Exit" button.
     * Closes the application.

    @FXML
    private void handleExit() {
        stage.close();
        CincuentazoWelcomeStage.deleteInstance();
    }
}*/