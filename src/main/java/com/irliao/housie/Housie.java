package com.irliao.housie;

import com.irliao.housie.combination.AbstractCombination;
import com.irliao.housie.combination.EarlyFiveCombination;
import com.irliao.housie.combination.FullHouseCombination;
import com.irliao.housie.combination.TopLineCombination;
import com.irliao.housie.role.Player;

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
    }

    /***
     * Gets the map of the current winners and the combinations won.
     * @return map of winning player's ID to the Set of combinations won
     */
    Map<Integer, Set<AbstractCombination>> getCurrentWinners() {
        return currentWinners;
    }

    /***
     * Sets up the game to the specification of the Housie type bingo.
     * For Housie, we want to register 3 types of winnable combinations and also
     * create a new map for the game to keep track of all the winners.
     * Before we add the setup, we clear the current state so when play() is called
     * multiple times, each game will have a fresh/clean state.
     */
    @Override
    void setUpGame() {
        currentWinners.clear();

        winnableCombinations.clear();
        winnableCombinations.add(new EarlyFiveCombination());
        winnableCombinations.add(new FullHouseCombination());
        winnableCombinations.add(new TopLineCombination());
    }

    /***
     * Checks the current players' tickets to see if any combination has been won.
     * Once the combination has been claimed by a player, it is no longer claimable by other players.
     * Since the players are sequentially created, this method will shuffle the players each time so
     * the order of checking will be fair to all even though we are sequentially traversing the list.
     */
    @Override
    void determineWinners() {
        // shuffle the players each time before we attempt to determine the winners, this provides fairness
        // to the player so each player at each round will have equal chance of claiming the combination first
        Collections.shuffle(players);

        players.forEach(player ->
            winnableCombinations.stream()
                .filter(winCombination -> !winCombination.getClaimed()) // this would be commented out
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
    void registerWinner(int id, AbstractCombination combinationWon) {
        Set<AbstractCombination> combinationsWon = currentWinners.getOrDefault(id, new HashSet<>());
        combinationsWon.add(combinationWon);
        currentWinners.put(id, combinationsWon);
    }

    /***
     * Determines if the current game is over yet.
     * The game is over once there are no more players or no more winnable combinations left.
     * @return true if all winnable combinations are claimed
     */
    @Override
    boolean isGameInProgress() {
        boolean playerStillInGame = !players.isEmpty();
        boolean hasWinnableCombination = winnableCombinations.stream()
                                   .anyMatch(winCombination -> !winCombination.getClaimed());
        return playerStillInGame && hasWinnableCombination;
    }

    /***
     * Generates a string of current summary of the game.
     * The summary contains player ID and the combinations claimed by the player.
     * The summary should be specific to Housie type bingo.
     * Since we are shuffling the players each time we determine the winners, we need to sort
     * the list here to print the summary of players in ascending order to their IDs.
     * @return summary of the Housie game
     */
    @Override
    String generateGameSummary() {
        StringBuilder stringBuilder = new StringBuilder();
        players.stream()
               .sorted(Comparator.comparingInt(Player::getId))
               .forEach(player -> {
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
