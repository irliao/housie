package com.irliao.housie.role;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Deque;

/***
 * Dealer of the bingo game.
 * Responsible for drawing a random number from a provided Deque and publishing the drawn number.
 */
public class Dealer {

    private static final String NEXT_NUMBER_PROPERTY_NAME = "NEXT_NUMBER";

    private final Deque<Integer> bingoNumbers;
    private final PropertyChangeSupport propertyChangeSupport;
    private int calledNumber;

    public Dealer(Deque<Integer> numberProvider) {
        this.bingoNumbers = numberProvider;
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    /***
     * Subscribes a listener to observe for changes to the number been called.
     * @param listener the object listening for the updates of the number been called
     */
    public void addListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(NEXT_NUMBER_PROPERTY_NAME, listener);
    }

    /***
     * Draws the next random bingo number and publishes (call) the number to the subscribers.
     * The Deque where the number is being drawn from should already be in random order.
     * @return the number being drawn and called
     */
    public int drawAndCallNextNumber() {
        int lastDrawnNumber = calledNumber;
        int newDrawnNumber = bingoNumbers.pop();
        calledNumber = newDrawnNumber;
        propertyChangeSupport.firePropertyChange(NEXT_NUMBER_PROPERTY_NAME, lastDrawnNumber, newDrawnNumber);
        return calledNumber;
    }
}
