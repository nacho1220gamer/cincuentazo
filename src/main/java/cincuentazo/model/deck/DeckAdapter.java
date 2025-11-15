package cincuentazo.model.deck;

import cincuentazo.model.exceptions.EmptyDeckException;
import cincuentazo.model.card.Card;

import java.util.Stack;

/**
 * Concrete implementation of the Deck used in the "Cincuentazo" game.
 */

/**
 * Adapter class that provides empty implementations of IDeck.
 * Subclasses can override only the methods they need.
 */
public abstract class DeckAdapter implements IDeck {

    @Override
    public void shuffle() {
        // Default: do nothing
    }

    @Override
    public Card drawCard() throws EmptyDeckException {
        throw new EmptyDeckException("Empty Deck");
    }

    @Override
    public void addCardToBottom(Card card) {
        // Default: do nothing
    }

    @Override
    public void addCardsToBottom(Stack<Card> newCards) {
        // Default: do nothing
    }

    @Override
    public int remainingCards() {
        return 0;
    }

    @Override
    public void resetDeck(Stack<Card> recycledCards) {
        // Default: do nothing
    }
}