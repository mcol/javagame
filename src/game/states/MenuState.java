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
    private static final String[] options = { "Start", "High scores", "Help", "Quit" };

    /** Current menu choice. */
    private int currentChoice;

    /** Constructor. */
    public MenuState(Handler handler) {
        super(handler);
        bg = new Background("/textures/menubg.png", -0.1f,
                            Game.WIDTH, Game.HEIGHT);
    }

    @Override
    public void render(Graphics g) {

        // background
        bg.render(g);

        // title
        Font.setColour(Color.ORANGE);
        Font.renderMessage(g, Game.TITLE, 40, 50,
                           Font.Size.TITLE, true);

        // options
        for (int i = 0; i < options.length; i++) {
            Font.setColour((i == currentChoice) ? Color.WHITE : Color.DARK_GRAY);
            Font.renderMessage(g, options[i], 100, 90 + 100 * (1 + i),
                               Font.Size.LARGE, true);
        }
    }

    @Override
    public void setKeyBindings() {
        KeyManager.addKeyBinding(display, KeyEvent.VK_UP,
                                 (e) -> chooseOption(true));

        KeyManager.addKeyBinding(display, KeyEvent.VK_DOWN,
                                 (e) -> chooseOption(false));

        KeyManager.addKeyBinding(display, KeyEvent.VK_SPACE,
                                 (e) -> activateChoice());

        KeyManager.addKeyBinding(display, KeyEvent.VK_ENTER,
                                 (e) -> activateChoice());

        KeyManager.addKeyBinding(display, KeyEvent.VK_H,
                                 (e) -> Game.setHelpState());

        KeyManager.addKeyBinding(display, KeyEvent.VK_Q,
                                 (e) -> System.exit(0));

        KeyManager.addKeyBinding(display, KeyEvent.VK_ESCAPE,
                                 (e) -> System.exit(0));
    }

    /** Selects the menu option. */
    private void chooseOption(boolean up) {
        currentChoice = up ? currentChoice - 1
                           : (currentChoice + 1) % options.length;
        if (currentChoice < 0)
            currentChoice = options.length - 1;
    }

    /** Activates the menu choice. */
    private void activateChoice() {
        if (currentChoice == 0)
            Game.setGameState();
        else if (currentChoice == 1)
            Game.setHighScoreState(-1, -1);
        else if (currentChoice == 2)
            Game.setHelpState();
        else if (currentChoice == 3)
            System.exit(0);
    }
}
