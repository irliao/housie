package com.irliao.housie.bingo;

import java.util.Deque;

/***
 * Interface for object to provide a set of numbers the Dealer and Ticket will use.
 */
public interface BingoNumberProvider {

    /***
     * Creates a Deque to present the bag of numbers the Dealer and Ticket will use.
     * @return Deque of numbers
     */
    Deque<Integer> createBingoNumbers();
}
