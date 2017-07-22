package game;

import java.util.ArrayList;
import java.util.Random;
import assets.entities.Entity;
import assets.entities.EntityManager;
import assets.entities.Player;
import gfx.Camera;
import worlds.World;

public class Handler {

    private final Game game;
    private World world;

    /** Random number generator. */
    private static final Random random = new Random();

    /** Constructor. */
    public Handler(Game game) {
        this.game = game;
    }

    /** Returns a random integer between two values. */
    public static int randomInteger(int min, int max) {
        return min + random.nextInt(max - min);
    }

    // getters and setters

    public Camera getCamera() {
        return game.getCamera();
    }

    public Display getDisplay() {
        return game.getDisplay();
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public EntityManager getEntityManager() {
        return world.getEntityManager();
    }

    public ArrayList<Entity> getEntities() {
        return world.getEntityManager().getEntities();
    }

    /** Returns the player. */
    public Player getPlayer() {
        return world.getEntityManager().getPlayer();
    }
}
