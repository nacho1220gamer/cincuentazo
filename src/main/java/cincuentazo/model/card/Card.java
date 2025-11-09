package cincuentazo.model.card;

/**
 * Represents a playing card in the "Cincuentazo" game.
 * Implements ICard interface.
 */
public class Card implements ICard {

    private final String symbol; // "A", "2", ..., "10", "J", "Q", "K"
    private final String suit;   // "Hearts", "Diamonds", "Clubs", "Spades"

    public Card(String symbol, String suit) {
        this.symbol = symbol;
        this.suit = suit;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String getSuit() {
        return suit;
    }

    @Override
    public int getNumericValue() {
        return switch (symbol) {
            case "A" -> 1; // base value; may change in calculateEffect
            case "J", "Q", "K" -> -10;
            case "9" -> 0;
            default -> Integer.parseInt(symbol); // 2–8 or 10
        };
    }

    @Override
    public int calculateEffect(int currentSum) {
        // For Ace (A), choose 1 or 10 depending on which keeps sum <= 50
        if (symbol.equals("A")) {
            return (currentSum + 10 <= 50) ? 10 : 1;
        }
        return getNumericValue();
    }

    @Override
    public String toString() {
        return symbol + " of " + suit;
    }
}
