package com.irliao.housie;

import com.irliao.housie.combination.AbstractCombination;
import com.irliao.housie.combination.EarlyFiveCombination;
import com.irliao.housie.combination.FullHouseCombination;
import com.irliao.housie.combination.TopLineCombination;

import java.util.HashSet;
import java.util.Set;

/***
 * An implementation of the Housie game.
 * This class provides the winning combinations the Base bingo class will use to determine winner
 * and determine when the game is over.
 */
class Housie extends AbstractBingoGame {
    Housie() {
        gameName = "Housie";
    }

    /***
     * Sets up the winning combinations that are allowed in this type of bingo.
     * Housie specifically allows: Early Five, Full House, and Top Line.
     * @return Set of combinations the ticket can win with
     */
    @Override
    Set<AbstractCombination> setUpCombinations() {
        Set<AbstractCombination> winnableCombinations = new HashSet<>();
        winnableCombinations.add(new EarlyFiveCombination());
        winnableCombinations.add(new FullHouseCombination());
        winnableCombinations.add(new TopLineCombination());
        return winnableCombinations;
    }
}
