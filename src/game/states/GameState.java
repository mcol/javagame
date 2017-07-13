package game.states;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import game.HUD;
import game.Handler;
import game.input.KeyManager;
import worlds.World;

public class GameState extends State {

    private final World world;

    /** Heads-up display. */
    private final HUD hud;

    /** Keys pressed. */
    private boolean pause, quit;

    /** Constructor. */
    public GameState(Handler handler) {
        super(handler);
        world = new World(handler, "res/worlds/world1.txt");
        handler.setWorld(world);
        hud = new HUD(handler.getPlayer(), handler.getGame().getWidth());
    }

    /** Quits the current game. */
    private void quit() {
        handler.getGame().setMenuState();
    }

    @Override
    public void tick() {
        if (!pause)
            world.tick();

        if (handler.getPlayer().isDead() || quit)
            quit();
    }

    @Override
    public void render(Graphics g) {
        world.render(g);
        hud.render(g);
    }

    @Override
    public void setKeyBindings() {
        world.getEntityManager().getPlayer().addKeyBindings();
        KeyManager.addKeyBinding(handler.getFrame(), KeyEvent.VK_P,
                                 (e) -> pause = !pause, null);
        KeyManager.addKeyBinding(handler.getFrame(), KeyEvent.VK_Q,
                                 (e) -> quit = true, (e) -> quit = false);
    }
}
