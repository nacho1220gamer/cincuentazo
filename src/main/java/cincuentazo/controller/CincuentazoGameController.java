package cincuentazo.controller;

import cincuentazo.model.card.Card;
import cincuentazo.model.exceptions.EmptyDeckException;
import cincuentazo.model.exceptions.InvalidMoveException;
import cincuentazo.model.game.Game;
import cincuentazo.model.player.Player;
import cincuentazo.view.CincuentazoGameStage;
import cincuentazo.view.CincuentazoWinnerStage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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

import java.io.IOException;
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

    //Para hacer pruebas
    @FXML
    private ImageView testCard;

    /**
     * Initializes the game with the specified number of machine players.
     * @param numMachines number of CPU opponents (1-3)
     */
    public void initializeGame(int numMachines) {
        try {
            game = new Game(numMachines);
            humanPlayer = game.getPlayers().get(0); // Human is always first
            cardViewMap = new HashMap<>();
            gameRunning = true;

            assignPlayerPositions(numMachines);

            // Initial UI update
            Platform.runLater(() -> {
                updateUI();
                showGameStart(numMachines);
            });

            Platform.runLater(() -> {
                if (testCard != null) {
                    testCard.setOnMouseEntered(e -> System.out.println("Entró en carta del FXML"));
                    testCard.setOnMouseExited(e -> System.out.println("Salió de carta del FXML"));
                } else {
                    System.out.println("testCard es null (no está enlazado al FXML)");
                }

                // 🔹 NUEVO: detectar todas las cartas visibles del jugador (bottomVBox)
                if (bottomVBox != null) {
                    for (var node : bottomVBox.lookupAll(".image-view")) {
                        if (node instanceof ImageView imageView) {
                            imageView.setOnMouseEntered(e -> System.out.println("Mouse sobre una carta"));
                            imageView.setOnMouseExited(e -> System.out.println("Mouse salió de una carta"));
                        }
                    }
                    System.out.println("Eventos agregados a todas las cartas del jugador.");
                } else {
                    System.out.println("bottomVBox es null — no se pudieron buscar las cartas.");
                }
            });

            // Start game loop in separate thread
            startGameLoop();

        } catch (EmptyDeckException e) {
            showError("Game Initialization Error", "Could not start the game: " + e.getMessage());
        }
    }

    /**
     * Shows game start message.
     */
    private void showGameStart(int numPlayers) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Start");
        alert.setHeaderText("¡Cincuentazo!");
        alert.setContentText("Playing against " + numPlayers + " CPU opponent(s).\nYour turn is first. Click a card to play!");
        alert.show();
    }

    /**
     * Assigns each player to a position on the screen.
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
     * Main game loop that manages turns between players.
     */
    private void startGameLoop() {
        gameThread = new Thread(() -> {
            try {
                while (gameRunning && !game.isGameOver()) {
                    Player currentPlayer = getCurrentPlayer();

                    if (currentPlayer == null || currentPlayer.isEliminated()) {
                        advanceTurn();
                        continue;
                    }

                    Platform.runLater(() -> highlightCurrentPlayer(currentPlayer));

                    if (currentPlayer.isMachine()) {
                        // Machine player turn
                        playMachineTurn(currentPlayer);
                    } else {
                        // Human player turn
                        playHumanTurn(currentPlayer);
                    }

                    Thread.sleep(500); // Small delay between actions
                }

                if (gameRunning) {
                    Platform.runLater(this::showGameOver);
                }

            } catch (InterruptedException e) {
                System.out.println("Game thread interrupted");
                Thread.currentThread().interrupt();
            }
        });
        gameThread.setDaemon(true);
        gameThread.start();
    }

    /**
     * Gets the current player whose turn it is.
     */
    private Player getCurrentPlayer() {
        List<Player> activePlayers = game.getPlayers().stream()
                .filter(p -> !p.isEliminated())
                .toList();

        if (activePlayers.isEmpty()) { return null;}

        // This is a simple round-robin approach
        // You may need to adjust based on your Game class implementation
        if (currentPlayerIndex >= activePlayers.size()) {
            currentPlayerIndex = 0;
        }
        return activePlayers.get(currentPlayerIndex);
    }

    /**
     * Advances to the next player's turn.
     */
    private void advanceTurn() {
        currentPlayerIndex++;
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

    /**
     * Executes a machine player's turn.
     */
    private void playMachineTurn(Player cpu) {
        try {
            // Random delay between 2-4 seconds for realism
            int delay = 2000 + (int)(Math.random() * 2000);
            Thread.sleep(delay);

            // Try to play a card
            Card played = cpu.playCard(game.getTableSum());
            int effect = played.calculateEffect(game.getTableSum());

            // Update game state (this should be in Game class ideally)
            Platform.runLater(() -> {
                // Update table
                updateTableWithCard(played, effect);

                // Draw new card
                try {
                    cpu.drawCard(game.getDeck());
                } catch (EmptyDeckException e) {
                    handleDeckEmpty();
                }

                // Update UI
                updateUI();

                showInfo("CPU Turn", cpu.getName() + " played " + played + "\nNew sum: " + game.getTableSum());
            });

            // advance turn after the CPU finishes its action, or it will loop infinitely
            advanceTurn();

        } catch (InvalidMoveException e) {
            // If the CPU made an invalid move, handle elimination and still advance the turn
            Platform.runLater(() -> {
                handlePlayerElimination(cpu, e.getMessage());
            });
            advanceTurn();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    /**
     * Handles the human player's turn.
     */
    private void playHumanTurn(Player player) throws InterruptedException {
        humanTurnActive.set(true);
        Platform.runLater(() -> {
            updateUI();
            showInfo("Your Turn!", "Select a card to play from your hand.");
        });

        // Wait for human to play
        while (humanTurnActive.get() && gameRunning && !game.isGameOver()) {
            Thread.sleep(100);
        }
    }

    /**
     * Handles clicking on a card in the human player's hand.
     */
    private void handleCardClick(Card card, ImageView cardView) {
        if (!humanTurnActive.get()) {
            showWarning("Not Your Turn", "Please wait for your turn to play.");
            return;
        }

        // Check if card is valid
        int effect = card.calculateEffect(game.getTableSum());
        int newSum = game.getTableSum() + effect;

        if (newSum > 50) {
            boolean hasValidCard = humanPlayer.getHand().stream()
                    .anyMatch(c -> game.getTableSum() + c.calculateEffect(game.getTableSum()) <= 50);

            if (hasValidCard) {
                showWarning("Invalid Move", "This card would exceed 50 (new sum: " + newSum + ").\nChoose another card.");
                return;
            } else {
                // No valid cards - player is eliminated
                handlePlayerElimination(humanPlayer, "No valid cards to play");
                humanTurnActive.set(false);
                return;
            }
        }

        // Play the card
        playHumanCard(card);

        humanTurnActive.set(false);
        advanceTurn();
    }


    /**
     * Plays the selected card for the human player.
     */
    private void playHumanCard(Card card) {
        try {
            humanPlayer.getHand().remove(card);
            int effect = card.calculateEffect(game.getTableSum());

            // Update table
            updateTableWithCard(card, effect);

            // Draw a new card
            humanPlayer.drawCard(game.getDeck());

            // Update UI
            updateUI();

            // End turn
            humanTurnActive.set(false);

        } catch (EmptyDeckException e) {
            handleDeckEmpty();
            humanTurnActive.set(false);
        }
    }

    /**
     * Updates the table with a newly played card.
     */
    private void updateTableWithCard(Card card, int effect) {
        game.setTopCard(card);
        game.addTableSum(effect);

        updateTableCard();
        updateTableSum();
    }

    /**
     * Handles when the deck is empty and needs to be recycled.
     */
    private void handleDeckEmpty() {
        showInfo("Deck Empty", "Recycling cards from the table...");
        // Game class should handle this
        updateUI();
    }

    /**
     * Handles when a player is eliminated.
     */
    private void handlePlayerElimination(Player player, String reason) {
        List<Card> eliminatedCards = player.eliminate();

        // Return cards to deck
        for (Card c : eliminatedCards) {
            game.getDeck().addCardToBottom(c);
        }

        updateUI();

        showWarning("Player Eliminated!", player.getName() + " has been eliminated!\n" + reason);

        // Check for winner
        checkForWinner();
    }

    /**
     * Checks if there's a winner.
     */
    private void checkForWinner() {
        long activePlayers = game.getPlayers().stream()
                .filter(p -> !p.isEliminated())
                .count();

        if (activePlayers == 1) {
            gameRunning = false;
            Platform.runLater(this::showGameOver);
        }
    }

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
                if(is != null) {
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
                        glow.setColor(isPlayable ? Color.LIME : Color.RED);
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
        if(!faceUp) {
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
     * Shows the game over screen (winner stage).
     */
    private void showGameOver() {
        List<Player> players = game.getPlayers();
        Player winner = players.stream()
                .filter(p -> !p.isEliminated())
                .findFirst()
                .orElse(null);

        if (winner != null) {
            boolean humanWon = !winner.isMachine();

            try {
                // Cierra la ventana actual del juego
                CincuentazoGameStage.deleteInstance();

                if (humanWon) {
                    // 🏆 Si el humano gana → muestra pantalla de ganador
                    CincuentazoWinnerStage.getInstance();
                    System.out.println("Game over. Winner: " + winner.getName() + " (Jugador humano)");
                } else {
                    // 💀 Si gana la máquina → regresa al menú o muestra un alert
                    System.out.println("Game over. Winner: " + winner.getName() + " (Máquina)");

                }

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error loading end screen: " + e.getMessage());
            }
        } else {
            System.out.println("No winner found — no one survived?");
        }
    }

    /**
     * Handles the back button.
     */
    @FXML
    private void handleBack(ActionEvent event) {
        gameRunning = false;
        if (gameThread != null && gameThread.isAlive()) {
            gameThread.interrupt();
        }
        if (game != null) {
            game.stop();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/miniproyecto3/fxml/cincuentazo-menu-view.fxml"));
            Parent menuView = loader.load();

            Stage stage = (Stage) bottomVBox.getScene().getWindow();
            Scene scene = new Scene(menuView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading menu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the help button.
     */
    @FXML
    private void handleHelp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/miniproyecto3/fxml/cincuentazo-help-view.fxml"));
            Parent helpView = loader.load();

            Stage stage = (Stage) bottomVBox.getScene().getWindow();
            Scene scene = new Scene(helpView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading help: " + e.getMessage());
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
     */
    private void showInfo(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /**
     * Shows a warning dialog.
     */
    private void showWarning(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /**
     * Cleanup when controller is destroyed.
     */
    public void shutdown() {
        gameRunning = false;
        if (gameThread != null && gameThread.isAlive()) {
            gameThread.interrupt();
        }
    }
}