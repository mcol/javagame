package game;

import java.util.ArrayList;
import assets.entities.Entity;
import assets.entities.EntityManager;
import assets.entities.Player;
import gfx.Camera;
import worlds.World;

public class Handler {

    private final Game game;
    private World world;

    /** Constructor. */
    public Handler(Game game) {
        this.game = game;
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
        return World.getEntityManager();
    }

    public ArrayList<Entity> getEntities() {
        return World.getEntityManager().getEntities();
    }

    /** Returns the player. */
    public Player getPlayer() {
        return World.getEntityManager().getPlayer();
    }
}
