package cincuentazo.controller;

import cincuentazo.model.card.Card;
import cincuentazo.model.exceptions.EmptyDeckException;
import cincuentazo.model.exceptions.InvalidMoveException;
import cincuentazo.model.game.Game;
import cincuentazo.model.player.Player;
import cincuentazo.view.CincuentazoGameStage;
import cincuentazo.view.CincuentazoHelpStage;
import cincuentazo.view.CincuentazoWelcomeStage;
import cincuentazo.view.CincuentazoWinnerStage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Controller for the main game view of Cincuentazo.
 * Manages the game logic, UI updates, and player interactions.
 * Implements MVC pattern with proper separation of concerns.
 */
public class CincuentazoGameController {

    @FXML
    private VBox topVBox;

    @FXML
    private VBox leftVBox;

    @FXML
    private VBox rightVBox;

    @FXML
    private VBox bottomVBox;

    @FXML
    private Label tableSumLabel;

    @FXML
    private ImageView tableCardImage;

    private Game game;
    private Map<String, VBox> playerPositions;
    private Player humanPlayer;
    private final AtomicBoolean humanTurnActive = new AtomicBoolean(false);
    private Card selectedCard = null;
    private volatile boolean gameRunning = false;
    private Thread gameThread;
    private Map<ImageView, Card> cardViewMap;
    private int currentPlayerIndex = 0;
    private boolean cardPlayedThisTurn = false;

    private final AlertManager alertManager = new AlertManager(); //de la clase interna

    //Para hacer pruebas
    @FXML
    private ImageView testCard;

    /**
     * Initializes the game with the specified number of machine players.
     */
    public void initializeGame(int numMachines) {
        try {
            game = new Game(numMachines);
            humanPlayer = game.getPlayers().get(0);
            cardViewMap = new HashMap<>();
            gameRunning = true;

            assignPlayerPositions(numMachines);

            Platform.runLater(() -> {
                updateUI();
                showGameStart(numMachines);
            });

            startGameLoop();

        } catch (EmptyDeckException e) {
            alertManager.showError("Game Initialization Error", "Could not start the game: " + e.getMessage());
        }
    }

    /**
     * Shows game start message.*/

     private void showGameStart(int numPlayers) {
     Alert alert = new Alert(Alert.AlertType.INFORMATION);
     alert.initStyle(StageStyle.UNDECORATED);
     alert.setTitle("Game Start");
     alert.setHeaderText("¡Cincuentazo!");
     alert.setContentText("Playing against " + numPlayers + " CPU opponent(s).\nYour turn is first. Click a card to play!");
     alert.setGraphic(null);
     DialogPane dialogPane = alert.getDialogPane();
     URL css = getClass().getResource("/com/example/miniproyecto3/css/Styles.css");
     if (css != null) {
     dialogPane.getStylesheets().add(css.toExternalForm());
     } else {
     System.err.println("⚠️ CSS not found in /css/Styles.css");
     }
     dialogPane.getStyleClass().add("my-info-alert");
     alert.show();
     }



    /**
     * Assigns each player to a position on the screen.
     *
     * @param numMachines number of CPU opponents
     */
    private void assignPlayerPositions(int numMachines) {
        playerPositions = new HashMap<>();
        List<Player> players = game.getPlayers();

        // Human player always at bottom
        playerPositions.put(players.get(0).getName(), bottomVBox);

        // Assign machine players to available positions
        if (numMachines >= 1) {
            playerPositions.put(players.get(1).getName(), topVBox);
        }
        if (numMachines >= 2) {
            playerPositions.put(players.get(2).getName(), leftVBox);
        }
        if (numMachines >= 3) {
            playerPositions.put(players.get(3).getName(), rightVBox);
        }

        // Hide unused positions
        Platform.runLater(() -> {
            if (numMachines < 3) rightVBox.setVisible(false);
            if (numMachines < 2) leftVBox.setVisible(false);
        });
    }

