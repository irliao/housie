package com.irliao.housie.ticket;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class TicketTest {

    private final Deque<Integer> mockBingoNumbers = Mockito.mock(Deque.class);

    @Before
    public void setup() {
        when(mockBingoNumbers.pop()).thenReturn(1);
    }

    @Test
    public void testCreateSlotsWithRandomNumbers() {
        int numOfRows = 3;
        int numOfCols = 10;
        int numPerRow = 5;
        Ticket ticket = new Ticket(numOfRows, numOfCols, mockBingoNumbers, numPerRow);
        List<List<TicketSlot>> slots = ticket.getSlots();
        List<TicketSlot> flattenedSlots = slots.stream().flatMap(List::stream).collect(Collectors.toList());

        assertEquals(numOfRows * numPerRow, flattenedSlots.stream().filter(Objects::nonNull).count());
    }
}
