package assets.entities;

import assets.Assets;

public class CollectableItem extends StaticEntity {

    /** Interval for which the item is displayed in milliseconds. */
    private static final long DISPLAY_INTERVAL = 10000;

    /** Type of item. */
    private final Items item;

    /** Whether the item has been collected. */
    protected boolean collected = false;

    /** Constructor. */
    public CollectableItem(Items item, int x, int y) {
        super(Assets.items[item.ordinal()], x, y, 64, 64);
        this.item = item;
        setBounds(18, 18, 32, 32);
        spawnTime = System.currentTimeMillis();
    }

    @Override
    public void tick() {
        if (System.currentTimeMillis() - spawnTime > DISPLAY_INTERVAL)
            collected = true;
    }

    /** Collects the item. */
    public void collectItem(Player player) {
        collected = true;
        player.increaseScore(item.getValue());
        player.increaseHealth(item.getHealth());
        player.increasePoop(item.getPoop());
    }

    /** Returns whether the item should be removed. */
    @Override
    public boolean shouldRemove() {
        return collected;
    }
}
