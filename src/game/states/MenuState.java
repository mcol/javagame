package game.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import game.Handler;

public class MenuState extends State {

    private static final String[] options = { "Start", "Quit" };
    private static final Font titleFont = new Font("Century Gothic", Font.BOLD, 96);
    private static final Font font = new Font("Century Gothic", Font.BOLD, 72);

    /** Current menu choice. */
    private int currentChoice;

    /** Constructor. */
    public MenuState(Handler handler) {
        super(handler);
    }

    @Override
    public void tick() {
        if (handler.getGame().getKeyManager().up)
            chooseOption(true);
        else if (handler.getGame().getKeyManager().down)
            chooseOption(false);
        else if (handler.getGame().getKeyManager().space)
            activateChoice();
    }

    @Override
    public void render(Graphics g) {

        // background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 800 / 16 * 9);

        // title
        g.setFont(titleFont);
        g.setColor(Color.ORANGE);
        g.drawString(handler.getGame().getTitle(), 100, 125);

        // options
        g.setFont(font);
        for (int i = 0; i < options.length; i++) {
            g.setColor(i == currentChoice ? Color.WHITE : Color.GRAY);
            g.drawString(options[i], 100, 150 + 100 * (1 + i));
        }
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
            System.exit(0);
    }
}
