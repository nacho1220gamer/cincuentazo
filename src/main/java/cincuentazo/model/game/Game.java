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
    public IDeck getDeck() {
        return deck;
    }
    public void stop(){
        gameOver=true;
    }
    private final List<Player> players;
    private final Queue<Player> turnQueue;
    private final Stack<Card> tableCards;
    private int tableSum;
    private boolean gameOver;

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
     * Starts the game loop using threads for machine turns.
     */
    public void startGame() {
        new Thread(() -> {
            while (!gameOver) {
                try {
                    playTurn();
                    Thread.sleep(1000); // short delay for visualization
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    /**
     * Plays one complete turn (human or machine).
     */
    private void playTurn() throws EmptyDeckException, InvalidMoveException {
        Player current = turnQueue.poll();

        if (current == null || current.isEliminated()) {
            checkForWinner();
            return;
        }

        System.out.println("\n🔹 Turn: " + current.getName());
        if (current.isMachine()) {
            machinePlay(current);
        } else {
            humanPlay(current);
        }

        // Return player to queue if still active
        if (!current.isEliminated()) {
            turnQueue.add(current);
        }

        checkForWinner();
    }

    /**
     * Simulates a machine player's turn (with delay).
     */
    private void machinePlay(Player cpu) throws EmptyDeckException {
        try {
            Thread.sleep((int) (2000 + Math.random() * 2000)); // 2–4s delay
            Card played = cpu.playCard(tableSum);
            int effect = played.calculateEffect(tableSum);
            System.out.println(played);
            tableCards.push(played);
            tableSum += effect;
            cpu.drawCard(deck);
        } catch (InvalidMoveException e) {
            handleElimination(cpu, e.getMessage());
        } catch (InterruptedException ignored) {}
    }

    /**
     * Handles the human player's turn (can later connect with GUI events).
     */
    private void humanPlay(Player player) throws EmptyDeckException {
        // For now, simulate automatic play (GUI will handle in future)
        try {
            Card played = player.playCard(tableSum);
            int effect = played.calculateEffect(tableSum);
            System.out.println(played);
            tableCards.push(played);
            tableSum += effect;
            player.drawCard(deck);
        } catch (InvalidMoveException e) {
            handleElimination(player, e.getMessage());
        }
    }

    /**
     * Handles when a player is eliminated.
     */
    private void handleElimination(Player player, String reason) {
        System.out.println("Eliminated " + reason);
        List<Card> eliminatedCards = player.eliminate();
        for (Card c : eliminatedCards) {
            deck.addCardToBottom(c);
        }
    }

    /**
     * Checks if only one player remains active → declares winner.
     */
    private void checkForWinner() {
        long activePlayers = players.stream().filter(p -> !p.isEliminated()).count();

        if (activePlayers == 1) {
            Player winner = players.stream()
                    .filter(p -> !p.isEliminated())
                    .findFirst()
                    .orElse(null);
            System.out.println("\nWinner: " + (winner != null ? winner.getName() : "No one"));
            gameOver = true;
        }
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
        return tableCards.peek();
    }
}

