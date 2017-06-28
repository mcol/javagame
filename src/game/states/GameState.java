package game.states;

import java.awt.Graphics;
import game.Handler;
import worlds.World;

public class GameState extends State {

    private final World world;

    /** Constructor. */
    public GameState(Handler handler) {
        super(handler);
        world = new World(handler, "res/worlds/world1.txt");
        handler.setWorld(world);
    }

    @Override
    public void tick() {
        world.tick();
        if (world.getEntityManager().getPlayer().isDead())
            handler.getGame().setMenuState();
    }

    @Override
    public void render(Graphics g) {
        world.render(g);
    }
}
