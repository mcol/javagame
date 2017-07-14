package game.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import game.Handler;
import game.input.KeyManager;
import gfx.Background;

public class MenuState extends State {

    private static final String[] options = { "Start", "Help", "Quit" };
    private static final Font titleFont = new Font("Century Gothic", Font.BOLD, 96);
    private static final Font font = new Font("Century Gothic", Font.BOLD, 72);

    /** Current menu choice. */
    private int currentChoice;

    /** Constructor. */
    public MenuState(Handler handler) {
        super(handler);
        bg = new Background("/textures/menubg.png", -0.1f,
                            handler.getGame().getWidth(),
                            handler.getGame().getHeight());
    }

    @Override
    public void render(Graphics g) {

        // background
        bg.render(g);

        // title
        g.setFont(titleFont);
        g.setColor(Color.ORANGE);
        g.drawString(handler.getGame().getTitle(), 100, 125);

        // options
        g.setFont(font);
        for (int i = 0; i < options.length; i++) {
            g.setColor(i == currentChoice ? Color.WHITE : Color.DARK_GRAY);
            g.drawString(options[i], 100, 130 + 90 * (1 + i));
        }
    }

    @Override
    public void setKeyBindings() {
        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_UP,
                                 (e) -> chooseOption(true), null);

        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_DOWN,
                                 (e) -> chooseOption(false), null);

        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_SPACE,
                                 (e) -> activateChoice(), null);

        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_ENTER,
                                 (e) -> activateChoice(), null);
    }

    /** Selects the menu option. */
    private void chooseOption(boolean up) {
        currentChoice = up ? currentChoice - 1
                           : (currentChoice  + 1) % options.length;
        if (currentChoice < 0)
            currentChoice = options.length - 1;
    }

    /** Activates the menu choice. */
    private void activateChoice() {

        if (currentChoice == 0)
            handler.getGame().setGameState();
        else if (currentChoice == 1)
            handler.getGame().setHelpState();
        else if (currentChoice == 2)
            System.exit(0);
    }
}
