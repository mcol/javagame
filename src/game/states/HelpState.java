package game.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import game.Handler;
import game.input.KeyManager;
import gfx.Background;
import gfx.Font;

public class HelpState extends State {

    /** Keys used in the game. */
    private static final String[] keys = { "Arrows", "Space", "H", "P", "Q" };

    /** Help message for each key. */
    private static final String[] help = { "Move",
                                           "Poop",
                                           "Help",
                                           "Pause",
                                           "Quit"
                                           };

    /** State active before entering the help state. */
    final State previousState;

    /** Constructor. */
    public HelpState(Handler handler, State previousState) {
        super(handler);
        this.previousState = previousState;
        bg = new Background("/textures/menubg.png", -0.1f,
                            handler.getGame().getWidth(),
                            handler.getGame().getHeight());
    }

    @Override
    public void render(Graphics g) {
        bg.render(g);

        // title
        Font.setColour(Color.ORANGE);
        Font.renderMessage(g, "Help", 100, 70,
                           Font.Size.LARGE, true);

        // keys
        Font.setColour(Color.WHITE);
        for (int i = 0; i < keys.length; i++)
            Font.renderMessage(g, keys[i], 100, 100 + 56 * (1 + i),
                               Font.Size.MEDIUM, true);

        // help message
        Font.setColour(Color.DARK_GRAY);
        for (int i = 0; i < help.length; i++)
            Font.renderMessage(g, help[i], 450, 100 + 56 * (1 + i),
                               Font.Size.MEDIUM, true);
    }

    @Override
    public void setKeyBindings() {
        KeyManager.addKeyBinding(display, KeyEvent.VK_H,
                                 (e) -> exit(), null);
        KeyManager.addKeyBinding(display, KeyEvent.VK_Q,
                                 (e) -> exit(), null);
        KeyManager.addKeyBinding(display, KeyEvent.VK_SPACE,
                                 (e) -> exit(), null);
        KeyManager.addKeyBinding(display, KeyEvent.VK_ENTER,
                                 (e) -> exit(), null);
        KeyManager.removeKeyBindings(display,
                                     new int[] { KeyEvent.VK_P,
                                                 KeyEvent.VK_UP,
                                                 KeyEvent.VK_DOWN});
    }

    /** Leaves the help page to return to the previous state. */
    private void exit() {
        State.setState(previousState);
    }
}
