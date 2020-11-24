package com.irliao.housie;

import com.irliao.housie.bingo.BingoNumberProvider;
import com.irliao.housie.bingo.RandomBingoNumberProvider;
import com.irliao.housie.combination.AbstractCombination;
import com.irliao.housie.command.CallCommand;
import com.irliao.housie.command.Command;
import com.irliao.housie.command.QuitCommand;
import com.irliao.housie.handler.KeyPressHandler;
import com.irliao.housie.handler.SettingHandler;
import com.irliao.housie.role.Dealer;
import com.irliao.housie.role.Player;
import com.irliao.housie.ticket.Ticket;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/***
 * Base class of Bingo for other types of Bingo games to subclass.
 * This requires the Bingo subclass to define how the winners are chosen, how the game ends, and
 * how the summary should be displayed.
 */
public abstract class AbstractBingoGame {

    public static final String KEY_TO_QUIT = "Q";
    private static final String KEY_TO_CONTINUE = "N";

    private final Map<Integer, Set<AbstractCombination>> currentWinners;

    private final Scanner inputScanner;
    private final KeyPressHandler keyPressHandler;
    private Dealer dealer;
    private BingoNumberProvider bingoNumberProvider;
    String gameName;
    List<Player> players;
    Set<AbstractCombination> winnableCombinations;

    AbstractBingoGame() {
        currentWinners = new HashMap<>();
        inputScanner = new Scanner(System.in);
        keyPressHandler = new KeyPressHandler();
    }

    /***
     * Sets up the winning combinations that are allowed in this type of bingo.
     * Requires the subclass to implement this since every type of bingo game
     * may have different combinations the ticket can win with.
     * This class will use the provided Set of combinations to determine if
     * a player's ticket is a winning ticket of that combination.
     * @return Set of combinations the ticket can win with
     */
    abstract Set<AbstractCombination> setUpCombinations();

    /***
     * Gets the map of the current winners and the combinations won.
     * @return map of winning player's ID to the Set of combinations won
     */
    Map<Integer, Set<AbstractCombination>> getCurrentWinners() {
        return currentWinners;
    }

    /***
     * Starts the bingo game application, using the implementations specified by the subclass.
     * Will first request user input to set up the game, then execute the game loop.
     */
    void play() {
        displayGameIntro();

        registerQuitCommand();

        // requests the user for inputs on how the game should be set up
        SettingHandler settingHandler = new SettingHandler(inputScanner);
        settingHandler.requestUserInputsForSetting();

        // sets up the roles needed in a bingo game
        bingoNumberProvider = new RandomBingoNumberProvider(settingHandler.getNumberRangeStart(), settingHandler.getNumberRangeEnd());
        dealer = new Dealer(bingoNumberProvider.createBingoNumbers());
        players = createAndRegisterPlayersWithTicket(settingHandler);
        winnableCombinations = setUpCombinations();

        // register this after the setup has been completed
        registerCallCommand();

        // game loop (specific implementations provided by subclass)
        while (isGameInProgress()) {
            try {
                keyPressHandler.runCommand(inputScanner.nextLine());
                determineWinners();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        displayGameSummary();
    }

    /***
     * Creates the players with tickets and registers the players to listen to Dealer calling numbers.
     * The number of player and size of ticket will be specified in the SettingHandler.
     * @param settingHandler settings from the user input
     * @return list of players participating in this bingo game.
     */
    List<Player> createAndRegisterPlayersWithTicket(SettingHandler settingHandler) {
        int ticketRowSize = settingHandler.getNumberOfRows();
        int ticketColSize = settingHandler.getNumberOfCols();
        List<Player> players = new ArrayList<>();
        IntStream.rangeClosed(1, settingHandler.getNumberOfPlayers())
            .forEach(playerId -> {
                Ticket ticket = new Ticket(ticketRowSize, ticketColSize, bingoNumberProvider.createBingoNumbers(), settingHandler.getNumberPerRow());
                Player player = new Player(playerId, ticket);
                players.add(player);
                dealer.addListener(player);
            });

        System.out.println("\n***Ticket Created Successfully ****");

        return players;
    }

    /***
     * Checks the current players' tickets to see if any combination has been won.
     * Once the combination has been claimed by a player, it is no longer claimable by other players.
     * The order at which the players are checked is sequential to how the players are created.
     */
    void determineWinners() {
        players.forEach(player ->
            winnableCombinations.stream()
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
    void registerWinner(int id, AbstractCombination combinationWon) {
        Set<AbstractCombination> combinationsWon = currentWinners.getOrDefault(id, new HashSet<>());
        combinationsWon.add(combinationWon);
        currentWinners.put(id, combinationsWon);
    }

    /***
     * Determines if the current game is over yet.
     * The game is over once there are no more winnable combinations left.
     * @return true if all winnable combinations are claimed
     */
    boolean isGameInProgress() {
        return winnableCombinations.stream()
            .anyMatch(winCombination -> !winCombination.getClaimed());
    }

    /***
     * Registers the Quit (exit the application) command to key press.
     */
    void registerQuitCommand() {
        Command quitCommand = new QuitCommand();
        keyPressHandler.registerCommand(KEY_TO_QUIT, quitCommand);
        System.out.println("Note: - Press '" + KEY_TO_QUIT + "' to quit any time.\n");
    }

    /***
     * Registers the Call (Dealer drawing and calling the next number) command to key press.
     */
    void registerCallCommand() {
        Command nextCommand = new CallCommand(dealer);
        keyPressHandler.registerCommand(KEY_TO_CONTINUE, nextCommand);
        keyPressHandler.registerCommand("", nextCommand); // NOTE: this is only registered for easy of use in demo
        System.out.println(">> Press " + KEY_TO_CONTINUE + " to generate next number.\n");
    }

    /***
     * Display the intro of the game.
     */
    void displayGameIntro() {
        System.out.println("**** Let's Play " + gameName + " *****");
        System.out.println();
    }

    /***
     * Displays the summary of the game when the game ends.
     */
    void displayGameSummary() {
        System.out.println("***** Game Over *****");
        System.out.println("======================");
        System.out.println("Summary:");
        System.out.println(generateGameSummary());
        System.out.println("=======================");
    }

    /***
     * Generates a string of current summary of the game.
     * The summary contains player ID and the combinations claimed by the player.
     * The summary should be specific to Housie type bingo.
     * @return summary of the Housie game
     */
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
