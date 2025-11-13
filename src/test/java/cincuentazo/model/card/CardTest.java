package cincuentazo.model.card;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void getSymbol() {
        // Test Ace
        Card aceHearts = new Card("A", "Hearts");
        assertEquals("A", aceHearts.getSymbol());

        // Test number cards
        Card twoClubs = new Card("2", "Clubs");
        assertEquals("2", twoClubs.getSymbol());

        Card tenSpades = new Card("10", "Spades");
        assertEquals("10", tenSpades.getSymbol());

        // Test face cards
        Card jackDiamonds = new Card("J", "Diamonds");
        assertEquals("J", jackDiamonds.getSymbol());

        Card queenHearts = new Card("Q", "Hearts");
        assertEquals("Q", queenHearts.getSymbol());

        Card kingClubs = new Card("K", "Clubs");
        assertEquals("K", kingClubs.getSymbol());
    }

    @Test
    void getSuit() {
        // Test all suits
        Card heartCard = new Card("A", "Hearts");
        assertEquals("Hearts", heartCard.getSuit());

        Card diamondCard = new Card("5", "Diamonds");
        assertEquals("Diamonds", diamondCard.getSuit());

        Card clubCard = new Card("J", "Clubs");
        assertEquals("Clubs", clubCard.getSuit());

        Card spadeCard = new Card("K", "Spades");
        assertEquals("Spades", spadeCard.getSuit());
    }

    @Test
    void getNumericValue() {
        // Test Ace - should return 1 as base value
        Card ace = new Card("A", "Hearts");
        assertEquals(1, ace.getNumericValue());

        // Test number cards 2-8
        Card two = new Card("2", "Clubs");
        assertEquals(2, two.getNumericValue());

        Card five = new Card("5", "Diamonds");
        assertEquals(5, five.getNumericValue());

        Card eight = new Card("8", "Spades");
        assertEquals(8, eight.getNumericValue());

        // Test 9 - should return 0
        Card nine = new Card("9", "Hearts");
        assertEquals(0, nine.getNumericValue());

        // Test 10
        Card ten = new Card("10", "Clubs");
        assertEquals(10, ten.getNumericValue());

        // Test face cards - should return -10
        Card jack = new Card("J", "Diamonds");
        assertEquals(-10, jack.getNumericValue());

        Card queen = new Card("Q", "Hearts");
        assertEquals(-10, queen.getNumericValue());

        Card king = new Card("K", "Clubs");
        assertEquals(-10, king.getNumericValue());
    }

    @Test
    void calculateEffect() {
        // Test Ace behavior - returns 10 when currentSum + 10 <= 50
        Card ace = new Card("A", "Hearts");
        assertEquals(10, ace.calculateEffect(0));    // 0 + 10 = 10 (valid)
        assertEquals(10, ace.calculateEffect(20));   // 20 + 10 = 30 (valid)
        assertEquals(10, ace.calculateEffect(40));   // 40 + 10 = 50 (valid)

        // Test Ace behavior - returns 1 when currentSum + 10 > 50
        assertEquals(1, ace.calculateEffect(41));    // 41 + 10 = 51 (invalid, so returns 1)
        assertEquals(1, ace.calculateEffect(45));    // 45 + 10 = 55 (invalid, so returns 1)
        assertEquals(1, ace.calculateEffect(49));    // 49 + 10 = 59 (invalid, so returns 1)

        // Test number cards - should return their numeric value
        Card two = new Card("2", "Clubs");
        assertEquals(2, two.calculateEffect(0));
        assertEquals(2, two.calculateEffect(25));
        assertEquals(2, two.calculateEffect(48));

        Card five = new Card("5", "Diamonds");
        assertEquals(5, five.calculateEffect(10));
        assertEquals(5, five.calculateEffect(30));

        Card ten = new Card("10", "Spades");
        assertEquals(10, ten.calculateEffect(0));
        assertEquals(10, ten.calculateEffect(20));

        // Test 9 - should always return 0
        Card nine = new Card("9", "Hearts");
        assertEquals(0, nine.calculateEffect(0));
        assertEquals(0, nine.calculateEffect(25));
        assertEquals(0, nine.calculateEffect(49));

        // Test face cards - should always return -10
        Card jack = new Card("J", "Diamonds");
        assertEquals(-10, jack.calculateEffect(20));
        assertEquals(-10, jack.calculateEffect(40));

        Card queen = new Card("Q", "Hearts");
        assertEquals(-10, queen.calculateEffect(30));

        Card king = new Card("K", "Clubs");
        assertEquals(-10, king.calculateEffect(50));

        // Edge case: test with sum = 50 (maximum)
        assertEquals(1, ace.calculateEffect(50));
        assertEquals(2, two.calculateEffect(50));
        assertEquals(-10, jack.calculateEffect(50));
    }
}