package game;

import java.util.ArrayList;
import javax.swing.JFrame;
import assets.entities.Entity;
import assets.entities.EntityManager;
import gfx.Camera;
import worlds.World;

public class Handler {

    private Game game;
    private World world;

    /** Constructor. */
    public Handler(Game game) {
        this.game = game;
    }

    // getters and setters

    public Camera getCamera() {
        return game.getCamera();
    }

    public JFrame getFrame() {
        return game.getDisplay().getFrame();
    }

    /** Returns the width of the game in pixels. */
    public int getWidth() {
        return game.getWidth();
    }

    /** Returns the height of the game in pixels. */
    public int getHeight() {
        return game.getHeight();
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
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
}
