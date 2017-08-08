package game.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import game.Game;
import game.input.KeyManager;
import gfx.Background;
import gfx.Font;

public class HighScoreState extends State {

    /** The title text. */
    private final String title;

    /** The highscore list. */
    private final String[] scores;

    /** Index of the most recently added score. */
    private final int index;

    /** Constructor. */
    public HighScoreState(int score, int index) {
        super(handler);
        bg = new Background("/textures/menubg.png", -0.1f,
                            Game.WIDTH, Game.HEIGHT);
        this.title = score == -1 ? "High scores" : "Score: " + score;
        this.scores = Game.getHighScores();
        this.index = index;
    }

    @Override
    public void render(Graphics g) {

        // background
        bg.render(g);

        // title
        Font.setColour(Color.ORANGE);
        Font.renderMessage(g, title, 40, 50,
                           Font.Size.TITLE, true);

        // high scores
        Font.setColour(Color.WHITE);
        for (int i = 0; i < scores.length; i++)
            Font.renderMessage(g, scores[i],
                               100 + 500 * (i / 5), 90 + 85 * (i % 5 + 1),
                               Font.Size.MEDIUM, true);

        // most recently added score
        if (index != -1) {
            Font.setColour(Color.ORANGE);
            Font.renderMessage(g, scores[index],
                               100 + 500 * (index / 5), 90 + 85 * (index % 5 + 1),
                               Font.Size.MEDIUM, true);
        }
    }

    @Override
    public void setKeyBindings() {
        KeyManager.addKeyBinding(display, KeyEvent.VK_ENTER,
                                 (e) -> Game.setMenuState());
        KeyManager.addKeyBinding(display, KeyEvent.VK_SPACE,
                                 (e) -> Game.setMenuState());
        KeyManager.addKeyBinding(display, KeyEvent.VK_Q,
                                 (e) -> Game.setMenuState());
    }
}
