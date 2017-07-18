package assets.entities;

import assets.Assets;
import game.Game;

public class CollectableItem extends StaticEntity {

    /** Interval for which the item is displayed in ticks. */
    private static final long DISPLAY_INTERVAL = 10 * Game.FPS;

    /** Type of item. */
    private final Items item;

    /** Whether the item has been collected. */
    protected boolean collected = false;

    /** Constructor. */
    public CollectableItem(Items item, int x, int y) {
        super(Assets.items[item.ordinal()], x, y, 64, 64);
        this.item = item;
        setBounds(18, 18, 32, 32);
        spawnTime = handler.getGame().getTicksTime();;
    }

    @Override
    public void tick() {
        now = handler.getGame().getTicksTime();
        if (now - spawnTime > DISPLAY_INTERVAL)
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
