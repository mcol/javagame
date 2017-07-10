package game.states;

import java.awt.Graphics;
import game.Handler;
import gfx.Background;

public abstract class State {

    private static State currentState = null;
    protected final Handler handler;

    /** Background image. */
    protected Background bg;

    /** Constructor. */
    public State(Handler handler) {
        this.handler = handler;
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
    }

    /** Sets the key bindings for the state. */
    public abstract void setKeyBindings();
}
