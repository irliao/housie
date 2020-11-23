package com.irliao.housie.combination;

import com.irliao.housie.ticket.Ticket;
import com.irliao.housie.ticket.TicketSlot;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.when;

public class EarlyFiveCombinationTest {

    private final AbstractCombination earlyFiveCombination = new EarlyFiveCombination();
    private final Ticket mockTicket = Mockito.mock(Ticket.class);
    private final List<List<TicketSlot>> slots = new ArrayList<>();

    @Before
    public void setup() {
        // initialize ticket with empty slots
        slots.clear();
        List<TicketSlot> row1 = new ArrayList<>(Collections.nCopies(15, null));
        List<TicketSlot> row2 = new ArrayList<>(Collections.nCopies(15, null));
        // add slots with numbers
        IntStream.range(0, 10).forEach(i -> {
            row1.set(i, new TicketSlot(i));
            row2.set(i, new TicketSlot(i));
        });

        slots.add(row1);
        slots.add(row2);

        when(mockTicket.getSlots()).thenReturn(slots);
    }

    @Test
    public void testIsWinningTicket_false() {
        // set only 4 slots to marked
        IntStream.range(0, 2).forEach(i -> {
            slots.get(0).get(i).setMarked(true);
            slots.get(1).get(i).setMarked(true);
        });

        assertFalse(earlyFiveCombination.isWinningTicket(mockTicket));
    }

    @Test
    public void testIsWinningTicket_true() {
        // set 6 slots to be marked
        IntStream.range(0, 3).forEach(i -> {
            slots.get(0).get(i).setMarked(true);
            slots.get(1).get(i).setMarked(true);
        });

        assertTrue(earlyFiveCombination.isWinningTicket(mockTicket));
    }
}
