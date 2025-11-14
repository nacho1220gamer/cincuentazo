package cincuentazo.model.deck;

import cincuentazo.model.card.Card;
import cincuentazo.model.exceptions.EmptyDeckException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();
    }

    /**
     * Builds a standard ordered deck stack identical to Deck.initializeDeck().
     * Suits: Hearts, Diamonds, Clubs, Spades
     * Symbols: A, 2, ..., K
     */
    private Stack<Card> buildOrderedStandardStack() {
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] symbols = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        Stack<Card> stack = new Stack<>();
        for (String suit : suits) {
            for (String symbol : symbols) {
                stack.push(new Card(symbol, suit));
            }
        }
        return stack;
    }

    @Test
    void shuffle() throws Exception{
        // Access the private 'cards' field using reflection to control its content
        Field cardsField = Deck.class.getDeclaredField("cards");
        cardsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Stack<Card> internal = (Stack<Card>) cardsField.get(deck);

        // Replace current deck with a known, ordered stack
        internal.clear();
        Stack<Card> ordered = buildOrderedStandardStack();
        internal.addAll(ordered);

        // Save current order before shuffle
        Card[] before = internal.toArray(new Card[0]);

        // Perform shuffle
        deck.shuffle();

        // Compare card order before and after shuffle
        Card[] after = internal.toArray(new Card[0]);
        boolean changed = false;
        if (before.length == after.length) {
            for (int i = 0; i < before.length; i++) {
                if (!before[i].equals(after[i])) {
                    changed = true;
                    break;
                }
            }
        }

        assertTrue(changed, "shuffle() should usually change the order of cards");
    }

    @Test
    void drawCard() throws Exception{
        int initial = deck.remainingCards();
        Card c = deck.drawCard();
        assertNotNull(c, "drawCard() should return a non-null card");
        assertEquals(initial - 1, deck.remainingCards(), "Deck size should decrease by one after drawing a card");
    }

    @Test
    void addCardToBottom() throws Exception{
        Card drawn = deck.drawCard();
        int before = deck.remainingCards();
        deck.addCardToBottom(drawn);
        assertEquals(before + 1, deck.remainingCards(),
                "addCardToBottom() should increase deck size by one");
    }

    @Test
    void addCardsToBottom() {
        Stack<Card> newCards = new Stack<>();
        newCards.push(new Card("A", "Hearts"));
        newCards.push(new Card("K", "Spades"));

        int before = deck.remainingCards();
        deck.addCardsToBottom(newCards);
        assertEquals(before + 2, deck.remainingCards(),
                "addCardsToBottom() should increase deck size by the number of added cards");
        assertTrue(newCards.isEmpty(),
                "After addCardsToBottom(), the source stack should be empty (cards popped out)");
    }

    @Test
    void remainingCards() {
        int full = deck.remainingCards();
        assertTrue(full > 0, "Deck should contain cards upon initialization (expected 52)");
        try {
            deck.drawCard();
        } catch (EmptyDeckException e) {
            fail("Unexpected EmptyDeckException when drawing from a full deck");
        }
        assertEquals(full - 1, deck.remainingCards(),
                "remainingCards() should reflect the correct count after drawing a card");
    }

    @Test
    void resetDeck() {
        // Prepare a recycled stack with 2 cards
        Stack<Card> recycled = new Stack<>();
        recycled.push(new Card("A", "Hearts"));
        recycled.push(new Card("2", "Hearts"));

        int before = deck.remainingCards();
        deck.resetDeck(recycled);

        // resetDeck() adds all recycled cards and then shuffles
        assertEquals(before + recycled.size(), deck.remainingCards(),
                "resetDeck() should increase deck size by the number of recycled cards");

        // Implementation does not clear the recycled stack
        assertEquals(2, recycled.size(),
                "resetDeck() should not modify the passed recycled stack (no pop operations)");
    }
}