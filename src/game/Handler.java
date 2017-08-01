package game;

import java.util.ArrayList;
import assets.entities.Entity;
import assets.entities.EntityManager;
import assets.entities.Player;
import gfx.Camera;

public class Handler {

    private World world;

    // getters and setters

    public Display getDisplay() {
        return Game.getDisplay();
    }

    /** Returns the camera. */
    public Camera getCamera() {
        return Game.getCamera();
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

    /** Add a message to the list of messages to be displayed. */
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
