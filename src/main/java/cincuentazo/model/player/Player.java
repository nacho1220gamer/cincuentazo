package cincuentazo.model.player;

import cincuentazo.model.exceptions.EmptyDeckException;
import cincuentazo.model.exceptions.InvalidMoveException;
import cincuentazo.model.card.Card;
import cincuentazo.model.deck.IDeck;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player (human or machine) in the "Cincuentazo" game.
 * A player holds a hand of cards, can draw new cards from the deck, and attempts
 * to play a valid card based on the current accumulated sum of the game.
 * If a player cannot make a valid move, they are eliminated from the round.
 */
public class Player extends PlayerAdapter {

    private final String name;
    private final boolean machine;
    private final List<Card> hand;
    private boolean eliminated;

    /**
     * Creates a new player.
     *
     * @param name    the player's display name
     * @param machine {@code true} if the player is a CPU-controlled player; {@code false} otherwise
     */

    public Player(String name, boolean machine) {
        this.name = name;
        this.machine = machine;
        this.hand = new ArrayList<>();
        this.eliminated = false;
    }

    /**
     * Returns the name of the player.
     *
     * @return the player's name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the list of cards in the player's hand.
     *
     * @return the player's hand
     */
    @Override
    public List<Card> getHand() {
        return hand;
    }

    /**
     * Returns whether the player is CPU-controlled.
     *
     * @return {@code true} if the player is a machine; {@code false} otherwise
     */
    @Override
    public boolean isMachine() {
        return machine;
    }

    /**
     * Returns whether the player has been eliminated from the game.
     *
     * @return {@code true} if eliminated; {@code false} otherwise
     */
    @Override
    public boolean isEliminated() {
        return eliminated;
    }

    /**
     * Attempts to play a valid card from the player's hand based on the current sum.
     * A valid card is one that does not cause the sum to exceed 50. The first valid
     * card found is played and removed from the hand.
     * If no valid card is found, the player is eliminated and an {@link InvalidMoveException} is thrown.
     *
     * @param currentSum the current accumulated sum in the game
     * @return the card played
     * @throws InvalidMoveException if the player has no valid card to play
     */
    @Override
    public Card playCard(int currentSum) throws InvalidMoveException {
        for (Card card : hand) {
            int effect = card.calculateEffect(currentSum);
            int newSum = currentSum + effect;

            if (newSum <= 50) {
                hand.remove(card);
                return card;
            }
        }

        // If not returned yet, throw exception
        this.eliminated = true;
        throw new InvalidMoveException(name + " has no valid card to play and is eliminated.");
    }

    /**
     * Draws a card from the deck if the player has fewer than 4 cards.
     *
     * @param deck the deck from which to draw
     * @throws EmptyDeckException if the deck has no remaining cards
     */
    @Override
    public void drawCard(IDeck deck) throws EmptyDeckException {
        if (hand.size() < 4) {
            hand.add(deck.drawCard());
        }
    }

    /**
     * Eliminates the player and removes all cards from their hand.
     *
     * @return a list containing all cards removed from the player's hand
     */
    @Override
    public List<Card> eliminate() {
        this.eliminated = true;
        List<Card> remaining = new ArrayList<>(hand);
        hand.clear();
        return remaining;
    }

    /**
     * Deals the initial 4 cards to the player at the start of the game.
     *
     * @param deck the deck from which to deal cards
     * @throws EmptyDeckException if the deck runs out of cards
     */
    public void dealInitialCards(IDeck deck) throws EmptyDeckException {
        while (hand.size() < 4) {
            hand.add(deck.drawCard());
        }
    }

    /**
     * Returns a string representation of the player, showing the name,
     * whether they are human or CPU, and their current hand.
     *
     * @return a formatted string representing the player
     */
    @Override
    public String toString() {
        return name + (machine ? " (CPU)" : " (Human)") + " - Cards: " + hand;
    }
}