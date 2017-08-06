package game.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import game.Game;
import game.input.KeyManager;
import gfx.Background;
import gfx.Font;

public class HighScoreState extends State {

    /** The highscore list. */
    String[] scores;

    /** Constructor. */
    public HighScoreState() {
        super(handler);
        bg = new Background("/textures/menubg.png", -0.1f,
                            Game.WIDTH, Game.HEIGHT);
        scores = Game.getHighScores();
    }

    @Override
    public void render(Graphics g) {

        // background
        bg.render(g);

        // title
        Font.setColour(Color.ORANGE);
        Font.renderMessage(g, "High scores", 40, 50,
                           Font.Size.TITLE, true);

        Font.setColour(Color.WHITE);
        for (int i = 0; i < scores.length; i++)
            Font.renderMessage(g, scores[i],
                               100 + 500 * (i / 5), 90 + 85 * (i % 5 + 1),
                               Font.Size.MEDIUM, true);
    }

    @Override
    public void setKeyBindings() {
        KeyManager.addKeyBinding(display, KeyEvent.VK_ENTER,
                                 (e) -> Game.setMenuState(-1));
        KeyManager.addKeyBinding(display, KeyEvent.VK_SPACE,
                                 (e) -> Game.setMenuState(-1));
        KeyManager.addKeyBinding(display, KeyEvent.VK_Q,
                                 (e) -> Game.setMenuState(-1));
    }
}
