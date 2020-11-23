package com.irliao.housie.ticket;

/***
 * Slot on the ticket with a number and a boolean to indicate if slot is marked.
 */
public class TicketSlot {

    private final int number;
    private boolean marked = false;

    public TicketSlot(int number) {
        this.number = number;
    }

    /***
     * Sets whether this slot has been marked off.
     * @param marked status of this slot
     */
    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    /***
     * Gets whether this slot has been marked off.
     * @return true if number on this slot has been called
     */
    public boolean getMarked() {
        return marked;
    }

    /***
     * Gets the number this slot is holding
     * @return number on this slot
     */
    public int getNumber() {
        return number;
    }
}
