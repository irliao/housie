package com.irliao.housie.combination;

import com.irliao.housie.ticket.Ticket;
import com.irliao.housie.ticket.TicketSlot;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Object to check if a ticket has 5 slots marked.
 * Maintains state on whether this combination has been claimed.
 */
public class EarlyFiveCombination extends AbstractCombination {

    public static final String COMBINATION_NAME = "Early Five";

    public EarlyFiveCombination() {
        super(COMBINATION_NAME);
    }

    /***
     * Checks if the ticket has any of the 5 slots marked.
     * @param ticket ticket to check
     * @return true if there are at least 5 slots marked on the ticket
     */
    @Override
    public boolean isWinningTicket(Ticket ticket) {
        List<List<TicketSlot>> slots = ticket.getSlots();
        return slots.stream()
                    .flatMap(Collection::stream)
                    .filter(Objects::nonNull)
                    .filter(TicketSlot::getMarked)
                    .count() >= 5;
    }
}
