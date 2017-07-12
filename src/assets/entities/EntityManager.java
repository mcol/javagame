package assets.entities;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;
import assets.hud.HUD;

public class EntityManager {

    private Player player;

    /** List of alive entities. */
    private final ArrayList<Entity> entities;

    /** Heads-up display. */
    private final HUD hud;

    /** Class to decide render order of entities. */
    private final Comparator<Entity> renderSorter = new Comparator<Entity>() {
        @Override
        public int compare(Entity e1, Entity e2) {
            if (e1.getY() + e1.getHeight() / 2 < e2.getY() + e2.getHeight() / 2)
                return -1;
            else
                return 1;
        }
    };

    /** Constructor. */
    public EntityManager(Player player) {
        entities = new ArrayList<Entity>();
        addEntity(player);
        hud = new HUD(player);
        this.player = player;
    }

    public void tick() {
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.tick();
            if (e.shouldRemove()) {
                entities.remove(i);
                i--;
            }
        }
        entities.sort(renderSorter);
    }

    public void render(Graphics g) {
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.render(g);
        }
        hud.render(g);
    }

    /** Add an entity to the list of alive entities. */
    public void addEntity(Entity e) {
        entities.add(e);
    }

    /** Remove an entity from the list of alive entities. */
    public void removeEntity(Entity e) {
        entities.remove(e);
    }

    // getters and setters

    /** Returns the player. */
    public Player getPlayer() {
        return player;
    }

    /** Sets the player. */
    public void setPlayer(Player player) {
        this.player = player;
        hud.setPlayer(player);
    }

    /** Returns the list of alive entities. */
    public ArrayList<Entity> getEntities() {
        return entities;
    }
}