    /**
     * Main game loop - now just orchestrates turns and UI updates.
     */
    private void startGameLoop() {
        gameThread = new Thread(() -> {
            try {
                while (gameRunning && !game.isGameOver()) {
                    Player currentPlayer = game.getCurrentPlayer();

                    if (currentPlayer == null) {
                        game.advanceTurn();
                        continue;
                    }

                    Platform.runLater(() -> highlightCurrentPlayer(currentPlayer));

                    if (currentPlayer.isMachine()) {
                        playMachineTurnUI(currentPlayer);
                    } else {
                        playHumanTurnUI();
                    }

                    Thread.sleep(500);
                }

                if (gameRunning) {
                    Platform.runLater(this::showGameOver);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        gameThread.setDaemon(true);
        gameThread.start();
    }

    /**
     * Handles machine player turn (UI side).
     */
    private void playMachineTurnUI(Player cpu) {
        try {
            // Realistic delay
            int delay = 2000 + (int) (Math.random() * 2000);
            Thread.sleep(delay);

            // Execute turn in model
            Card played = game.executeMachineTurn(cpu);

            Platform.runLater(() -> {
                if (played != null) {
                    // Successfully played
                    updateUI();
                } else {
                    // Player was eliminated
                    alertManager.showWarning("Player Eliminated!", cpu.getName() + " has been eliminated!");
                    updateUI();
                }
            });

            game.advanceTurn();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Handles human player turn (UI side).
     */
    private void playHumanTurnUI() throws InterruptedException {
        humanTurnActive.set(true);
        cardPlayedThisTurn = false;
        Platform.runLater(this::updateUI);

        // Wait for human action
        while (humanTurnActive.get() && gameRunning && !game.isGameOver()) {
            Thread.sleep(100);
        }
    }

    /**
     * Handles clicking on a card (human player).
     */
    private void handleCardClick(Card card, ImageView cardView) {
        if (!humanTurnActive.get()) {
            alertManager.showWarning("Not Your Turn", "Please wait for your turn.");
            return;
        }

        // Check if already played a card this turn
        if (cardPlayedThisTurn) {
            alertManager.showWarning("Already Played", "You already played a card this turn!\nClick 'Take Card' to draw a card.");
            return;
        }

        // Check if card is valid
        if (!game.isValidMove(card)) {
            if (!game.hasValidCards(humanPlayer)) {
                // No valid cards - eliminate player
                game.eliminatePlayer(humanPlayer);
                alertManager.showWarning("Eliminated!", "You have no valid cards to play.");
                humanTurnActive.set(false);
                cardPlayedThisTurn = false;
                game.advanceTurn();
                updateUI();
            } else {
                // Player has other valid cards
                int effect = card.calculateEffect(game.getTableSum());
                int newSum = game.getTableSum() + effect;
                alertManager.showWarning("Invalid Move", "This card would exceed 50 (new sum: " + newSum + ").\nChoose another card.");
            }
            return;
        }

        // Play the card
        try {
            game.executeHumanPlay(humanPlayer, card);
            cardPlayedThisTurn = true;
            updateUI();
        } catch (InvalidMoveException e) {
            alertManager.showWarning("Invalid Move", e.getMessage());
        }
    }

    /**
     * Handles the "Take Card" button.
     */
    @FXML
    private void handleTakeCard(ActionEvent event) {
        if (!humanTurnActive.get()) {
            alertManager.showWarning("Not Your Turn", "Please wait for your turn.");
            return;
        }

        game.executeHumanDraw(humanPlayer);
        updateUI();

        humanTurnActive.set(false);
        cardPlayedThisTurn = false;
        game.advanceTurn();
    }

    /**
     * Highlights the current player's position.
     */
    private void highlightCurrentPlayer(Player player) {
        // Remove all highlights
        removeAllHighlights();

        // Add highlight to current player
        VBox playerVBox = playerPositions.get(player.getName());
        if (playerVBox != null) {
            playerVBox.setStyle("-fx-background-color: rgba(225, 156, 27, 0.3); -fx-background-radius: 10;");
        }
    }

    /**
     * Removes highlights from all player positions.
     */
    private void removeAllHighlights() {
        for (VBox vbox : playerPositions.values()) {
            vbox.setStyle("");
        }
    }

    // ====== Deprecated methods ==========

//    /**
//     * Updates the table with a newly played card.
//     */
//    private void updateTableWithCard(Card card, int effect) {
//        game.setTopCard(card);
//        game.addTableSum(effect);
//
//        updateTableCard();
//        updateTableSum();
//    }
//
//    /**
//     * Handles when the deck is empty and needs to be recycled.
//     */
//    private void handleDeckEmpty() {
//        showInfo("Deck Empty", "Recycling cards from the table...");
//        // Game class should handle this
//        updateUI();
//    }

    // ============ UI UPDATE METHODS ============

    /**
     * Updates the entire UI.
     */
    private void updateUI() {
        Platform.runLater(() -> {
            updateTableSum();
            updateTableCard();
            updateAllPlayerHands();
        });
    }

    /**
     * Updates the table sum display.
     */
    private void updateTableSum() {
        if (tableSumLabel != null) {
            tableSumLabel.setText("Suma: " + game.getTableSum());
        }
    }

    /**
     * Updates the card displayed on the table.
     */
    private void updateTableCard() {
        if (tableCardImage != null && game.getTopCard() != null) {
            String imagePath = getCardImagePath(game.getTopCard());
            System.out.println(imagePath);
            var is = getClass().getResourceAsStream(imagePath);
            try {
                if (is != null) {
                    Image cardImage = new Image(is);
                    tableCardImage.setImage(cardImage);
                }
            } catch (Exception e) {
                System.err.println("Error loading card image: " + imagePath);
            }
        }
    }

    /**
     * Updates all players' hand displays.
     */
    private void updateAllPlayerHands() {
        for (Player player : game.getPlayers()) {
            VBox position = playerPositions.get(player.getName());
            if (position != null) {
                if (!player.isEliminated()) {
                    updatePlayerHand(player, position);
                } else {
                    clearPlayerDisplay(position);
                }
            }
        }
    }

    /**
     * Updates a specific player's hand display.
     */
    private void updatePlayerHand(Player player, VBox container) {
        // Buscar el HBox que contiene las cartas dentro del VBox entonces no asumimos indice fijo
        HBox cardContainer = null;
        for (var node : container.getChildren()) {
            if (node instanceof HBox h) {
                cardContainer = h;
                break;
            }
        }
        if (cardContainer == null) {
            // No hay HBox dentro del container entonces pues no hay nada que actualizar
            System.err.println("updatePlayerHand: no se encontró HBox en el container para " + player.getName());
            return;
        }

        List<Card> hand = player.getHand();
        System.out.println("Mano de " + player.getName() + ": " + hand);

        // Clean y re-draw the current hand
        cardContainer.getChildren().clear();
        // mantenemos cardViewMap para referencia, pero limpiamos y volvemos a llenar
        if (cardViewMap == null) cardViewMap = new HashMap<>();
        cardViewMap.clear();

        for (Card card : hand) {
            ImageView cardView = createCardView(card, !player.isMachine()); // faceUp para humano, faceDown para CPU

            if (!player.isMachine()) { // si es humano, agregamos listeners y efectos
                final Card currentCard = card;

                int effect = card.calculateEffect(game.getTableSum());
                int newSum = game.getTableSum() + effect;
                boolean isPlayable = (newSum <= 50);

                cardView.setOnMouseClicked(event -> handleCardClick(currentCard, cardView));

                cardView.setOnMouseEntered(event -> {
                    if (humanTurnActive.get()) {
                        cardView.setScaleX(1.15);
                        cardView.setScaleY(1.15);

                        DropShadow glow = new DropShadow();
                        glow.setRadius(25);
                        glow.setSpread(0.7);
                        glow.setColor(isPlayable ? Color.WHITE : Color.RED);
                        cardView.setEffect(glow);
                        cardView.setStyle("-fx-cursor: " + (isPlayable ? "hand" : "not-allowed") + ";");

                        javafx.animation.TranslateTransition lift =
                                new javafx.animation.TranslateTransition(javafx.util.Duration.millis(150), cardView);
                        lift.setFromY(cardView.getTranslateY());
                        lift.setToY(-10);
                        lift.play();
                    }
                });

                cardView.setOnMouseExited(event -> {
                    cardView.setEffect(null);
                    cardView.setScaleX(1.0);
                    cardView.setScaleY(1.0);
                    cardView.setStyle("-fx-cursor: hand;");
                    // animación de regreso
                    javafx.animation.TranslateTransition drop =
                            new javafx.animation.TranslateTransition(javafx.util.Duration.millis(150), cardView);
                    drop.setFromY(cardView.getTranslateY());
                    drop.setToY(0);
                    drop.play();
                });

                if (!isPlayable && humanTurnActive.get()) {
                    cardView.setOpacity(0.6);
                } else {
                    cardView.setOpacity(1.0);
                }

                cardViewMap.put(cardView, currentCard);
            }

            cardContainer.getChildren().add(cardView);
        }
    }

    /**
     * Creates an ImageView for a card.
     */
    private ImageView createCardView(Card card, boolean faceUp) {
        ImageView cardView = new ImageView();
        if (!faceUp) {
            cardView.setFitHeight(53.0);
            cardView.setFitWidth(38.0);
        } else {
            cardView.setFitHeight(82.0);
            cardView.setFitWidth(56.0);
        }
        cardView.setPreserveRatio(true);

        String imagePath = faceUp
                ? getCardImagePath(card)
                : "/com/example/miniproyecto3/images/backcard.png";

        var is = getClass().getResourceAsStream(imagePath);
        if (is != null) {
            cardView.setImage(new Image(is));
        } else {
            System.err.println("⚠️ No image found: " + imagePath);
        }

        return cardView;
    }

    /**
     * Clears a player's display when they are eliminated.
     */
    private void clearPlayerDisplay(VBox container) {
        if (container.getChildren().size() < 2) return;

        HBox cardContainer = (HBox) container.getChildren().get(1);
        cardContainer.getChildren().clear();

        Label eliminatedLabel = new Label("ELIMINATED");
        eliminatedLabel.setStyle("-fx-text-fill: #ff0000; -fx-font-size: 16px; -fx-font-weight: bold;");
        cardContainer.getChildren().add(eliminatedLabel);
        cardContainer.setAlignment(Pos.CENTER);
    }

    /**
     * Converts a Card to its image file path.
     */
    private String getCardImagePath(Card card) {
        String symbol = card.getSymbol();
        String suit = card.getSuit();

        String suitSpanish = switch (suit) {
            case "Hearts" -> "Corazones";
            case "Diamonds" -> "Diamantes";
            case "Clubs" -> "Treboles";
            case "Spades" -> "Picas";
            default -> suit;
        };

        return "/com/example/miniproyecto3/images/cards/" + symbol + suitSpanish + ".png";
    }

    /**
     * Shows the winner screen.
     */
    private void showGameOver() {
        Player winner = game.getWinner();

        if (winner != null) {
            shutdown();
            boolean humanWon = !winner.isMachine();

            try {
                CincuentazoGameStage.deleteInstance();

                if (humanWon) {
                    CincuentazoWinnerStage.getInstance();
                    System.out.println("Winner: " + winner.getName() + " (Human)");
                } else {
                    CincuentazoWelcomeStage.getInstance();
                    System.out.println("Winner: " + winner.getName() + " (Machine)");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ============ NAVIGATION METHODS ============

    /**
     * Handles the back button.
     */
    @FXML
    private void handleBack(ActionEvent event) {
        shutdown();
        try {
            // Close game stage (calls shutdown automatically)
            CincuentazoGameStage.deleteInstance();

            // Open welcome/menu stage
            CincuentazoWelcomeStage.getInstance();

        } catch (IOException e) {
            System.err.println("Error loading menu: " + e.getMessage());
            e.printStackTrace();
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
            CincuentazoHelpStage.getInstance("game");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows an error dialog.
     */
    private void showError(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /**
     * Shows an information dialog.

     private void showInfo(String title, String message) {
     Platform.runLater(() -> {
     Alert alert = new Alert(Alert.AlertType.INFORMATION);
     alert.initStyle(StageStyle.UNDECORATED);
     alert.setTitle(title);
     alert.setHeaderText(null);
     alert.setContentText(message);
     DialogPane dialogPane = alert.getDialogPane();
     URL css = getClass().getResource("/com/example/miniproyecto3/css/Styles.css");
     if (css != null) {
     dialogPane.getStylesheets().add(css.toExternalForm());
     } else {
     System.err.println("⚠️ No se encontró el archivo CSS en /css/Styles.css");
     }
     dialogPane.getStyleClass().add("my-info-alert");
     alert.showAndWait();
     });
     }*/

    /**
     * Shows a warning dialog.

     private void showWarning(String title, String message) {
     Platform.runLater(() -> {
     Alert alert = new Alert(Alert.AlertType.WARNING);
     alert.initStyle(StageStyle.UNDECORATED);
     alert.setTitle(title);
     alert.setHeaderText(null);
     alert.setContentText(message);
     DialogPane dialogPane = alert.getDialogPane();
     URL css = getClass().getResource("/com/example/miniproyecto3/css/Styles.css");
     if (css != null) {
     dialogPane.getStylesheets().add(css.toExternalForm());
     } else {
     System.err.println("⚠️ No se encontró el archivo CSS en /css/Styles.css");
     }

     dialogPane.getStyleClass().add("my-alert");
     alert.showAndWait();
     });
     }*/

    /**
     * Cleanup when controller is destroyed.
     */
    public void shutdown() {
        gameRunning = false;
        if (gameThread != null && gameThread.isAlive()) {
            gameThread.interrupt();
        }
        if (game != null) {
            game.stop();
        }
    }


    // Inner class managing alerts

    private class AlertManager {
        //private static final String CSS_PATH ="/com/example/miniproyecto3/css/Styles.css";

        /**
         * Shows the game start information alert.
         */
        public void showGameStart(int numPlayers) {
            Alert alert = createStyledAlert(
                    Alert.AlertType.INFORMATION,
                    "Game Start",
                    "¡Cincuentazo!",
                    "Playing against " + numPlayers + " CPU opponent(s).\nYour turn is first. Click a card to play!",
                    "my-info-alert"
            );
            alert.show();
        }

        /*
        Show error alert
         */
        public void showError(String title, String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();

        }

        /*
        Show information alert
         */
        public void showInfo(String title, String message) {
            Alert alert = createStyledAlert(
                    Alert.AlertType.INFORMATION, title, null, message, "my-info-alert"
            );
            alert.showAndWait();
        }


        /*
        Show warning alert
         */
        public void showWarning(String title, String message) {
            Alert alert = createStyledAlert(
                    Alert.AlertType.WARNING, title, null, message, "my-alert"
            );
            alert.showAndWait();
        }


        /*
        Create styled alert with custom CSS
         */
        private Alert createStyledAlert(Alert.AlertType type, String title, String header, String content, String styleClass) {
            Alert alert = new Alert(type);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.setGraphic(null);

            DialogPane dialogPane = alert.getDialogPane();
            URL css = CincuentazoGameController.this.getClass().getResource("/com/example/miniproyecto3/css/Styles.css");
            if (css != null) {
                dialogPane.getStylesheets().add(css.toExternalForm());
            } else {
                System.err.println("⚠️ CSS not found at: " + "/com/example/miniproyecto3/css/Styles.css");
            }
            dialogPane.getStyleClass().add(styleClass);

            return alert;
        }

    }

}

