package com.irliao.housie.combination;

import com.irliao.housie.ticket.Ticket;

/***
 * Base class for a winnable combination.
 * Contains state on whether the combination has been claimed yet.
 */
public abstract class AbstractCombination {

    private final String name;
    private boolean isClaimed = false;

    public AbstractCombination(String name) {
        this.name = name;
    }

    /***
     * Gets the claim status of this combination.
     * @return true if this combination has been claimed already
     */
    public boolean getClaimed() {
        return isClaimed;
    }

    /***
     * Sets whether this combination has been claimed.
     * @param claimed whether this combination has been won and claimed already
     */
    public void setClaimed(boolean claimed) {
        this.isClaimed = claimed;
    }

    /***
     * Gets the name of this combination.
     * @return name of combination
     */
    public String getName() {
        return name;
    }

    /***
     * Checks if the ticket is a winning ticket.
     * Subclass will provide the implementation to determine if a ticket has won.
     * @param ticket ticket to check
     * @return true if the ticket has met the winning conditions
     */
    public abstract boolean isWinningTicket(Ticket ticket);
}
