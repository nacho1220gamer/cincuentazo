package cincuentazo.controller;

import cincuentazo.model.card.Card;
import cincuentazo.model.exceptions.EmptyDeckException;
import cincuentazo.model.exceptions.InvalidMoveException;
import cincuentazo.model.game.Game;
import cincuentazo.model.player.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Controller for the main game view in Cincuentazo.
 * Handles user interactions, updates UI, and manages game flow with threading.
 */
public class CincuentazoGameController {

    // Center area (table)
    @FXML private VBox centerVBox;
    @FXML private Label sumLabel;
    @FXML private HBox tableCardContainer;
    @FXML private ImageView tableCardImage;

    // Player areas
    @FXML private BorderPane borderPane;
    @FXML private VBox leftVBox;    // CPU-1 (left)
    @FXML private VBox topVBox;     // CPU-2 (top)
    @FXML private VBox rightVBox;   // CPU-3 (right)
    @FXML private VBox bottomVBox;  // Human (bottom)

    // Player card containers
    private HBox humanCardsBox;
    private HBox cpu1CardsBox;
    private HBox cpu2CardsBox;
    private HBox cpu3CardsBox;

    // Player avatars
    private ImageView cpu1Avatar;
    private ImageView cpu2Avatar;
    private ImageView cpu3Avatar;

    private Stage stage;
    private Game game;
    private Player humanPlayer;
    private List<Player> cpuPlayers;
    private int numCPUs;

    private AtomicBoolean isHumanTurn = new AtomicBoolean(false);
    private AtomicBoolean waitingForHuman = new AtomicBoolean(false);
    private Card selectedCard = null;

    /**
     * Sets the stage for this controller.
     * @param stage the main application stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the game with the specified number of CPU players.
     * @param numCPUs number of CPU players (1-3)
     */
    public void initializeGame(int numCPUs) {
        this.numCPUs = numCPUs;
        this.cpuPlayers = new ArrayList<>();

        try {
            // Initialize game model (but don't start automatic thread yet)
            game = createGameWithoutAutoStart(numCPUs);

            // Get players
            for (Player p : game.getPlayers()) {
                if (!p.isMachine()) {
                    humanPlayer = p;
                } else {
                    cpuPlayers.add(p);
                }
            }

            // Setup UI
            setupPlayerAreas();
            updateUI();

            // Start custom game loop
            startCustomGameLoop();

        } catch (EmptyDeckException e) {
            showError("Failed to initialize game: " + e.getMessage());
        }
    }

    /**
     * Creates a game instance without starting the automatic thread.
     * We need manual control for GUI synchronization.
     */
    private Game createGameWithoutAutoStart(int numMachines) throws EmptyDeckException {
        // We'll need to modify Game class or create a custom initialization
        // For now, we create the game normally but we'll control turns manually
        return new Game(numMachines);
    }

    public void stopGame() {
        // Si quieres simplemente detener el hilo del juego
        if (game != null) {
            // Puedes agregar un metodo en Game para terminar el bucle
            game.stop(); // o algo similar
        }

        // También puedes usar una bandera local para detener la espera de turnos
        waitingForHuman.set(false);
        isHumanTurn.set(false);
    }
    /**
     * Sets up the player areas in the UI based on number of CPUs.
     */
    private void setupPlayerAreas() {
        // Get HBox containers from FXML
        humanCardsBox = (HBox) bottomVBox.getChildren().get(0);

        if (numCPUs >= 1) {
            cpu1CardsBox = (HBox) leftVBox.getChildren().get(1);
            cpu1Avatar = (ImageView) leftVBox.getChildren().get(0);
        } else {
            leftVBox.setVisible(false);
        }

        if (numCPUs >= 2) {
            cpu2CardsBox = (HBox) topVBox.getChildren().get(1);
            cpu2Avatar = (ImageView) topVBox.getChildren().get(0);
        } else {
            topVBox.setVisible(false);
        }

        if (numCPUs >= 3) {
            cpu3CardsBox = (HBox) rightVBox.getChildren().get(1);
            cpu3Avatar = (ImageView) rightVBox.getChildren().get(0);
        } else {
            rightVBox.setVisible(false);
        }
    }

