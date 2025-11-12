package cincuentazo.model.game;


import cincuentazo.model.exceptions.EmptyDeckException;
import cincuentazo.model.exceptions.InvalidMoveException;
import cincuentazo.model.card.Card;
import cincuentazo.model.deck.Deck;
import cincuentazo.model.deck.IDeck;
import cincuentazo.model.player.Player;

import java.util.*;

/**
 * Represents the main game logic of "Cincuentazo".
 * Controls players, turns, table sum, and game flow.
 */
public class Game {

    private final IDeck deck;
    private final List<Player> players;
    private final Queue<Player> turnQueue;
    private final Stack<Card> tableCards;
    private int tableSum;
    private boolean gameOver;

    private int currentPlayerIndex;

    public Game(int numMachines) throws EmptyDeckException {
        this.deck = new Deck();
        this.players = new ArrayList<>();
        this.turnQueue = new LinkedList<>();
        this.tableCards = new Stack<>();
        this.tableSum = 0;
        this.gameOver = false;

        initializePlayers(numMachines);
        dealInitialCards();
    }

    /**
     * Initializes the human player and machine players.
     */
    private void initializePlayers(int numMachines) {
        Player human = new Player("You", false);
        players.add(human);
        turnQueue.add(human);

        for (int i = 1; i <= numMachines; i++) {
            Player machine = new Player("CPU-" + i, true);
            players.add(machine);
            turnQueue.add(machine);
        }
    }

    /**
     * Deals 4 cards to each player and places one card on the table.
     */
    private void dealInitialCards() throws EmptyDeckException {
        for (Player p : players) {
            p.dealInitialCards(deck);
        }

        // First card on table
        Card firstCard = deck.drawCard();
        tableCards.push(firstCard);
        tableSum += firstCard.calculateEffect(0);
        System.out.println("Starting card: " + firstCard + " → Table sum: " + tableSum);
    }

    /**
     * Gets the current player whose turn it is.
     * @return current player or null if no active players
     */
    public Player getCurrentPlayer() {
        List<Player> activePlayers = getActivePlayers();

        if (activePlayers.isEmpty()) {
            return null;
        }

        if (currentPlayerIndex >= activePlayers.size()) {
            currentPlayerIndex = 0;
        }

        return activePlayers.get(currentPlayerIndex);
    }

    /**
     * Gets all players that are not eliminated.
     */
    public List<Player> getActivePlayers() {
        return players.stream()
                .filter(p -> !p.isEliminated())
                .toList();
    }

    /**
     * Advances to the next player's turn.
     */
    public void advanceTurn() {
        currentPlayerIndex++;

        // Check for winner after each turn
        if (getActivePlayers().size() == 1) {
            gameOver = true;
        }
    }

    /**
     * Validates if a card can be played without exceeding 50.
     * @param card the card to validate
     * @return true if the card is valid, false otherwise
     */
    public boolean isValidMove(Card card) {
        int effect = card.calculateEffect(tableSum);
        int newSum = tableSum + effect;
        return newSum <= 50;
    }

    /**
     * Checks if a player has any valid cards to play.
     * @param player the player to check
     * @return true if player has at least one valid card
     */
    public boolean hasValidCards(Player player) {
        return player.getHand().stream()
                .anyMatch(this::isValidMove);
    }

    /**
     * Plays a card for a player (human or machine).
     * Updates the table sum and removes the card from player's hand.
     * @param player the player playing the card
     * @param card the card to play
     * @throws InvalidMoveException if the move is invalid
     */
    public void playCard(Player player, Card card) throws InvalidMoveException {
        if (!isValidMove(card)) {
            throw new InvalidMoveException("Card would exceed 50");
        }

        // Remove card from player's hand
        player.getHand().remove(card);

        // Update table
        int effect = card.calculateEffect(tableSum);
        tableCards.push(card);
        tableSum += effect;

        System.out.println(player.getName() + " played " + card + " → Table sum: " + tableSum);
    }

    /**
     * Executes a machine player's turn.
     * Tries to play a valid card, or gets eliminated.
     * @param cpu the machine player
     * @return the card played, or null if eliminated
     */
    public Card executeMachineTurn(Player cpu) {
        try {
            // Machine strategy: play a valid card
            Card played = cpu.playCard(tableSum);

            // Play the card
            playCard(cpu, played);

            // Draw new card
            try {
                cpu.drawCard(deck);
            } catch (EmptyDeckException e) {
                recycleDeck();
            }

            return played;

        } catch (InvalidMoveException e) {
            // Machine has no valid cards - eliminate
            eliminatePlayer(cpu);
            return null;
        }
    }

    /**
     * Attempts to play a card for the human player.
     * @param player the human player
     * @param card the card selected
     * @throws InvalidMoveException if the card cannot be played
     */
    public void executeHumanPlay(Player player, Card card) throws InvalidMoveException {
        if (!hasValidCards(player)) {
            // No valid cards - player must be eliminated
            eliminatePlayer(player);
            throw new InvalidMoveException("No valid cards to play");
        }

        // Play the card
        playCard(player, card);
    }

    /**
     * Handles drawing a card for the human player.
     * @param player the human player
     */
    public void executeHumanDraw(Player player) {
        try {
            player.drawCard(deck);
        } catch (EmptyDeckException e) {
            recycleDeck();
        }
    }

    /**
     * Eliminates a player and returns their cards to the deck.
     * @param player the player to eliminate
     */
    public void eliminatePlayer(Player player) {
        List<Card> eliminatedCards = player.eliminate();

        // Return cards to bottom of deck
        for (Card card : eliminatedCards) {
            deck.addCardToBottom(card);
        }

        System.out.println(player.getName() + " has been eliminated!");

        // Check if game is over
        if (getActivePlayers().size() == 1) {
            gameOver = true;
        }
    }

    /**
     * Recycles the table cards back into the deck when it's empty.
     */
    private void recycleDeck() {
        if (tableCards.size() > 1) {
            Card topCard = tableCards.pop(); // Keep top card

            // Shuffle remaining cards back into deck
            List<Card> cardsToRecycle = new ArrayList<>(tableCards);
            Collections.shuffle(cardsToRecycle);

            for (Card card : cardsToRecycle) {
                deck.addCardToBottom(card);
            }

            tableCards.clear();
            tableCards.push(topCard);

            System.out.println("Deck recycled! Cards returned from table.");
        }
    }

    /**
     * Gets the winner of the game (if any).
     * @return the winning player, or null if no winner yet
     */
    public Player getWinner() {
        List<Player> active = getActivePlayers();
        return active.size() == 1 ? active.get(0) : null;
    }

    public IDeck getDeck() {
        return deck;
    }

    public void stop(){
        gameOver=true;
    }

    public void addTableSum(int value) {
        tableSum += value;
    }

    public int getTableSum() {
        return tableSum;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setTopCard(Card card) { tableCards.push(card); }

    public Card getTopCard() {
        return tableCards.isEmpty() ? null : tableCards.peek();
    }
}

