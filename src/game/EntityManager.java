package game;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;
import assets.entities.CollectableItem;
import assets.entities.Enemy;
import assets.entities.Entity;
import assets.entities.ExitItem;
import assets.entities.Player;

public class EntityManager {

    /** Current player. */
    private final Player player;

    /** List of alive entities. */
    private final ArrayList<Entity> entities;

    /** Number of alive enemies. */
    private int enemyCount;

    /** Number of items to be collected. */
    private int itemCount;

    /** Exit item. */
    private ExitItem exit;

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
        this.player = player;
        entities = new ArrayList<Entity>();
        enemyCount = 0;
        itemCount = 0;
        addEntity(player);
    }

    public void tick() {
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.tick();
            if (e.shouldRemove()) {
                removeEntity(e);
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
    }

    /** Adds an entity to the list of alive entities. */
    public void addEntity(Entity e) {
        entities.add(e);
        if (e instanceof Enemy)
            enemyCount++;
        else if (e instanceof CollectableItem)
            itemCount++;
        else if (e instanceof ExitItem)
            exit = (ExitItem) e;
    }

    /** Removes an entity from the list of alive entities. */
    private void removeEntity(Entity e) {
        entities.remove(e);
        if (e instanceof Enemy)
            enemyCount--;
        else if (e instanceof CollectableItem)
            itemCount--;
    }

    /** Displays the exit. */
    public void showExit() {
        exit.show();
    }

    /** Returns whether the exit has been taken. */
    public boolean isExitTaken() {
        return exit.isTaken();
    }

    // getters and setters

    /** Returns the player. */
    public Player getPlayer() {
        return player;
    }

    /** Returns the list of alive entities. */
    public ArrayList<Entity> getEntities() {
        return entities;
    }

    /** Returns the number of alive enemies. */
    public int getEnemyCount() {
        return enemyCount;
    }

    /** Returns the number of items to be collected. */
    public int getItemCount() {
        return itemCount;
    }
}
