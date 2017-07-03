package assets.entities;

import assets.Assets;

public class CollectableItem extends StaticEntity {

    /** Type of item. */
    private final Items item;

    /** Whether the item has been collected. */
    protected boolean collected = false;

    /** Constructor. */
    public CollectableItem(Items item, int x, int y) {
        super(Assets.items[item.ordinal()], x, y, 64, 64);
        this.item = item;
        setBounds(18, 18, 32, 32);
    }

    /** Collects the item. */
    public void collectItem(Player player) {
        collected = true;
        Player.increaseScore(item.getValue());
        player.increaseHealth(item.getHealth());
        player.increasePoop(item.getPoop());
    }

    /** Returns whether the item should be removed. */
    @Override
    public boolean shouldRemove() {
        return collected;
    }
}
