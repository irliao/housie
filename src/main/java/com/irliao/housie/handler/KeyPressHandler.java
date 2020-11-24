package com.irliao.housie.handler;

import com.irliao.housie.command.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/***
 * Handles all the actions performed for the mapped key presses.
 * Commands need to be registered to this handler to it to be used.
 */
public class KeyPressHandler {

    private final Map<String, Command> keyMap = new HashMap<>();

    /***
     * Registers a key press to a command into our map.
     * @param keyPress letter string of the key press
     * @param command command to execute
     */
    public void registerCommand(String keyPress, Command command) {
        if (keyPress == null || command == null) {
            throw new IllegalArgumentException("Attempting to register invalid key press or command, both should not be null");
        }
        keyMap.put(keyPress.toLowerCase(), command);
    }

    /***
     * Runs the command mapped to the key pressed (if found).
     * @param keyPress letter string of the key press
     * @throws IllegalArgumentException if the key handled is not recognized/registered
     */
    public void runCommand(String keyPress) throws IllegalArgumentException {
        if (!keyMap.containsKey(keyPress)) {
            String validKeys = keyMap.keySet()
                                     .stream()
                                     .map(String::toUpperCase)
                                     .map(registeredKeyPress -> {
                                         if (registeredKeyPress.isEmpty()) {
                                             return "Enter";
                                         }
                                         return registeredKeyPress;
                                     })
                                     .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Invalid key press, valid keys are: " + validKeys);
        }
        keyMap.get(keyPress).execute();
    }
}
