package assets.entities;

import assets.Assets;
import assets.tiles.Tile;
import gfx.Animation;
import utils.Utils;

public class Boo extends Enemy {

    /** Score awarded when killed. */
    private static final int BOO_SCORE = 4;

    /** Health bar colour. */
    private static final int BAR_COLOUR = 0xfdfdfd;

    /** Constructor. */
    public Boo(int x, int y, int health) {
        super(x, y, 55, 55, health, BOO_SCORE, BAR_COLOUR);

        // bounding box
        setBounds(3, 6, width - 6, height - 12);

        // enemy parameters
        this.facingRight = Utils.randomBoolean();
        this.hasGravity = false;
        this.findSolidGround = false;

        // animations
        animation = new Animation(Assets.boo_moving, 250);
        animDying = new Animation(Assets.boo_dying, 125);

        // movement
        xMove = SLOW_SPEED * (facingRight ? 1.0f : -1.0f)
              + Utils.randomFloat(-1f, 1f);
    }

    // no collisions with solid tiles
    @Override
    protected float getMovementX() {
        if (xMove > 0) {
            int rightEdge = handler.getWorld().getWidth() * Tile.TILESIZE;
            if (getRightBound() + xMove >  rightEdge)
                return rightEdge - getRightBound();
        }
        else if (xMove < 0) {
            // check the left edge of the map
            if (getLeftBound() + xMove < 0)
                return -getLeftBound();
        }
        return xMove;
    }
}
