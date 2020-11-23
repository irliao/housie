package com.irliao.housie.role;

import com.irliao.housie.ticket.Ticket;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Deque;

import static org.mockito.Mockito.*;

public class DealerTest {

    private final Deque<Integer> mockNumberProvider = Mockito.mock(Deque.class);

    @Before
    public void setup() {
        when(mockNumberProvider.pop()).thenReturn(5);
    }

    @Test
    public void testDrawAndCallNextNumber() {
        Dealer dealer = new Dealer(mockNumberProvider);

        Ticket mockTicket1 = Mockito.mock(Ticket.class);
        doNothing().when(mockTicket1).markNumberIfFound(anyInt());
        Player player1 = new Player(1, mockTicket1);
        Ticket mockTicket2 = Mockito.mock(Ticket.class);
        doNothing().when(mockTicket2).markNumberIfFound(anyInt());
        Player player2 = new Player(2, mockTicket2);

        dealer.addListener(player1);
        dealer.addListener(player2);

        dealer.drawAndCallNextNumber();
        dealer.drawAndCallNextNumber();

        verify(mockTicket1, times(1)).markNumberIfFound(anyInt());
        verify(mockTicket2, times(1)).markNumberIfFound(anyInt());
    }
}
