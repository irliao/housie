package com.irliao.housie.command;

/***
 * Command to run to exit the application.
 */
public class QuitCommand implements Command {

    private static final int EXIT_CODE = 0;

    /***
     * Exists the application with the exit code.
     */
    @Override
    public void execute() {
        System.exit(EXIT_CODE);
    }
}
