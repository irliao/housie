package com.irliao.housie.handler;

import java.util.Scanner;

public class SettingHandler {

    private static final int DEFAULT_NUMBER_OF_PLAYERS = 5;
    private static final int DEFAULT_NUMBER_RANGE_END = 90;
    private static final int DEFAULT_NUMBER_OF_ROWS = 3;
    private static final int DEFAULT_NUMBER_OF_COLS = 10;
    private static final int DEFAULT_NUMBERS_PER_ROW = 5;

    private final int numberRangeStart = 1;
    private final InputHandler inputHandler;

    private int numberOfPlayers;
    private int numberRangeEnd;
    private int numberOfRows;
    private int numberOfCols;
    private int numberPerRow;

    public SettingHandler(Scanner inputScanner) {
        inputHandler = new InputHandler(inputScanner);
    }

    /***
     * Requests user for settings of the bingo game.
     */
    public void requestUserInputsForSetting() {
        numberRangeEnd = requestNumberRange();
        numberOfPlayers = requestNumberOfPlayers();
        numberOfRows = requestNumberOfRows();
        numberOfCols = requestNumberOfCols();
        numberPerRow = requestNumbersPerRow();
    }

    /***
     * Gets the number of players in the bingo game.
     * @return number of players
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /***
     * Gets the starting range of numbers allowed on the ticket.
     * @return starting range
     */
    public int getNumberRangeStart() {
        return numberRangeStart;
    }

    /***
     * Gets the ending range of numbers allowed on the ticket.
     * @return ending range
     */
    public int getNumberRangeEnd() {
        return numberRangeEnd;
    }

    /***
     * Gets the number of rows on the ticket.
     * @return number of rows
     */
    public int getNumberOfRows() {
        return numberOfRows;
    }

    /***
     * Gets the number of columns on the ticket.
     * @return number of columns
     */
    public int getNumberOfCols() {
        return numberOfCols;
    }

    /***
     * Gets the number of slots on the ticket to fill with a number.
     * @return numbers per row
     */
    public int getNumberPerRow() {
        return numberPerRow;
    }

    /***
     * Requests user input for a number range for allotted numbers on the bingo ticket.
     * The predicate checks greater than or equal to 6 because an edge case exists where
     * if there are only 2 rows or 2 columns, then entering 3 row or 3 columns respectively will
     * make it no longer possible to have only 5 numbers on the ticket, thus we must have 6 at the minimum.
     * @return number range
     */
    int requestNumberRange() {
        return inputHandler.requestNumber(">> Enter the number range : ",
            "Invalid number range, please enter a number >= 6",
            DEFAULT_NUMBER_RANGE_END,
            rangeNum -> rangeNum >= 6);  // greater than or equal to 6 to handle Early Five Combination
    }

    /***
     * Request user input for number of players to participate in bingo.
     * @return number of players
     */
    int requestNumberOfPlayers() {
        return inputHandler.requestNumber(">> Enter Number of players playing the game : ",
            "Invalid number of players, please enter a number > 1",
            DEFAULT_NUMBER_OF_PLAYERS,
            playerNum -> playerNum > 1);
    }

    /***
     * Request user input for number of rows to have on the ticket.
     * Number of rows must be less than the range since there is at least 1 number per row.
     * @return number of rows
     */
    int requestNumberOfRows() {
        return inputHandler.requestNumber(">> Enter Number of rows for the Ticket : ",
            "Invalid number of rows, please enter a number where 0 < number <= " + numberRangeEnd,
            DEFAULT_NUMBER_OF_ROWS,
            numOfRows -> numOfRows > 0 &&
                         numOfRows <= numberRangeEnd);
    }

    /***
     * Request user input for number of columns to have on the ticket.
     * Total size of the ticket should be at least 5 due to Early Five Combination.
     * @return number of columns
     */
    int requestNumberOfCols() {
        return inputHandler.requestNumber(">> Enter Number of columns for the Ticket : ",
            "Invalid number of columns, please enter a number where number >= " + (6 / numberOfRows),
            DEFAULT_NUMBER_OF_COLS,
            numOfCols -> (numOfCols * numberOfRows) >= 6); // greater than or equal to 6 to handle Early Five Combination
    }

    /***
     * Requests user input for number to put per row of the ticket.
     * Number per row must be less than the number of columns of the ticket.
     * Number per row must is also checked to ensure we have enough numbers to fill the ticket with and
     * can win the Early Five Combination.
     * @return numbers per row
     */
    int requestNumbersPerRow() {
        return inputHandler.requestNumber(">> Enter numbers per row : ",
            "Invalid numbers per row, please enter a number where " + (6 / numberOfRows) + " <= number <= " + Math.min(numberOfCols, (numberRangeEnd / numberOfRows)),
            DEFAULT_NUMBERS_PER_ROW,
            numPerRow -> numPerRow > 0 &&
                         numPerRow <= numberOfCols &&
                         (numPerRow * numberOfRows) <= numberRangeEnd &&
                         (numPerRow * numberOfRows) >= 5); // greater than or equal to 5 because of Early Five Combination
    }
}
