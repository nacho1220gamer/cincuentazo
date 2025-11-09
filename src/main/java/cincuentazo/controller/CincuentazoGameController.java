package cincuentazo.controller;

import cincuentazo.view.CincuentazoFinalStage;
import cincuentazo.view.CincuentazoGameStage;
import cincuentazo.view.CincuentazoHelpStage;
import cincuentazo.view.CincuentazoWelcomeStage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Controller class for the main Cincuentazo game view.
 * Manages the game screen, player interactions, machine player turns,
 * and game flow including card playing and player elimination.
 */
public class CincuentazoGameController {

    /**
     * HBox containing the human player's cards.
     */
    @FXML
    private HBox humanPlayerCardsBox;

    /**
     * HBox containing machine player 1's cards (face down).
     */
    @FXML
    private HBox machinePlayer1CardsBox;

    /**
     * HBox containing machine player 2's cards (face down).
     */
    @FXML
    private HBox machinePlayer2CardsBox;

    /**
     * HBox containing machine player 3's cards (face down).
     */
    @FXML
    private HBox machinePlayer3CardsBox;

    /**
     * ImageView displaying the current card on the table.
     */
    @FXML
    private ImageView tableCardImageView;

    /**
     * Label displaying the current sum on the table.
     */
    @FXML
    private Label tableSumLabel;

    /**
     * Label indicating whose turn it is.
     */
    @FXML
    private Label currentTurnLabel;

    /**
     * Interface to the game model (injected or created).
     * This will interact with Card, Deck, Player classes from the Model.
     */
    private IGame game; // Assume this interface is provided by the model team

    /**
     * ExecutorService for managing machine player threads.
     */
    private ExecutorService executorService;

    /**
     * Number of machine players in the current game.
     */
    private int numberOfMachinePlayers;

