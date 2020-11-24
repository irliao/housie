package com.irliao.housie.ticket;

import java.util.*;
import java.util.stream.IntStream;

/***
 * Ticket of the bingo game.
 * This is a N rows x M columns ticket with slots.
 * Each slot has a number and boolean to indicate if slot has been marked.
 * The numbers on the slots will be random when created.
 */
public class Ticket {

    private final List<List<TicketSlot>> slots;
    private final int rowSize;
    private final int colSize;

    public Ticket(int rowSize, int colSize, Deque<Integer> bingoNumbers, int numbersPerRow) {
        this.rowSize = rowSize;
        this.colSize = colSize;
        slots = createSlotsWithRandomNumbers(bingoNumbers, numbersPerRow);
    }

    /***
     * Gets the slots of this ticket.
     * @return List of List that contains the TicketSlot wrapped in an Optional
     */
    public List<List<TicketSlot>> getSlots() {
        return slots;
    }

    /***
     * Checks the slots on the ticket for the number and marks the slot if found.
     * @param number number to search the ticket for
     */
    public void markNumberIfFound(int number) {
        slots.stream()
             .flatMap(Collection::stream)
             .filter(Objects::nonNull)
             .filter(ticketSlot -> ticketSlot.getNumber() == number)
             .findFirst()
             .ifPresent(ticketSlot -> ticketSlot.setMarked(true));
    }

    /***
     * Creates the ticket with random numbers on random slots per row.
     * The ticket is represented by a List of List since the ticket has 2 dimensional size.
     * Which slots to put a number on is chosen at random and only limits to the numbers per row specified.
     * @param bingoNumbers the Deque that will provide the numbers to fill the ticket with
     * @param numbersPerRow how many numbers should appear on each row of the ticket
     * @return ticket with random slots filled with random numbers.
     */
    List<List<TicketSlot>> createSlotsWithRandomNumbers(Deque<Integer> bingoNumbers, int numbersPerRow) {
        List<List<TicketSlot>> slots = new ArrayList<>(rowSize);
        IntStream.range(0, rowSize).forEachOrdered(row -> {
            List<TicketSlot> slotRow = new ArrayList<>(Collections.nCopies(colSize, null));
            IntStream.range(0, numbersPerRow).forEachOrdered(i -> {
                int randomNumber = bingoNumbers.pop();
                TicketSlot ticketSlotWithNumber = new TicketSlot(randomNumber);
                slotRow.set(i, ticketSlotWithNumber);
            });
            Collections.shuffle(slotRow);
            slots.add(row, slotRow);
        });

        return slots;
    }

}

