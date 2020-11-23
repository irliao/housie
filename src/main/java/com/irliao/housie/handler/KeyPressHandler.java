package com.irliao.housie.handler;

import com.irliao.housie.command.Command;

import java.util.HashMap;
import java.util.Map;

/***
 * Handles all the actions performed for the mapped key presses.
 * Commands need to be registered to this handler to it to be used.
 */
public class KeyPressHandler {

    final Map<String, Command> keyMap = new HashMap<>();

    /***
     * Registers a key press to a command into our map.
     * @param keyPress letter of the key pressed
     * @param command command to execute
     */
    public void registerCommand(String keyPress, Command command) {
        keyMap.put(keyPress, command);
    }

    /***
     * Runs the command mapped to the key pressed (if found).
     * @param keyPress
     * @throws IllegalArgumentException if the key handled is not recognized/registed
     */
    public void runCommand(String keyPress) throws IllegalArgumentException {
        if (!keyMap.containsKey(keyPress)) {
            throw new IllegalArgumentException("Invalid key press.");
        }
        keyMap.get(keyPress).execute();
    }
}
