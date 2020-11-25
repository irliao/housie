package com.irliao.housie;

import com.irliao.housie.bingo.BingoNumberProvider;
import com.irliao.housie.bingo.RandomBingoNumberProvider;
import com.irliao.housie.command.CallCommand;
import com.irliao.housie.command.Command;
import com.irliao.housie.command.QuitCommand;
import com.irliao.housie.handler.KeyPressHandler;
import com.irliao.housie.handler.SettingHandler;
import com.irliao.housie.role.Dealer;
import com.irliao.housie.role.Player;
import com.irliao.housie.ticket.Ticket;

import java.util.*;
import java.util.stream.IntStream;

/***
 * Base class of Bingo for other types of Bingo games to subclass.
 * This requires the Bingo subclass to define how the winners are chosen, how the game ends, and
 * how the summary should be displayed.
 */
public abstract class AbstractBingoGame {

    public static final String KEY_TO_QUIT = "Q"; // both Q and q will be registered as Quit command
    private static final String KEY_TO_CONTINUE = "N"; // both N and n will be registered as Continue command

    private final Scanner inputScanner;
    private final KeyPressHandler keyPressHandler;
    private Dealer dealer;
    private BingoNumberProvider bingoNumberProvider;
    String gameName;
    List<Player> players;

    public AbstractBingoGame() {
        inputScanner = new Scanner(System.in);
        keyPressHandler = new KeyPressHandler();
    }

    /***
     * Sets up the game to how a specific subclass bingo game type should behave.
     * This is called in the beginning of play() to initialize the objects needed and
     * to reinitialize to a fresh/clean state when play() is called multiple times.
     */
    abstract void setUpGame();

    /***
     * Define how the winners will be determined.
     */
    abstract void determineWinners();

    /***
     * Defines whether the game is still in progress.
     * @return true if game still in progress (in loop)
     */
    abstract boolean isGameInProgress();

    /***
     * Defines the summary of the game.
     * This summary is to be generated and displayed once the game is over.
     * @return summary of the game
     */
    abstract String generateGameSummary();

    /***
     * Starts the bingo game application, using the implementations specified by the subclass.
     * Will first request user input to set up the game, then execute the game loop.
     * Each time the play() is called, the subclass needs to properly handle any state
     * cleanup via implementing the setUpGame() as this method may be called more than once.
     */
    public void play() {
        displayGameIntro();

        // registering here because we want to print the instruction to Quit after the intro
        // otherwise this could go to the constructor (though displayGameIntro() doesn't make
        // sense to be called in the constructor)
        registerQuitCommand();

        // requests the user for inputs on how the game should be set up
        SettingHandler settingHandler = new SettingHandler(inputScanner);
        settingHandler.requestUserInputsForSetting();

        // sets up the roles needed in a bingo game
        bingoNumberProvider = new RandomBingoNumberProvider(settingHandler.getNumberRangeStart(), settingHandler.getNumberRangeEnd());
        dealer = new Dealer(bingoNumberProvider.createBingoNumbers());
        players = createAndRegisterPlayersWithTicket(settingHandler);

        // sets up game to the implementation provided by the subclass
        setUpGame();

        // register this after the setup has been completed
        registerCallCommand();

        // game loop (specific implementations provided by subclass)
        do {
            try {
                keyPressHandler.runCommand(inputScanner.nextLine());
                determineWinners();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        while (isGameInProgress());

        displayGameSummary();

        printAllTickets();
    }

    /***
     * Creates the players with tickets and registers the players to listen to Dealer calling numbers.
     * The number of player and size of ticket will be specified in the SettingHandler.
     * The list of players here will be sequentially generated, with ID from 1 to number of players.
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
     * Prints every players' ticket, ordered by player's ID
     */
    void printAllTickets() {
        players.stream()
               .sorted(Comparator.comparingInt(Player::getId))
               .forEach(player -> {
                    System.out.println("Player#" + player.getId() + ":");
                    player.getTicket().printTicket();
                    System.out.println(" ");
               });
    }
}
