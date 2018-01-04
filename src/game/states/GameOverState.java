package game.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import game.Game;
import game.input.KeyManager;
import game.scores.Score;
import gfx.Background;
import gfx.Font;

public class GameOverState extends State {

    /** List of characters. */
    private static final String keyboard = "abcdefghij"
                                         + "klmnopqrst"
                                         + "uvwxyz!~<|";

    /** Name of the player. */
    private String name;

    /** Score achieved by the player. */
    private final int score;

    /** Index of the cursor in the list of characters. */
    private int index;

    /** Constructor. */
    public GameOverState(int score) {
        super(handler);
        bg = new Background("/textures/menubg.png", -0.1f,
                            Game.WIDTH, Game.HEIGHT);
        this.name = "";
        this.score = score;
        this.index = 0;
    }

    /** Adds a character to the name. */
    private void addCharacter() {
        char selected = keyboard.charAt(index);
        if (selected == '|')
            exit();
        else if (selected == '<' && name != null)
            name = name.substring(0, Math.max(name.length() - 1, 0));
        else if (name.length() < Score.MAX_NAME_LENGTH) {
            if (selected == '~')
                selected = ' ';
            name += selected;
        }
    }

    /** Leaves this state and shows the highscore list. */
    private void exit() {
        if (name == "")
            name = "...";
        Game.setHighScoreState(score, Game.addHighScore(name));
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render(Graphics g) {
        // background
        bg.render(g);

        if (!Game.isHighScore(score))
            Game.setHighScoreState(score, -1);

        // title
        Font.setColour(Color.ORANGE);
        Font.renderMessage(g, "Score: " + score, 40, 50,
                           Font.Size.TITLE, true);

        // name
        Font.setColour(Color.WHITE);
        Font.renderMessage(g, "Enter name: ", 100, 175, Font.Size.LARGE, true);
        Font.setColour(Color.ORANGE);
        Font.renderMessage(g, name, 725, 175, Font.Size.LARGE, true);

        // available characters
        Font.setColour(Color.DARK_GRAY);
        for (int i = 0; i < keyboard.length(); i++)
            Font.renderMessage(g, "" + keyboard.charAt(i),
                               100 + 95 * (i % 10), 275 + 100 * (i / 10),
                               Font.Size.LARGE, true);

        // highlighted character
        Font.setColour(Color.WHITE);
        Font.renderMessage(g, "" + keyboard.charAt(index),
                           100 + 95 * (index % 10), 275 + 100 * (index / 10),
                           Font.Size.LARGE, true);
    }

    /** Adds the key bindings to control the state. */
    @Override
    public void setKeyBindings() {
        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_UP,
                                 (e) -> index = Math.floorMod(index - 10, 30));
        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_DOWN,
                                 (e) -> index = (index + 10) % 30);
        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_LEFT,
                                 (e) -> index = Math.floorMod(index - 1, 30));
        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_RIGHT,
                                 (e) -> index = (index + 1) % 30);
        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_ENTER,
                                 (e) -> addCharacter());
        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_ESCAPE,
                                 (e) -> exit());
        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_SPACE,
                                 (e) -> addCharacter());
        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_Q,
                                 (e) -> exit());
    }
}
