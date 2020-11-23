package com.irliao.housie.command;

/***
 * Command to execute.
 * Used to create new commands that can be registered to KeyPressHandler.
 */
public interface Command {

    /***
     * Actions to execute when the Command is called.
     * Used to map an action to a key press.
     */
    void execute();
}
