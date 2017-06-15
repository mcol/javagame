package game.states;

import java.awt.Graphics;

import game.Handler;

public abstract class State {

    private static State currentState = null;
    protected Handler handler;

    /** Constructor. */
    public State(Handler handler) {
        this.handler = handler;
    }

    public abstract void tick();
    public abstract void render(Graphics g);

    // getters and setters

    /** Returns the current state */
    public static State getState() {
        return currentState;
    }

    public static void setState(State state) {
        currentState = state;
    }
}
