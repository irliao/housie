package com.irliao.housie.role;

import com.irliao.housie.combination.AbstractCombination;
import com.irliao.housie.ticket.Ticket;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

/***
 * Player of the bingo game.
 * The player has a ticket and keeps track of the combinations currently won.
 * The player implements PropertyChangeListener so it can subscribe to the Dealer drawing number.
 */
public class Player implements PropertyChangeListener {

    private final Set<AbstractCombination> combinationsWon;
    private final int id;
    private final Ticket ticket;

    public Player(int id, Ticket ticket) {
        this.id = id;
        this.ticket = ticket;
        combinationsWon = new HashSet<>();
    }

    /***
     * Gets the set of combinations won for the current ticket.
     * @return set of combinations won
     */
    public Set<AbstractCombination> getCombinationsWon() {
        return combinationsWon;
    }

    /***
     * Adds a win combination to the set of combinations currently won.
     * @param winCombination combination the ticket has won
     */
    public void addWinningCombination(AbstractCombination winCombination) {
        combinationsWon.add(winCombination);
    }

    /***
     * Gets the ID of the player.
     * @return player's ID
     */
    public int getId() {
        return id;
    }

    /***
     * Gets the bingo ticket of the player.
     * @return player's ticket
     */
    public Ticket getTicket() {
        return ticket;
    }

    /***
     * Action performed when the Dealer we subscribe to draws a new number.
     * This requires the Dealer to add this Player as listener before this will receive event publication.
     * @param event event published when the Dealer draws a new number.
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        ticket.markNumberIfFound((Integer)(event.getNewValue()));
    }
}
