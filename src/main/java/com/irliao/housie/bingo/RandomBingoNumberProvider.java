package com.irliao.housie.bingo;

import java.util.*;
import java.util.stream.IntStream;

/***
 * Provides a Deque of random numbers in the given range.
 * This is used to represent a bag of bingo numbers that the Dealer and Ticket can use.
 */
public class RandomBingoNumberProvider implements BingoNumberProvider {

    private final List<Integer> bingoNumbers;

    public RandomBingoNumberProvider(int numberRangeStart, int numberRangeEnd) {
        bingoNumbers = new LinkedList<>();
        IntStream.rangeClosed(numberRangeStart, numberRangeEnd)
                 .forEach(bingoNumbers::add);
    }

    /***
     * Creates a Deque of random numbers specified in the range.
     * @return Deque of numbers
     */
    @Override
    public Deque<Integer> createBingoNumbers() {
        Deque<Integer> randomBingoNumbers = new LinkedList<>();
        Collections.shuffle(bingoNumbers);
        bingoNumbers.forEach(randomBingoNumbers::push);

        return randomBingoNumbers;
    }
}