    /**
     * Starts the custom game loop that handles turns with proper GUI synchronization.
     */
    private void startCustomGameLoop() {
        Thread gameThread = new Thread(() -> {
            while (!game.isGameOver()) {
                try {
                    Player currentPlayer = getCurrentPlayer();

                    if (currentPlayer == null || currentPlayer.isEliminated()) {
                        Thread.sleep(500);
                        continue;
                    }

                    if (currentPlayer.isMachine()) {
                        handleCPUTurn(currentPlayer);
                    } else {
                        handleHumanTurn(currentPlayer);
                    }

                    Platform.runLater(this::updateUI);

                    if (game.isGameOver()) {
                        Platform.runLater(this::showWinner);
                        break;
                    }

                    Thread.sleep(1000); // Delay between turns

                } catch (InterruptedException e) {
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        gameThread.setDaemon(true);
        gameThread.start();
    }

    /**
     * Gets the current player from the game's turn queue.
     */
    private Player getCurrentPlayer() {
        // This requires accessing the game's turn queue
        // We'll need to add a getter in Game class
        // For now, we'll rotate through players manually
        List<Player> activePlayers = new ArrayList<>();
        for (Player p : game.getPlayers()) {
            if (!p.isEliminated()) {
                activePlayers.add(p);
            }
        }

        if (activePlayers.isEmpty()) {
            return null;
        }

        // Simple round-robin (you may need to improve this)
        return activePlayers.get(0);
    }

    /**
     * Handles a CPU player's turn with delay.
     */
    private void handleCPUTurn(Player cpu) {
        try {
            // Visual feedback: highlight CPU
            Platform.runLater(() -> highlightCurrentPlayer(cpu));

            // CPU thinks for 2-4 seconds
            Thread.sleep(2000 + (int)(Math.random() * 2000));

            // Play card
            Card played = cpu.playCard(game.getTableSum());
            int effect = played.calculateEffect(game.getTableSum());

            Platform.runLater(() -> {
                // Update table (this will need game model updates)
                updateTableCard(played);
                updateSum(game.getTableSum() + effect);
            });

            Thread.sleep(1000);

            // Draw new card
            cpu.drawCard(game.getDeck());

        } catch (InvalidMoveException e) {
            Platform.runLater(() -> {
                showElimination(cpu);
                eliminatePlayer(cpu);
            });
        } catch (EmptyDeckException e) {
            Platform.runLater(() -> showError("Deck is empty: " + e.getMessage()));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Handles the human player's turn.
     * Waits for user interaction with cards.
     */
    private void handleHumanTurn(Player human) {
        Platform.runLater(() -> {
            highlightCurrentPlayer(human);
            isHumanTurn.set(true);
            waitingForHuman.set(true);
            enableHumanCards(true);
        });

        // Wait for human to play
        while (waitingForHuman.get() && !game.isGameOver()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }

        isHumanTurn.set(false);
    }

    /**
     * Handles when human clicks a card.
     */
    @FXML
    private void handleCardClick(javafx.event.ActionEvent event) {
        if (!isHumanTurn.get()) {
            return;
        }

        ImageView clickedCard = (ImageView) event.getSource();
        int cardIndex = humanCardsBox.getChildren().indexOf(clickedCard);

        if (cardIndex < 0 || cardIndex >= humanPlayer.getHand().size()) {
            return;
        }

        Card card = humanPlayer.getHand().get(cardIndex);

        try {
            // Validate move
            int effect = card.calculateEffect(game.getTableSum());
            int newSum = game.getTableSum() + effect;

            if (newSum > 50) {
                showError("This card would exceed 50! Choose another or you'll be eliminated.");
                return;
            }

            // Play the card
            humanPlayer.getHand().remove(cardIndex);
            updateTableCard(card);
            updateSum(newSum);

            // Draw new card
            humanPlayer.drawCard(game.getDeck());

            enableHumanCards(false);
            waitingForHuman.set(false);

        } catch (EmptyDeckException e) {
            showError("Cannot draw card: " + e.getMessage());
        } catch (Exception e) {
            showError("Error playing card: " + e.getMessage());
        }
    }

    /**
     * Updates the UI with current game state.
     */
    private void updateUI() {
        // Update human player cards
        updatePlayerCards(humanPlayer, humanCardsBox, false);

        // Update CPU cards (face down)
        if (numCPUs >= 1 && cpuPlayers.size() > 0) {
            updatePlayerCards(cpuPlayers.get(0), cpu1CardsBox, true);
        }
        if (numCPUs >= 2 && cpuPlayers.size() > 1) {
            updatePlayerCards(cpuPlayers.get(1), cpu2CardsBox, true);
        }
        if (numCPUs >= 3 && cpuPlayers.size() > 2) {
            updatePlayerCards(cpuPlayers.get(2), cpu3CardsBox, true);
        }

        // Update table
        if (game.getTopCard() != null) {
            updateTableCard(game.getTopCard());
        }

        // Update sum
        updateSum(game.getTableSum());
    }

    /**
     * Updates a player's card display.
     */
    private void updatePlayerCards(Player player, HBox container, boolean faceDown) {
        container.getChildren().clear();

        for (Card card : player.getHand()) {
            ImageView cardView = new ImageView();
            cardView.setFitHeight(82);
            cardView.setFitWidth(56);
            cardView.setPreserveRatio(true);

            if (faceDown || player.isEliminated()) {
                cardView.setImage(loadImage("backcard.png"));
            } else {
                cardView.setImage(loadCardImage(card));
                cardView.setOnMouseClicked(e -> handleCardClickEvent(card));
                cardView.setStyle("-fx-cursor: hand;");
            }

            container.getChildren().add(cardView);
        }
    }

    /**
     * Handles card click for human player.
     */
    private void handleCardClickEvent(Card card) {
        if (!isHumanTurn.get()) {
            return;
        }

        try {
            int effect = card.calculateEffect(game.getTableSum());
            int newSum = game.getTableSum() + effect;

            if (newSum > 50) {
                showError("This card would make sum = " + newSum + " (>50). Choose another!");
                return;
            }

            humanPlayer.getHand().remove(card);
            updateTableCard(card);
            updateSum(newSum);
            humanPlayer.drawCard(game.getDeck());

            enableHumanCards(false);
            waitingForHuman.set(false);

        } catch (EmptyDeckException e) {
            showError("Cannot draw card: " + e.getMessage());
        }
    }

    /**
     * Updates the table card display.
     */
    private void updateTableCard(Card card) {
        tableCardImage.setImage(loadCardImage(card));
    }

    /**
     * Updates the sum display.
     */
    private void updateSum(int sum) {
        sumLabel.setText("Suma: " + sum);
    }

    /**
     * Loads a card image based on card properties.
     */
    private Image loadCardImage(Card card) {
        String suitMap = switch (card.getSuit()) {
            case "Hearts" -> "Corazones";
            case "Diamonds" -> "Diamantes";
            case "Clubs" -> "Treboles";
            case "Spades" -> "Picas";
            default -> card.getSuit();
        };

        String filename = card.getSymbol() + suitMap + ".png";
        return loadImage("cards/" + filename);
    }

    /**
     * Loads an image from resources.
     */
    private Image loadImage(String path) {
        try {
            return new Image(getClass().getResourceAsStream("/cincuentazo/images/" + path));
        } catch (Exception e) {
            System.err.println("Failed to load image: " + path);
            return null;
        }
    }

    /**
     * Enables or disables human cards for interaction.
     */
    private void enableHumanCards(boolean enable) {
        // This would set the clickable state
        // Implementation depends on how cards are rendered
    }

    /**
     * Highlights the current player.
     */
    private void highlightCurrentPlayer(Player player) {
        // Add visual feedback (glow, border, etc.)
        // Implementation depends on your design
    }

    /**
     * Shows elimination message.
     */
    private void showElimination(Player player) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Player Eliminated");
        alert.setHeaderText(player.getName() + " has been eliminated!");
        alert.setContentText("No valid cards to play.");
        alert.showAndWait();
    }

    /**
     * Eliminates a player.
     */
    private void eliminatePlayer(Player player) {
        List<Card> cards = player.eliminate();
        for (Card c : cards) {
            game.getDeck().addCardToBottom(c);
        }
        updateUI();
    }

    /**
     * Shows the winner.
     */
    private void showWinner() {
        Player winner = null;
        for (Player p : game.getPlayers()) {
            if (!p.isEliminated()) {
                winner = p;
                break;
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("🎉 Winner: " + (winner != null ? winner.getName() : "No one"));
        alert.setContentText("Congratulations!");
        alert.showAndWait();
    }

    /**
     * Shows an error dialog.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Game Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Handles the "Back" button to return to menu.
     */
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cincuentazo/cincuentazo-menu-view.fxml"));
            Parent root = loader.load();

            CincuentazoWelcomeController welcomeController = loader.getController();
            welcomeController.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Cincuentazo");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Help" button.
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
}