package com.example.cincuentazodemo1.model.exceptions;

/**
 * Custom exception thrown when a player cannot make a valid move.
 */
public class InvalidMoveException extends Exception {
    public InvalidMoveException(String message) {
        super(message);
    }
}