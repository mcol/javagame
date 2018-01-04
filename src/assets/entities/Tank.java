package assets.entities;

import assets.Assets;
import assets.tiles.Tile;
import gfx.Animation;
import utils.Utils;

public class Tank extends Enemy {

    /** Damage procured by the enemy. */
    private static final int TANK_DAMAGE = 8;

    /** Score awarded when killed. */
    private static final int TANK_SCORE = 15;

    /** Health bar colour. */
    private static final int BAR_COLOUR = 0x777755;

    /** Constructor. */
    public Tank(int x, int y, int health) {
        super(x, y, 140, 90, health, TANK_DAMAGE, TANK_SCORE, BAR_COLOUR);

        // enemy parameters
        this.facingRight = Utils.randomBoolean();
        this.frenzyThreshold = health / 2;
        this.frenzySpeed = SLOW_SPEED;

        // bounding box
        setBounds(10, 10, width - 20, height - 15);

        // animations
        animation = new Animation(Assets.tank_moving, 125);
        animFrenzy = new Animation(Assets.tank_frenzy, 125);
        animDying = new Animation(Assets.tank_dying, 100);

        // movement
        xMove = Utils.randomInteger(1, 2) * (facingRight ? -1 : 1);
    }

    // no fall from solid tiles
    @Override
    protected float getMovementX() {

        float bb = getBottomBound();

        if (xMove > 0) { // moving right

            int rightEdge = handler.getWorld().getWidth() * Tile.TILESIZE;
            if (getRightBound() + xMove > rightEdge)
                return rightEdge - getRightBound();

            // right coordinate of bounding box
            float tx = (getRightBound() + xMove) / Tile.TILESIZE;

            if (collisionWithTile(tx, bb / Tile.TILESIZE) ||
                !collisionWithTile(tx, (bb + Tile.TILESIZE / 2) / Tile.TILESIZE))
                return (int) tx * Tile.TILESIZE - getRightBound();
        }
        else if (xMove < 0) { // moving left

            // left coordinate of bounding box
            float tx = (getLeftBound() + xMove) / Tile.TILESIZE;

            // check the left edge of the map and align to it
            if (tx < 0)
                return -getLeftBound();

            if (collisionWithTile(tx, bb / Tile.TILESIZE) ||
                !collisionWithTile(tx, (bb + Tile.TILESIZE / 2) / Tile.TILESIZE))
                return (int) tx * Tile.TILESIZE + Tile.TILESIZE - getLeftBound();
        }

        return xMove;
    }
}
