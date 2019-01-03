package game.states;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import assets.entities.Player;
import game.Game;
import game.HUD;
import game.World;
import game.input.KeyManager;

public class GameState extends State {

    /** World currently loaded. */
    private final World world;

    /** Heads-up display. */
    private final HUD hud;

    /** Keys pressed. */
    private boolean pause;

    /** Constructor. */
    public GameState() {
        super(handler);
        world = new World(handler, new Player(handler, 0, 0));
        hud = new HUD(handler.getPlayer(), Game.WIDTH);
    }

    /** Quits the current game. */
    private void quit() {
        int score = handler.getPlayer().getScore();
        if (score > 0)
            Game.setGameOverState(score);
        else
            Game.setMenuState();
    }

    @Override
    public void tick() {
        if (!pause) {
            world.tick();
            hud.tick();
        }

        if (handler.getPlayer().isDead()) {
            if (hud.isHealthCurrent())
                quit();
            return;
        }

        if (world.isCleared()) {
            handler.getPlayer().increaseLevel();
        }
    }

    @Override
    public void render(Graphics g) {
        world.render(g);
        hud.render(g);
    }

    /** Adds the key bindings to control the state. */
    @Override
    public void setKeyBindings() {
        World.getEntityManager().getPlayer().addKeyBindings();
        KeyManager.addKeyBinding(display, KeyEvent.VK_P,
                                 (e) -> pause = !pause);
        KeyManager.addKeyBinding(display, KeyEvent.VK_Q,
                                 (e) -> quit());
        KeyManager.addKeyBinding(display, KeyEvent.VK_ESCAPE,
                                 (e) -> quit());
        KeyManager.removeKeyBindings(display, new int[] { KeyEvent.VK_ENTER });
    }
}
