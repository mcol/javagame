package game.states;

import java.awt.Graphics;
import game.Display;
import game.Handler;
import gfx.Background;

public abstract class State {

    /** Current state. */
    private static State currentState = null;

    /** Game display. */
    protected static Display display;

    /** Object handler. */
    protected static Handler handler;

    /** Background image. */
    protected Background bg;

    /** Constructor. */
    public State(Handler handler) {
        State.handler = handler;
        display = handler.getDisplay();
    }

    public void tick() {
        bg.tick();
    }

    public abstract void render(Graphics g);

    // getters and setters

    /** Returns the current state. */
    public static State getState() {
        return currentState;
    }

    /** Sets the current state. */
    public static void setState(State state) {
        currentState = state;
        currentState.setKeyBindings();
        display.setState(State.getState());
    }

    /** Sets the key bindings for the state. */
    protected abstract void setKeyBindings();
}
