package game.states;

import java.awt.Graphics;
import game.HUD;
import game.Handler;
import worlds.World;

public class GameState extends State {

    private final World world;

    /** Heads-up display. */
    private final HUD hud;

    /** Constructor. */
    public GameState(Handler handler) {
        super(handler);
        world = new World(handler, "res/worlds/world1.txt");
        handler.setWorld(world);
        hud = new HUD(handler.getPlayer(), handler.getGame().getWidth());
    }

    @Override
    public void tick() {
        world.tick();
        if (handler.getPlayer().isDead())
            handler.getGame().setMenuState();
    }

    @Override
    public void render(Graphics g) {
        world.render(g);
        hud.render(g);
    }

    @Override
    public void setKeyBindings() {
        world.getEntityManager().getPlayer().addKeyBindings();
    }
}
