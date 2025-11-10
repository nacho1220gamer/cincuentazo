package cincuentazo.model.exceptions;

/**
 * Exception thrown when a player attempts to make an invalid move.
 * This exception is used to indicate that the action taken by a player
 * violates the rules of the game or cannot be executed under the current conditions.
 */
public class InvalidMoveException extends Exception {

    /**
     * Constructs a new {@code InvalidMoveException} with the specified detail message.
     *
     * @param message the detail message that provides more information about the invalid move.
     */
    public InvalidMoveException(String message) {
        super(message);
    }
}