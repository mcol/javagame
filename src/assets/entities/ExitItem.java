package assets.entities;

import java.awt.Graphics;
import assets.Assets;

public class ExitItem extends StaticEntity {

    /** Whether the exit is visible. */
    private boolean visible;

    /** Whether the player has interacted with the exit. */
    private boolean taken;

    /** Constructor. */
    public ExitItem(float x, float y) {
        super(Assets.items[Items.EXIT.ordinal()], x, y, 64, 64);
        this.visible = false;
        this.taken = false;
        setBounds(0, 0, 0, 0);
    }

    /** Returns whether the entity collides with another at the given offset. */
    @Override
    protected boolean collisionWithEntity(float xOffset, float yOffset) {
        taken = visible && super.collisionWithEntity(xOffset, yOffset);
        return taken;
    }

    /** Makes the exit visible. */
    public void show() {
        if (visible)
            return;
        setBounds(18, 18, 32, 32);
        visible = true;
    }

    /** Takes the exit and increases the player's state. */
    public void take(Player player) {
        int value = Items.EXIT.getValue();
        player.increaseScore(value);
        handler.addMessage(Integer.toString(value), x, y);
        taken = true;
    }

    @Override
    public void render(Graphics g) {
        if (visible)
            super.render(g);
    }

    /** Returns whether the item should be removed. */
    @Override
    public boolean shouldRemove() {
        return taken;
    }

    // getters and setters

    /** Returns whether the exit has been taken. */
    public boolean isTaken() {
        return taken;
    }
}
