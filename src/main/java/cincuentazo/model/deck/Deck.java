package cincuentazo.model.deck;

import cincuentazo.model.exceptions.EmptyDeckException;
import cincuentazo.model.card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Represents the deck of cards used in the "Cincuentazo" game.
 * Handles card creation, shuffling, drawing, and resetting.
 */
public class Deck extends DeckAdapter {

    private final Stack<Card> cards;

    public Deck() {
        cards = new Stack<>();
        initializeDeck();
        shuffle();
    }

    /**
     * Initializes the deck with 52 standard cards.
     */
    private void initializeDeck() {
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] symbols = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

        for (String suit : suits) {
            for (String symbol : symbols) {
                cards.push(new Card(symbol, suit));
            }
        }
    }

    /**
     * Randomly shuffles the deck.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    @Override
    public Card drawCard() throws EmptyDeckException {
        if(!cards.isEmpty()){
            return cards.pop();
        }
        throw new EmptyDeckException("No more cards left in the deck!");
    }

    /**
     * Adds a card to the bottom of the deck (used when a player is eliminated).
     * @param card the card to be added
     */
    public void addCardToBottom(Card card) {
        cards.insertElementAt(card, 0);
    }

    /**
     * Adds multiple cards (for example, from an eliminated player or from the table).
     */
    public void addCardsToBottom(Stack<Card> newCards) {
        while (!newCards.isEmpty()) {
            addCardToBottom(newCards.pop());
        }
    }

    /**
     * Returns the number of remaining cards in the deck.
     */
    public int remainingCards() {
        return cards.size();
    }

    /**
     * Rebuilds the deck with new cards (used when cards run out).
     */
    public void resetDeck(Stack<Card> recycledCards) {
        cards.addAll(recycledCards);
        shuffle();
    }

// Inner static class responsible for creating card

    private static class CardFactory {
        private static final String[] SUITS= {"Hearts", "Diamonds", "Clubs", "Spades"};
        private static final String[] Symbols= {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

        //Creates a standart 52-card deck

        static List<Card> createStandardDeck() {
            List<Card> deck = new ArrayList<>();
            for (String suit : SUITS) {
                for (String symbol : Symbols) {
                    deck.add(new Card(symbol, suit));
                }

            }
            return deck;
        }

    }
}


