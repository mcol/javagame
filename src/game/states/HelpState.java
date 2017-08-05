package game.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import game.Game;
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
    public HelpState(State previousState) {
        super(handler);
        this.previousState = previousState;
        bg = new Background("/textures/menubg.png", -0.1f,
                            Game.WIDTH, Game.HEIGHT);
    }

    @Override
    public void render(Graphics g) {
        bg.render(g);

        // title
        Font.setColour(Color.ORANGE);
        Font.renderMessage(g, "Help", 40, 50,
                           Font.Size.TITLE, true);

        // keys
        Font.setColour(Color.WHITE);
        for (int i = 0; i < keys.length; i++)
            Font.renderMessage(g, keys[i], 100, 80 + 85 * (1 + i),
                               Font.Size.LARGE, true);

        // help message
        Font.setColour(Color.DARK_GRAY);
        for (int i = 0; i < help.length; i++)
            Font.renderMessage(g, help[i], 550, 80 + 85 * (1 + i),
                               Font.Size.LARGE, true);
    }

    @Override
    public void setKeyBindings() {
        KeyManager.addKeyBinding(display, KeyEvent.VK_H,
                                 (e) -> exit());
        KeyManager.addKeyBinding(display, KeyEvent.VK_Q,
                                 (e) -> exit());
        KeyManager.addKeyBinding(display, KeyEvent.VK_SPACE,
                                 (e) -> exit());
        KeyManager.addKeyBinding(display, KeyEvent.VK_ENTER,
                                 (e) -> exit());
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
