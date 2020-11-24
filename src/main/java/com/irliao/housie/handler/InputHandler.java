package com.irliao.housie.handler;

import com.irliao.housie.AbstractBingoGame;
import com.irliao.housie.command.Command;
import com.irliao.housie.command.QuitCommand;

import java.util.Optional;
import java.util.Scanner;
import java.util.function.Predicate;

/***
 * Handles the inputs entered by the user from stdin.
 * The inputs will be parsed and validated against a Predicate.
 * The handler will repeatedly ask for user input until user enters an input that passes the Predicate.
 * This object should be responsible of validating all inputs so the entered parameters will form a proper game.
 */
public class InputHandler {

    private final Scanner inputScanner;
    private final Command quitCommand;

    InputHandler(Scanner inputScanner) {
        this.inputScanner = inputScanner;
        quitCommand = new QuitCommand();
    }

    /***
     * Requests a number from user input via the Scanner.
     * This method will repeatedly request for user input until the input is valid.
     * The user may press the KEY_TO_QUIT (Q) to quit at any time.
     * @param promptMessage message to display when prompting for input
     * @param errorMessage message to display when the entered value is invalid
     * @param defaultValue default value to use if user enters nothing
     * @param predicate conditon the entered value must fullfill.
     * @return number entered
     */
    int requestNumber(String promptMessage, String errorMessage,
                      int defaultValue, Predicate<Integer> predicate) {
        Optional<Integer> inputIntOpt = Optional.empty();
        while (!inputIntOpt.isPresent()) {
            System.out.print(promptMessage);
            String inputString = inputScanner.nextLine();
            if (AbstractBingoGame.KEY_TO_QUIT.equalsIgnoreCase(inputString)) {
                quitCommand.execute();
            }

            try {
                if (inputString.isEmpty() || inputString.trim().isEmpty()) {
                    System.out.println("No input detected, defaulting to " + defaultValue);
                    inputIntOpt = Optional.of(defaultValue);
                } else {
                    inputIntOpt = Optional.of(Integer.parseInt(inputString));
                }

                if (!predicate.test(inputIntOpt.get())) {
                    System.out.println(errorMessage);
                    inputIntOpt = Optional.empty();
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input entered, please enter a valid number.");
            }
        }

        return inputIntOpt.get();
    }
}
