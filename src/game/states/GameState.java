package game.states;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import assets.entities.Player;
import game.Game;
import game.HUD;
import game.Handler;
import game.input.KeyManager;
import worlds.World;

public class GameState extends State {

    private final World world;

    /** Heads-up display. */
    private final HUD hud;

    /** Keys pressed. */
    private boolean help, pause, quit;

    /** Constructor. */
    public GameState(Handler handler) {
        super(handler);
        world = new World(handler, "res/worlds/world1.txt",
                          new Player(handler, 0, 0));
        handler.setWorld(world);
        hud = new HUD(handler.getPlayer(), Game.WIDTH);
    }

    /** Shows the help page. */
    private void help() {
        Game.setHelpState();
    }

    /** Quits the current game. */
    private void quit() {
        Game.setMenuState();
    }

    @Override
    public void tick() {
        if (!pause)
            world.tick();

        if (handler.getPlayer().isDead() || quit)
            quit();

        if (help)
            help();
    }

    @Override
    public void render(Graphics g) {
        world.render(g);
        hud.render(g);
    }

    @Override
    public void setKeyBindings() {
        world.getEntityManager().getPlayer().addKeyBindings();
        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_H,
                                 (e) -> help = true, (e) -> help = false);
        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_P,
                                 (e) -> pause = !pause, null);
        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_Q,
                                 (e) -> quit = true, (e) -> quit = false);
    }
}
