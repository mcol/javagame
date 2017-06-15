package game.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyManager extends KeyAdapter {

    private boolean[] keys;
    public boolean up, down, left, right, space;

    /** Constructor. */
    public KeyManager() {
        keys = new boolean[100];
    }

    public void tick() {
        up = keys[KeyEvent.VK_UP];
        down = keys[KeyEvent.VK_DOWN];
        left = keys[KeyEvent.VK_LEFT];
        right = keys[KeyEvent.VK_RIGHT];
        space = keys[KeyEvent.VK_SPACE];
    }

    @Override
    public void keyPressed(KeyEvent event) {
        try{
            keys[event.getKeyCode()] = true;
        } catch (ArrayIndexOutOfBoundsException e) {}
    }

    @Override
    public void keyReleased(KeyEvent event) {
        try {
            keys[event.getKeyCode()] = false;
        } catch (ArrayIndexOutOfBoundsException e) {}
    }
}
