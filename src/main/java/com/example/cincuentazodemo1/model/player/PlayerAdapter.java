package com.example.cincuentazodemo1.model.player;

import com.example.cincuentazodemo1.model.exceptions.EmptyDeckException;
import com.example.cincuentazodemo1.model.exceptions.InvalidMoveException;
import com.example.cincuentazodemo1.model.card.Card;
import com.example.cincuentazodemo1.model.deck.IDeck;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class that provides default implementations of IPlayer.
 * Subclasses can override only the methods they need.
 */
public abstract class PlayerAdapter implements IPlayer {

    @Override
    public String getName() {
        return "Unnamed Player";
    }

    @Override
    public List<Card> getHand() {
        return new ArrayList<>();
    }

    @Override
    public Card playCard(int currentSum) throws InvalidMoveException {
        throw new InvalidMoveException("No implementation provided for playCard()");
    }

    @Override
    public void drawCard(IDeck deck) throws EmptyDeckException {
        // Default: do nothing
    }

    @Override
    public boolean isEliminated() {
        return false;
    }

    @Override
    public List<Card> eliminate() {
        return new ArrayList<>();
    }

    @Override
    public boolean isMachine() {
        return false;
    }
}
