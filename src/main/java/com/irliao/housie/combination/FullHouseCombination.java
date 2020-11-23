package com.irliao.housie.combination;

import com.irliao.housie.ticket.Ticket;
import com.irliao.housie.ticket.TicketSlot;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/***
 * Object to check if a ticket has all the slots marked.
 * Maintains state on whether this combination has been claimed.
 */
public class FullHouseCombination extends AbstractCombination {

    public static final String COMBINATION_NAME = "Full House";

    public FullHouseCombination() {
        super(COMBINATION_NAME);
    }

    /***
     * Checks if the ticket has all of the slots marked.
     * @param ticket ticket to check
     * @return true if all the filled slots on the ticket are marked
     */
    @Override
    public boolean isWinningTicket(Ticket ticket) {
        List<List<TicketSlot>> slots = ticket.getSlots();
        return slots.stream()
                    .flatMap(Collection::stream)
                    .filter(Objects::nonNull)
                    .allMatch(TicketSlot::getMarked);
    }
}
