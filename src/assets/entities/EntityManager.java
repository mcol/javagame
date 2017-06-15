package assets.entities;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;

public class EntityManager {

    private Player player;
    private ArrayList<Entity> entities;

    /** Class to decide render order of entities. */
    private Comparator<Entity> renderSorter = new Comparator<Entity>() {
        @Override
        public int compare(Entity e1, Entity e2) {
            if (e1.getY() + e1.getHeight() / 2 < e2.getY() + e2.getHeight() / 2)
                return -1;
            else
                return 1;
        }
    };

    public EntityManager(Player player) {
        entities = new ArrayList<Entity>();
        addEntity(player);
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
        for (Entity e : entities)
            e.render(g);
    }

    public void addEntity(Entity e) {
        entities.add(e);
    }

    public void removeEntity(Entity e) {
        entities.remove(e);
    }

    // getters and setters

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }
}
