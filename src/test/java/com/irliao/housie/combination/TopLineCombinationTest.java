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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class TopLineCombinationTest {

    private final AbstractCombination topLineCombination = new TopLineCombination();
    private final Ticket mockTicket = Mockito.mock(Ticket.class);
    private final List<List<TicketSlot>> slots = new ArrayList<>();

    @Before
    public void setup() {
        // initialize ticket
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
    public void testIsWinningTicket_false_secondLineMarked() {
        IntStream.range(0, 10).forEach(i -> slots.get(1).get(i).setMarked(true));

        assertFalse(topLineCombination.isWinningTicket(mockTicket));
    }

    @Test
    public void testIsWinningTicket_false_topLineMissing1() {
        IntStream.range(0, 9).forEach(i -> {
            slots.get(1).get(i).setMarked(true);
            slots.get(0).get(i).setMarked(true);
        });

        assertFalse(topLineCombination.isWinningTicket(mockTicket));
    }

    @Test
    public void testIsWinningTicket_true() {
        // set all slots to marked
        IntStream.range(0, 10).forEach(i -> slots.get(0).get(i).setMarked(true));

        assertTrue(topLineCombination.isWinningTicket(mockTicket));
    }
}
