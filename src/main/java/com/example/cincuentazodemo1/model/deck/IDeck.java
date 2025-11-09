package com.example.cincuentazodemo1.model.deck;

import com.example.cincuentazodemo1.model.exceptions.EmptyDeckException;
import com.example.cincuentazodemo1.model.card.Card;

import java.util.Stack;

/**
 * Interface that defines the basic behavior of any deck used in the game.
 */
public interface IDeck {

    /**
     * Shuffles the deck.
     */
    void shuffle();

    /**
     * Draws the top card from the deck.
     * @return the drawn Card
     * @throws EmptyDeckException if the deck is empty
     */
    Card drawCard() throws EmptyDeckException;

    /**
     * Adds one card to the bottom of the deck.
     * @param card the card to add
     */
    void addCardToBottom(Card card);

    /**
     * Adds multiple cards to the bottom of the deck.
     * @param newCards the stack of cards to add
     */
    void addCardsToBottom(Stack<Card> newCards);

    /**
     * Returns the remaining number of cards in the deck.
     */
    int remainingCards();

    /**
     * Resets the deck using another stack of cards.
     * @param recycledCards cards to reinsert and shuffle
     */
    void resetDeck(Stack<Card> recycledCards);
}
