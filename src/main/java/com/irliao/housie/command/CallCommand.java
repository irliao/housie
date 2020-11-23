package com.irliao.housie.command;

import com.irliao.housie.role.Dealer;

/***
 * Command to run to have the Dealer call the next number.
 */
public class CallCommand implements Command {

    private final Dealer dealer;

    public CallCommand(Dealer dealer) {
        this.dealer = dealer;
    }

    /***
     * Have the Dealer call the next number.
     * Also prints the number called by the Dealer.
     */
    @Override
    public void execute() {
        int drawnNumber = dealer.drawAndCallNextNumber();
        System.out.println("Next number is: " + drawnNumber + " ");
    }
}
