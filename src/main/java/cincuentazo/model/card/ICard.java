package cincuentazo.model.card;

/**
 * Represents the basic behavior of any playing card in the game "Cincuentazo".
 * Defines how the card value affects the table sum.
 */
public interface ICard {

    /**
     * Returns the display value of the card (e.g., "A", "K", "5").
     */
    String getSymbol();

    /**
     * Returns the numeric value of the card.
     * Cards with letters have special values depending on the game rules.
     */
    int getNumericValue();

    /**
     * Defines how the card affects the table sum.
     * For example, "A" can add 1 or 10 depending on the game situation.
     */
    int calculateEffect(int currentSum);

    /**
     * Returns the suit of the card (e.g., "Hearts", "Spades").
     */
    String getSuit();

    /**
     * Returns a readable representation of the card (symbol + suit).
     */
    String toString();
}
