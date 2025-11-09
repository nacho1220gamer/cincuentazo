package cincuentazo.model.player;

import cincuentazo.model.exceptions.EmptyDeckException;
import cincuentazo.model.exceptions.InvalidMoveException;
import cincuentazo.model.card.Card;
import cincuentazo.model.deck.IDeck;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player (human or machine) in the "Cincuentazo" game.
 */
public class Player extends PlayerAdapter {

    private final String name;
    private final boolean machine;
    private final List<Card> hand;
    private boolean eliminated;

    public Player(String name, boolean machine) {
        this.name = name;
        this.machine = machine;
        this.hand = new ArrayList<>();
        this.eliminated = false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Card> getHand() {
        return hand;
    }

    @Override
    public boolean isMachine() {
        return machine;
    }

    @Override
    public boolean isEliminated() {
        return eliminated;
    }

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

    @Override
    public void drawCard(IDeck deck) throws EmptyDeckException {
        if (hand.size() < 4) {
            hand.add(deck.drawCard());
        }
    }

    @Override
    public List<Card> eliminate() {
        this.eliminated = true;
        List<Card> remaining = new ArrayList<>(hand);
        hand.clear();
        return remaining;
    }

    /**
     * Deals 4 initial cards to the player.
     */
    public void dealInitialCards(IDeck deck) throws EmptyDeckException {
        while (hand.size() < 4) {
            hand.add(deck.drawCard());
        }
    }

    @Override
    public String toString() {
        return name + (machine ? " (CPU)" : " (Human)") + " - Cards: " + hand;
    }
}