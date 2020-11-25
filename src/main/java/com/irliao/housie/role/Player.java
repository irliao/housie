package com.irliao.housie.role;

import com.irliao.housie.ticket.Ticket;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/***
 * Player of the bingo game.
 * The player has a ticket and is responsible for marking the ticket when notified.
 * The player implements PropertyChangeListener so it can subscribe to the Dealer drawing number.
 */
public class Player implements PropertyChangeListener {

    private final int id;
    private final Ticket ticket;

    public Player(int id, Ticket ticket) {
        this.id = id;
        this.ticket = ticket;
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
