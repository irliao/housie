package com.irliao.housie.combination;

import com.irliao.housie.ticket.Ticket;
import com.irliao.housie.ticket.TicketSlot;

import java.util.List;
import java.util.Objects;

/***
 * Object to check if the ticket's top row has all filled slots marked.
 * Maintains state on whether this combination has been claimed.
 */
public class TopLineCombination extends AbstractCombination {

    public static final String COMBINATION_NAME = "Top Line";

    public TopLineCombination() {
        super(COMBINATION_NAME);
    }

    /***
     * Checks if the ticket has all the filled slots on top row marked.
     * @param ticket ticket to check
     * @return true if all the filled slots on the top row are marked
     */
    @Override
    public boolean isWinningTicket(Ticket ticket) {
        List<TicketSlot> topLine = ticket.getSlots().get(0);
        return topLine.stream()
                      .filter(Objects::nonNull)
                      .allMatch(TicketSlot::getMarked);
    }
}
