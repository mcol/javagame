package assets.entities;

import assets.Assets;

public class CollectableItem extends StaticEntity {

    /** Whether the item has been collected. */
    protected boolean collected = false;

    /** Constructor. */
    public CollectableItem(Items item, int x, int y) {
        super(Assets.items[item.ordinal()], x, y, 64, 64);
        setBounds(18, 18, 32, 32);
    }

    /** Returns whether the item should be removed. */
    @Override
    public boolean shouldRemove() {
        return collected;
    }
}
