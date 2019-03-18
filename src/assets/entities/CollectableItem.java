package assets.entities;

import assets.Assets;
import game.Game;

public class CollectableItem extends StaticEntity {

    /** Interval for which the item is displayed in ticks. */
    private static final long DISPLAY_INTERVAL = 15 * Game.FPS;

    /** Type of item. */
    private final Items item;

    /** Whether the item has been collected. */
    private boolean collected = false;

    /** Constructor. */
    public CollectableItem(Items item, int x, int y) {
        super(Assets.items[item.ordinal()], x, y, 64, 64);
        this.item = item;
        setBounds(18, 18, 32, 32);
    }

    @Override
    public void tick() {
        super.tick();
        if (now - spawnTime > DISPLAY_INTERVAL)
            collected = true;
    }

    /** Collects the item. */
    public void collectItem() {
        collected = true;

        // avoid further collisions
        setBounds(0, 0, 0, 0);
    }

    /** Collects the item and increases the player's state. */
    public void collectItem(Player player) {
        collectItem();
        player.increaseScore(item.getValue());
        player.increaseTime(item.getTime());
        player.increaseHealth(item.getHealth());
        player.increasePoop(item.getPoop());
        handler.addMessage(Integer.toString(item.getValue()), x, y);
    }

    /** Returns whether the item should be removed. */
    @Override
    public boolean shouldRemove() {
        return collected;
    }
}
