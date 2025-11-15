package cincuentazo.model.exceptions;

/**
 * Exception thrown when an operation is attempted on an empty deck.
 * This exception is used to indicate that a game action requiring cards
 * cannot proceed because the deck has no remaining cards.
 */
public class EmptyDeckException extends Exception {

    /**
     * Constructs a new {@code EmptyDeckException} with the specified detail message.
     *
     * @param message the detail message that provides more information about the error.
     */
    public EmptyDeckException(String message) {
        super(message);
    }
}
