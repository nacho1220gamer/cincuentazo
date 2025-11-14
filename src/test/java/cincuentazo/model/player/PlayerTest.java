package cincuentazo.model.player;

import cincuentazo.model.card.Card;
import cincuentazo.model.deck.IDeck;
import cincuentazo.model.exceptions.EmptyDeckException;
import cincuentazo.model.exceptions.InvalidMoveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;
class PlayerTest {

    private Player humanPlayer;
    private Player cpuPlayer;

    @BeforeEach
    void setUp() {
        humanPlayer = new Player("Alice", false);
        cpuPlayer = new Player("Bot", true);
    }

    @Test
    void getName() {
        assertEquals("Alice", humanPlayer.getName(), "getName() should return the player's name");
    }

    @Test
    void getHand() {
        assertTrue(humanPlayer.getHand().isEmpty(), "Hand should start empty");
        humanPlayer.getHand().add(new Card("5", "Hearts"));
        assertEquals(1, humanPlayer.getHand().size(), "Hand should reflect added cards");
    }

    @Test
    void isMachine() {
        assertFalse(humanPlayer.isMachine(), "Human player should not be marked as machine");
        assertTrue(cpuPlayer.isMachine(), "CPU player should return true for isMachine()");
    }

    @Test
    void playCard_validMove_removesCard() throws InvalidMoveException {
        // Card with a small positive effect
        Card playable = new Card("5", "Hearts") {
            @Override
            public int calculateEffect(int currentSum) {
                return 5; // Always valid if sum + 5 <= 50
            }
        };
        humanPlayer.getHand().add(playable);

        Card played = humanPlayer.playCard(40);
        assertEquals(playable, played, "playCard() should return the card that was played");
        assertTrue(humanPlayer.getHand().isEmpty(), "Played card should be removed from the hand");
        assertFalse(humanPlayer.isEliminated(), "Player should not be eliminated after valid play");
    }

    @Test
    void playCard_noValidMove_eliminatesPlayer() {
        // Card that always makes sum exceed 50
        Card invalid = new Card("K", "Spades") {
            @Override
            public int calculateEffect(int currentSum) {
                return 20; // Always invalid
            }
        };
        humanPlayer.getHand().add(invalid);

        assertThrows(InvalidMoveException.class, () -> humanPlayer.playCard(40),
                "playCard() should throw InvalidMoveException when no valid moves exist");
        assertTrue(humanPlayer.isEliminated(), "Player should be marked eliminated after invalid move");
        assertTrue(humanPlayer.getHand().isEmpty(), "Invalid move should also clear the player's hand");
    }

    @Test
    void drawCard_addsToHand_whenBelowLimit() throws EmptyDeckException {
        // Simple mock deck that always returns the same card
        IDeck mockDeck = new IDeck() {
            @Override
            public void shuffle() {}
            @Override
            public Card drawCard() { return new Card("A", "Hearts"); }
            @Override
            public void addCardToBottom(Card card) {}
            @Override
            public void addCardsToBottom(Stack<Card> newCards) {}
            @Override
            public int remainingCards() { return 10; }
            @Override
            public void resetDeck(Stack<Card> recycledCards) {}
        };

        humanPlayer.drawCard(mockDeck);
        assertEquals(1, humanPlayer.getHand().size(),
                "drawCard() should add one card when below the 4-card limit");
    }

    @Test
    void drawCard_doesNothing_whenHandFull() throws EmptyDeckException {
        // Fill hand manually
        for (int i = 0; i < 4; i++) {
            humanPlayer.getHand().add(new Card("A", "Hearts"));
        }

        IDeck dummyDeck = new IDeck() {
            @Override public void shuffle() {}
            @Override public Card drawCard() { fail("Should not draw when hand is full"); return null; }
            @Override public void addCardToBottom(Card card) {}
            @Override public void addCardsToBottom(Stack<Card> newCards) {}
            @Override public int remainingCards() { return 0; }
            @Override public void resetDeck(Stack<Card> recycledCards) {}
        };

        humanPlayer.drawCard(dummyDeck);
        assertEquals(4, humanPlayer.getHand().size(),
                "drawCard() should not add more than 4 cards");
    }

    @Test
    void eliminate() {
        humanPlayer.getHand().add(new Card("2", "Hearts"));
        humanPlayer.getHand().add(new Card("3", "Clubs"));

        List<Card> remaining = humanPlayer.eliminate();

        assertTrue(humanPlayer.isEliminated(), "eliminate() should mark player as eliminated");
        assertTrue(humanPlayer.getHand().isEmpty(), "Hand should be cleared after elimination");
        assertEquals(2, remaining.size(), "Returned list should contain the previously held cards");
    }

    @Test
    void dealInitialCards() throws EmptyDeckException{
        // Mock deck that returns different cards
        IDeck mockDeck = new IDeck() {
            int counter = 0;
            @Override public void shuffle() {}
            @Override public Card drawCard() { return new Card(String.valueOf(++counter), "Spades"); }
            @Override public void addCardToBottom(Card card) {}
            @Override public void addCardsToBottom(Stack<Card> newCards) {}
            @Override public int remainingCards() { return 52 - counter; }
            @Override public void resetDeck(Stack<Card> recycledCards) {}
        };

        humanPlayer.dealInitialCards(mockDeck);
        assertEquals(4, humanPlayer.getHand().size(),
                "dealInitialCards() should ensure exactly 4 cards in hand");
    }
}