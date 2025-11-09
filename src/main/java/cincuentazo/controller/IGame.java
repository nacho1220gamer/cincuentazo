package cincuentazo.controller;

import java.util.List;

/**
 * Interface that defines the contract between the Controller and the Model.
 * This interface should be implemented by a Game Adapter class that wraps
 * the actual game model provided by the model team.
 *
 * The controller will use this interface to interact with the game logic
 * without knowing the specific implementation details of the model.
 */
public interface IGame {

    /**
     * Starts a new game with the specified number of machine players.
     * Initializes the deck, deals cards, and sets up the initial table card.
     *
     * @param numberOfMachinePlayers the number of machine players (1, 2, or 3).
     */
    void startGame(int numberOfMachinePlayers);

    /**
     * Attempts to play a card from the human player's hand at the specified index.
     * Validates if the card can be played according to game rules.
     *
     * @param cardIndex the index of the card in the player's hand.
     * @return true if the card was successfully played, false otherwise.
     * @throws InvalidCardPlayException if the card would exceed the sum of 50.
     * @throws PlayerEliminatedException if the player has no valid cards to play.
     */
    boolean playCard(int cardIndex) throws InvalidCardPlayException, PlayerEliminatedException;

    /**
     * Allows the human player to draw a card from the deck after playing a card.
     *
     * @throws NoCardsAvailableException if the deck is empty and cannot be refilled.
     */
    void drawCard() throws NoCardsAvailableException;

    /**
     * Plays a card for the specified machine player.
     * The machine player selects a valid card automatically.
     *
     * @param playerNumber the machine player number (1, 2, or 3).
     * @throws PlayerEliminatedException if the machine player has no valid cards.
     */
    void playMachinePlayerCard(int playerNumber) throws PlayerEliminatedException;

    /**
     * Allows the specified machine player to draw a card from the deck.
     *
     * @param playerNumber the machine player number (1, 2, or 3).
     * @throws NoCardsAvailableException if the deck is empty and cannot be refilled.
     */
    void drawCardForMachinePlayer(int playerNumber) throws NoCardsAvailableException;

    /**
     * Gets the list of cards currently in the human player's hand.
     * These cards should be displayed face-up in the UI.
     *
     * @return a list of Card objects representing the human player's hand.
     */
    List<Object> getHumanPlayerCards(); // Change Object to your Card class

    /**
     * Gets the number of cards in the specified machine player's hand.
     * Used to display card backs in the UI.
     *
     * @param playerNumber the machine player number (1, 2, or 3).
     * @return the number of cards in the machine player's hand.
     */
    int getMachinePlayerCardCount(int playerNumber);

    /**
     * Gets the current card on top of the table.
     *
     * @return the Card object currently on the table.
     */
    Object getCurrentTableCard(); // Change Object to your Card class

    /**
     * Gets the current sum on the table.
     *
     * @return the current sum value.
     */
    int getTableSum();

    /**
     * Gets the name of the current player whose turn it is.
     *
     * @return the name of the current player (e.g., "Jugador", "Máquina 1").
     */
    String getCurrentPlayerName();

    /**
     * Checks if the specified player is still active in the game.
     *
     * @param playerNumber the player number (0 for human, 1-3 for machines).
     * @return true if the player is active, false if eliminated.
     */
    boolean isPlayerActive(int playerNumber);

    /**
     * Checks if the game is over (only one player remains).
     *
     * @return true if the game is over, false otherwise.
     */
    boolean isGameOver();

    /**
     * Gets the name of the winner when the game is over.
     *
     * @return the name of the winning player.
     */
    String getWinner();
}

/**
 * Exception thrown when a player attempts to play a card that would exceed
 * the table sum of 50.
 */
class InvalidCardPlayException extends Exception {
    public InvalidCardPlayException(String message) {
        super(message);
    }
}

/**
 * Exception thrown when a player has no valid cards to play and is eliminated.
 */
class PlayerEliminatedException extends Exception {
    public PlayerEliminatedException(String message) {
        super(message);
    }
}

/**
 * Exception thrown when trying to draw a card but the deck is empty
 * and cannot be refilled.
 */
class NoCardsAvailableException extends Exception {
    public NoCardsAvailableException(String message) {
        super(message);
    }
}