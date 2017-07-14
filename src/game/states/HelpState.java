package game.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import game.Handler;
import game.input.KeyManager;
import gfx.Background;

public class HelpState extends State {

    private static final Font titleFont = new Font("Century Gothic", Font.BOLD, 96);
    private static final Font font = new Font("Century Gothic", Font.BOLD, 48);

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
        g.setFont(titleFont);
        g.setColor(Color.ORANGE);
        g.drawString("Help", 100, 125);

        // keys
        g.setFont(font);
        for (int i = 0; i < keys.length; i++) {
            g.setColor(Color.WHITE);
            g.drawString(keys[i], 100, 140 + 56 * (1 + i));
            g.setColor(Color.DARK_GRAY);
            g.drawString(help[i], 350, 140 + 56 * (1 + i));
        }
    }

    @Override
    public void setKeyBindings() {
        KeyManager.addKeyBinding(handler.getFrame(), KeyEvent.VK_H,
                                 (e) -> exit(), null);
        KeyManager.addKeyBinding(handler.getFrame(), KeyEvent.VK_Q,
                                 (e) -> exit(), null);
        KeyManager.addKeyBinding(handler.getFrame(), KeyEvent.VK_SPACE,
                                 (e) -> exit(), null);
        KeyManager.addKeyBinding(handler.getFrame(), KeyEvent.VK_ENTER,
                                 (e) -> exit(), null);
        KeyManager.removeKeyBindings(handler.getFrame(),
                                     new int[] { KeyEvent.VK_P,
                                                 KeyEvent.VK_UP,
                                                 KeyEvent.VK_DOWN});
    }

    /** Leaves the help page to return to the previous state. */
    private void exit() {
        State.setState(previousState);
    }
}
