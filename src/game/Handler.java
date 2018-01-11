package game;

import java.util.ArrayList;
import assets.entities.Entity;
import assets.entities.Player;
import gfx.Camera;

public class Handler {

    /** Current world. */
    private World world;

    // getters and setters

    /** Returns the game display. */
    public Display getDisplay() {
        return Game.getDisplay();
    }

    /** Returns the camera. */
    public Camera getCamera() {
        return Game.getCamera();
    }

    /** Returns the current world. */
    public World getWorld() {
        return world;
    }

    /** Sets the current world. */
    public void setWorld(World world) {
        this.world = world;
    }

    /** Returns the entity manager. */
    public EntityManager getEntityManager() {
        return World.getEntityManager();
    }

    /** Returns the list of alive entities. */
    public ArrayList<Entity> getEntities() {
        return World.getEntityManager().getEntities();
    }

    /** Adds a message to the list of messages to be displayed. */
    public void addMessage(String message, float x, float y) {
        World.getMessageManager()
             .addMessage(message,
                         x - getCamera().getxOffset(),
                         y - getCamera().getyOffset());
    }

    /** Returns the player. */
    public Player getPlayer() {
        return World.getEntityManager().getPlayer();
    }
}
