package com.irliao.housie;

import com.irliao.housie.bingo.BingoNumberProvider;
import com.irliao.housie.combination.AbstractCombination;
import com.irliao.housie.combination.EarlyFiveCombination;
import com.irliao.housie.combination.FullHouseCombination;
import com.irliao.housie.combination.TopLineCombination;
import com.irliao.housie.handler.SettingHandler;
import com.irliao.housie.role.Dealer;
import com.irliao.housie.role.Player;
import com.irliao.housie.ticket.Ticket;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class HousieTest {

    private final AbstractCombination mockFullHouseCombination = Mockito.mock(FullHouseCombination.class);
    private final AbstractCombination mockTopLineCombination = Mockito.mock(TopLineCombination.class);
    private final AbstractCombination mockEarlyFiveCombination = Mockito.mock(EarlyFiveCombination.class);

    private final Ticket mockTicket = Mockito.mock(Ticket.class);

    private final Set<AbstractCombination> winnableCombinations = new HashSet<>();
    private final BingoNumberProvider mockBingoNumberProvider = Mockito.mock(BingoNumberProvider.class);
    private final Deque<Integer> mockBingoNumbers = Mockito.mock(Deque.class);
    private final Dealer mockDealer = Mockito.mock(Dealer.class);

    Housie housie = new Housie();

    @Before
    public void setup() {
        doNothing().when(mockDealer).addListener(any(Player.class));
        ReflectionTestUtils.setField(housie, "dealer", mockDealer);

        when(mockBingoNumbers.pop()).thenReturn(1);
        when(mockBingoNumberProvider.createBingoNumbers()).thenReturn(mockBingoNumbers);
        ReflectionTestUtils.setField(housie, "bingoNumberProvider", mockBingoNumberProvider);

        when(mockFullHouseCombination.getName()).thenReturn(FullHouseCombination.COMBINATION_NAME);
        when(mockTopLineCombination.getName()).thenReturn(TopLineCombination.COMBINATION_NAME);
        when(mockEarlyFiveCombination.getName()).thenReturn(EarlyFiveCombination.COMBINATION_NAME);

        winnableCombinations.add(mockFullHouseCombination);
        winnableCombinations.add(mockEarlyFiveCombination);
        winnableCombinations.add(mockTopLineCombination);

        ReflectionTestUtils.setField(housie, "winnableCombinations", winnableCombinations);
    }

    @Test
    public void testIsGameInProgress_false() {
        when(mockFullHouseCombination.getClaimed()).thenReturn(true);
        when(mockTopLineCombination.getClaimed()).thenReturn(true);
        when(mockEarlyFiveCombination.getClaimed()).thenReturn(true);

        assertFalse(housie.isGameInProgress());
    }

    @Test
    public void testIsGameInProgress_true() {
        when(mockFullHouseCombination.getClaimed()).thenReturn(false);
        when(mockTopLineCombination.getClaimed()).thenReturn(true);
        when(mockEarlyFiveCombination.getClaimed()).thenReturn(true);

        assertTrue(housie.isGameInProgress());
    }

    @Test
    public void testDetermineWinners() {
        when(mockFullHouseCombination.getClaimed()).thenReturn(false).thenReturn(true);
        when(mockTopLineCombination.getClaimed()).thenReturn(false).thenReturn(true);
        when(mockEarlyFiveCombination.getClaimed()).thenReturn(false).thenReturn(true);

        when(mockFullHouseCombination.isWinningTicket(any(Ticket.class))).thenReturn(true);
        when(mockTopLineCombination.isWinningTicket(any(Ticket.class))).thenReturn(true);
        when(mockEarlyFiveCombination.isWinningTicket(any(Ticket.class))).thenReturn(false);

        // using real objects instead of Mock so we can successfully add to the
        // list of won combinations for assertion
        Player player1 = new Player(1, mockTicket);
        Player player2 = new Player(2, mockTicket);

        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        ReflectionTestUtils.setField(housie, "players", players);

        housie.determineWinners();
        Map<Integer, Set<AbstractCombination>> playerIdWinMap = housie.getCurrentWinners();

        assertEquals(1, playerIdWinMap.keySet().size());
        assertEquals(2, playerIdWinMap.get(1).size());
    }

    @Test
    public void testGenerateGameSummary() {
        Player player1 = new Player(1, mockTicket);
        housie.registerWinner(player1.getId(), mockTopLineCombination);
        housie.registerWinner(player1.getId(), mockFullHouseCombination);

        Player player2 = new Player(2, mockTicket);
        housie.registerWinner(player2.getId(), mockEarlyFiveCombination);

        Player player3 = new Player(3, mockTicket);

        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);

        ReflectionTestUtils.setField(housie, "players", players);

        String summaryString = housie.generateGameSummary();
        System.out.println(summaryString);
        assertTrue(summaryString.contains("Player#1 : Full House and Top Line") ||
                   summaryString.contains("Player#1 : Top Line and Full House"));
        assertTrue(summaryString.contains("Player#2 : Early Five"));
        assertTrue(summaryString.contains("Player#3 : Nothing"));
    }

    @Test
    public void testCreateAndRegisterPlayersWithTicket() {
        SettingHandler mockSettingHandler = Mockito.mock(SettingHandler.class);
        when(mockSettingHandler.getNumberOfRows()).thenReturn(3);
        when(mockSettingHandler.getNumberOfCols()).thenReturn(10);
        when(mockSettingHandler.getNumberOfPlayers()).thenReturn(5);
        when(mockSettingHandler.getNumberPerRow()).thenReturn(5);

        List<Player> playersWithTicket = housie.createAndRegisterPlayersWithTicket(mockSettingHandler);
        assertEquals(5, playersWithTicket.size());
    }
}
