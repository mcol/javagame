package game.states;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import assets.entities.Player;
import game.Game;
import game.HUD;
import game.Handler;
import game.World;
import game.input.KeyManager;

public class GameState extends State {

    /** The world currently loaded. */
    private final World world;

    /** Heads-up display. */
    private final HUD hud;

    /** Keys pressed. */
    private boolean pause;

    /** Constructor. */
    public GameState(Handler handler) {
        super(handler);
        world = new World(handler, new Player(handler, 0, 0));
        hud = new HUD(handler.getPlayer(), Game.WIDTH);
    }

    /** Shows the help page. */
    private void help() {
        Game.setHelpState();
    }

    /** Quits the current game. */
    private void quit() {
        Game.setMenuState(handler.getPlayer().getScore());
    }

    @Override
    public void tick() {
        if (!pause) {
            world.tick();
            hud.tick();
        }

        if (handler.getPlayer().isDead())
            quit();

        if (world.isCleared()) {
            handler.getPlayer().increaseLevel();
        }
    }

    @Override
    public void render(Graphics g) {
        world.render(g);
        hud.render(g);
    }

    @Override
    public void setKeyBindings() {
        World.getEntityManager().getPlayer().addKeyBindings();
        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_H,
                                 (e) -> help(), null);
        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_P,
                                 (e) -> pause = !pause, null);
        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_Q,
                                 (e) -> quit(), null);
        KeyManager.removeKeyBindings(display, new int[] { KeyEvent.VK_ENTER });
    }
}
