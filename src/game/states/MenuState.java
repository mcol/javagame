package game.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import game.Game;
import game.Handler;
import game.input.KeyManager;
import gfx.Background;
import gfx.Font;

public class MenuState extends State {

    /** Menu choices. */
    private static final String[] options = { "Start", "Help", "Quit" };

    /** Menu title. */
    private final String title;

    /** Current menu choice. */
    private int currentChoice;

    /** Constructor. */
    public MenuState(Handler handler, String title) {
        super(handler);
        bg = new Background("/textures/menubg.png", -0.1f,
                            Game.WIDTH, Game.HEIGHT);
        this.title = title;
    }

    @Override
    public void render(Graphics g) {

        // background
        bg.render(g);

        // title
        Font.setColour(Color.ORANGE);
        Font.renderMessage(g, title, 100, 70,
                           Font.Size.TITLE, true);

        // options
        for (int i = 0; i < options.length; i++) {
            Font.setColour((i == currentChoice) ? Color.WHITE : Color.DARK_GRAY);
            Font.renderMessage(g, options[i], 100, 90 + 90 * (1 + i),
                               Font.Size.LARGE, true);
        }
    }

    @Override
    public void setKeyBindings() {
        KeyManager.addKeyBinding(display, KeyEvent.VK_UP,
                                 (e) -> chooseOption(true), null);

        KeyManager.addKeyBinding(display, KeyEvent.VK_DOWN,
                                 (e) -> chooseOption(false), null);

        KeyManager.addKeyBinding(display, KeyEvent.VK_SPACE,
                                 (e) -> activateChoice(), null);

        KeyManager.addKeyBinding(display, KeyEvent.VK_ENTER,
                                 (e) -> activateChoice(), null);

        KeyManager.addKeyBinding(display, KeyEvent.VK_H,
                                 (e) -> handler.getGame().setHelpState(), null);

        KeyManager.addKeyBinding(display, KeyEvent.VK_Q,
                                 (e) -> System.exit(0), null);
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
