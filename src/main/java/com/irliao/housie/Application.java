package com.irliao.housie;

/***
 * Application running the Housie game.
 */
public class Application {
    // main driver of the Housie game
    public static void main(String... args) {
        Housie housie = new Housie();
        housie.play();
    }
}
