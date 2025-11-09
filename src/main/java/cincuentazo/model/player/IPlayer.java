package cincuentazo.model.player;

import cincuentazo.model.exceptions.EmptyDeckException;
import cincuentazo.model.exceptions.InvalidMoveException;
import cincuentazo.model.card.Card;
import cincuentazo.model.deck.IDeck;

import java.util.List;

/**
 * Interface that defines the basic behavior of a player in the "Cincuentazo" game.
 */
public interface IPlayer {

    /**
     * Returns the player's name.
     */
    String getName();

    /**
     * Returns the player's hand (list of 4 cards).
     */
    List<Card> getHand();

    /**
     * Plays a card according to the game rules.
     * @param currentSum current sum of the table
     * @return the card that was played
     * @throws InvalidMoveException if no valid card can be played
     */
    Card playCard(int currentSum) throws InvalidMoveException;

    /**
     * Draws a new card from the deck.
     */
    void drawCard(IDeck deck) throws EmptyDeckException;

    /**
     * Checks if the player has been eliminated.
     */
    boolean isEliminated();

    /**
     * Eliminates the player (sets status and returns their cards).
     */
    List<Card> eliminate();

    /**
     * Returns true if the player is controlled by the computer.
     */
    boolean isMachine();
}