    /**
     * Initializes the game controller.
     * Sets up the executor service for concurrent machine player actions.
     */
    @FXML
    public void initialize() {
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Initializes a new game with the specified number of machine players.
     * Sets up the initial game state, deals cards, and updates the UI.
     *
     * @param numberOfMachinePlayers the number of machine players (1, 2, or 3).
     */
    public void initializeGame(int numberOfMachinePlayers) {
        this.numberOfMachinePlayers = numberOfMachinePlayers;

        // Hide unused machine player boxes
        machinePlayer2CardsBox.setVisible(numberOfMachinePlayers >= 2);
        machinePlayer3CardsBox.setVisible(numberOfMachinePlayers >= 3);

        // Initialize game model (assuming IGame is provided)
        // game = new GameAdapter(numberOfMachinePlayers);
        // game.startGame();

        // Deal initial cards
        updateHumanPlayerCards();
        updateMachinePlayerCards();
        updateTableCard();
        updateTableSum();
        updateCurrentTurn();
    }

    /**
     * Handles the event when a human player clicks on one of their cards.
     * Validates if the card can be played according to game rules.
     *
     * @param cardIndex the index of the card in the player's hand.
     */
    @FXML
    private void handleHumanCardClick(int cardIndex) {
        try {
            // Attempt to play the card through the model
            // boolean success = game.playCard(cardIndex);

            // if (success) {
            // Update UI
            updateTableCard();
            updateTableSum();

            // Draw a new card
            // game.drawCard();
            updateHumanPlayerCards();

            // Check if game is over
            if (checkGameOver()) {
                return;
            }

            // Start machine players' turns
            executeMachinePlayersTurns();
            // } else {
            //     showAlert("Jugada Inválida", "Esta carta excede el límite de 50.");
            // }

        } catch (Exception e) {
            // Handle custom exceptions from model
            handleGameException(e);
        }
    }

    /**
     * Executes the turns for all active machine players using threads.
     * Each machine player has a delay of 2-4 seconds before playing a card,
     * and 1-2 seconds before drawing a new card.
     */
    private void executeMachinePlayersTurns() {
        executorService.submit(() -> {
            // for (int i = 0; i < numberOfMachinePlayers; i++) {
            //     if (!game.isPlayerActive(i + 1)) continue;

            try {
                // Delay before playing (2-4 seconds)
                Thread.sleep(2000 + (long)(Math.random() * 2000));

                Platform.runLater(() -> {
                    updateCurrentTurn();
                    // game.playMachinePlayerCard(i + 1);
                    updateTableCard();
                    updateTableSum();
                    updateMachinePlayerCards();
                });

                // Delay before drawing (1-2 seconds)
                Thread.sleep(1000 + (long)(Math.random() * 1000));

                Platform.runLater(() -> {
                    // game.drawCardForMachinePlayer(i + 1);
                    updateMachinePlayerCards();
                });

                // Check if game is over
                Platform.runLater(() -> {
                    if (checkGameOver()) {
                        return;
                    }
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Platform.runLater(() -> handleGameException(e));
            }
            // }

            // Return turn to human player
            Platform.runLater(this::updateCurrentTurn);
        });
    }

    /**
     * Updates the display of human player's cards.
     * Shows cards face up with click handlers.
     */
    private void updateHumanPlayerCards() {
        humanPlayerCardsBox.getChildren().clear();

        // Get cards from model
        // List<Card> cards = game.getHumanPlayerCards();

        // for (int i = 0; i < cards.size(); i++) {
        //     ImageView cardView = createCardImageView(cards.get(i), true);
        //     final int cardIndex = i;
        //     cardView.setOnMouseClicked(e -> handleHumanCardClick(cardIndex));
        //     humanPlayerCardsBox.getChildren().add(cardView);
        // }
    }

    /**
     * Updates the display of machine players' cards.
     * Shows cards face down.
     */
    private void updateMachinePlayerCards() {
        // Update each machine player's card display
        updateMachinePlayerBox(machinePlayer1CardsBox, 1);
        if (numberOfMachinePlayers >= 2) {
            updateMachinePlayerBox(machinePlayer2CardsBox, 2);
        }
        if (numberOfMachinePlayers >= 3) {
            updateMachinePlayerBox(machinePlayer3CardsBox, 3);
        }
    }

    /**
     * Updates a specific machine player's card box.
     *
     * @param box the HBox to update.
     * @param playerNumber the machine player number (1, 2, or 3).
     */
    private void updateMachinePlayerBox(HBox box, int playerNumber) {
        box.getChildren().clear();

        // Get number of cards for this player
        // int cardCount = game.getMachinePlayerCardCount(playerNumber);

        // Add face-down card images
        // for (int i = 0; i < cardCount; i++) {
        //     ImageView cardView = createCardImageView(null, false);
        //     box.getChildren().add(cardView);
        // }
    }

    /**
     * Creates an ImageView for a card.
     *
     * @param card the card object (null for face-down cards).
     * @param faceUp true if card should be shown face up, false for face down.
     * @return ImageView representing the card.
     */
    private ImageView createCardImageView(Object card, boolean faceUp) {
        ImageView cardView = new ImageView();
        cardView.setFitWidth(80);
        cardView.setFitHeight(120);
        cardView.setPreserveRatio(true);

        if (faceUp && card != null) {
            // Load card image based on card value
            // String imagePath = "/com/example/miniproyecto3/images/cards/" + card.getImageName();
            // cardView.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        } else {
            // Load card back image
            // cardView.setImage(new Image(getClass().getResourceAsStream("/com/example/miniproyecto3/images/cards/back.png")));
        }

        return cardView;
    }

    /**
     * Updates the card currently displayed on the table.
     */
    private void updateTableCard() {
        // Card currentCard = game.getCurrentTableCard();
        // tableCardImageView.setImage(getCardImage(currentCard));
    }

    /**
     * Updates the sum label with the current table sum.
     */
    private void updateTableSum() {
        // int sum = game.getTableSum();
        // tableSumLabel.setText("Suma: " + sum);
    }

    /**
     * Updates the current turn indicator.
     */
    private void updateCurrentTurn() {
        // String currentPlayer = game.getCurrentPlayerName();
        // currentTurnLabel.setText("Turno: " + currentPlayer);
    }

    /**
     * Checks if the game is over.
     * If only one player remains, declares them the winner and shows final stage.
     *
     * @return true if the game is over, false otherwise.
     */
    private boolean checkGameOver() {
        // if (game.isGameOver()) {
        //     String winner = game.getWinner();
        //     showFinalStage(winner);
        //     return true;
        // }
        return false;
    }

    /**
     * Shows the final stage with the game winner.
     *
     * @param winner the name of the winning player.
     */
    private void showFinalStage(String winner) {
        try {
            executorService.shutdown();
            CincuentazoFinalStage finalStage = CincuentazoFinalStage.getInstance();
            finalStage.getController().setWinner(winner);
            CincuentazoGameStage.deleteInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
     * Handles exceptions thrown by the game model.
     * Displays appropriate alerts for different exception types.
     *
     * @param e the exception to handle.
     */
    private void handleGameException(Exception e) {
        // Handle custom exceptions like:
        // - InvalidCardPlayException
        // - NoCardsAvailableException
        // - PlayerEliminatedException

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de Juego");
        alert.setHeaderText(null);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    /**
     * Handles the back button action.
     * Returns to the welcome screen.
     *
     * @param event the ActionEvent triggered by the back button.
     */
    @FXML
    void handleBack(ActionEvent event) {
        executorService.shutdown();
        try {
            CincuentazoWelcomeStage.getInstance();
            CincuentazoGameStage.deleteInstance();
        } catch (IOException e) {
            e.printStackTrace();
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