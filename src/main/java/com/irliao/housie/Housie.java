package com.irliao.housie;

import com.irliao.housie.combination.AbstractCombination;
import com.irliao.housie.combination.EarlyFiveCombination;
import com.irliao.housie.combination.FullHouseCombination;
import com.irliao.housie.combination.TopLineCombination;

import java.util.*;
import java.util.stream.Collectors;

/***
 * An implementation of the Housie game.
 * This class implements methods specified from AbstractBingoGame with logic that is specific to Housie.
 */
public class Housie extends AbstractBingoGame {

    private final Set<AbstractCombination> winnableCombinations;
    private final Map<Integer, Set<AbstractCombination>> currentWinners;

    public Housie() {
        gameName = "Housie";
        currentWinners = new HashMap<>();

        winnableCombinations = new HashSet<>();
        winnableCombinations.add(new EarlyFiveCombination());
        winnableCombinations.add(new FullHouseCombination());
        winnableCombinations.add(new TopLineCombination());
    }

    Map<Integer, Set<AbstractCombination>> getCurrentWinners() {
        return currentWinners;
    }

    /***
     * Checks the current players' tickets to see if any combination has been won.
     * Once the combination has been claimed by a player, it is no longer claimable by other players.
     * The order at which the players are checked is sequential to how the players are created.
     */
    @Override
    void determineWinners() {
        players.forEach(player -> winnableCombinations.stream()
            .filter(winCombination -> !winCombination.getClaimed())
            .forEach(winCombination ->  {
                if (winCombination.isWinningTicket(player.getTicket())) {
                    System.out.println("\n");
                    System.out.println("We have a winner: Player#" + player.getId() + " has won " + "'" + winCombination.getName() + "'" + " winning combination.");
                    registerWinner(player.getId(), winCombination);
                    winCombination.setClaimed(true);
                }
            }));
    }

    /***
     * Registers the player's ID and the player's win combinations into a map.
     * This method should be called when a new winner has been determined.
     * @param id id of the player who had a winning ticket for the combination
     * @param combinationWon the combination the player has won
     */
    public void registerWinner(int id, AbstractCombination combinationWon) {
        Set<AbstractCombination> combinationsWon = currentWinners.getOrDefault(id, new HashSet<>());
        combinationsWon.add(combinationWon);
        currentWinners.put(id, combinationsWon);
    }

    /***
     * Determines if the current game is over yet.
     * The game is over once there are no more winnable combinations left.
     * @return true if all winnable combinations are claimed
     */
    @Override
    boolean isGameInProgress() {
        return winnableCombinations.stream()
                                   .anyMatch(winCombination -> !winCombination.getClaimed());
    }

    /***
     * Generates a string of current summary of the game.
     * The summary contains player ID and the combinations claimed by the player.
     * The summary should be specific to Housie type bingo.
     * @return summary of the Housie game
     */
    @Override
    String generateGameSummary() {
        StringBuilder stringBuilder = new StringBuilder();
        players.forEach(player -> {
            Set<AbstractCombination> winCombinations = currentWinners.getOrDefault(player.getId(), new HashSet<>());
            List<String> winningCombinationsString = winCombinations.stream()
                .map(AbstractCombination::getName)
                .collect(Collectors.toList());
            boolean isWinningTicket = !winningCombinationsString.isEmpty();
            String playerSummary = isWinningTicket ? String.join(" and ", winningCombinationsString) : "Nothing";
            stringBuilder.append("Player#").append(player.getId()).append(" : ").append(playerSummary).append("\n");
        });

        return stringBuilder.toString();
    }
}
