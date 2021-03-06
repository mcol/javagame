package assets.entities;

import assets.tiles.Tile;
import utils.Utils;

public class Platform extends MovingSurface {

    /** Constructor. */
    public Platform(float x, float y) {
        super(x, y);

        // movement
        xMove = Utils.randomInteger(1, 3) * Utils.randomSign();
    }

    @Override
    public void tick() {
        super.tick();
        float dx = getMovementX(xMove);
        if (dx == 0)
            xMove = -xMove;
        x += dx;
    }

    /** Computes the allowed movement in the horizontal direction. */
    private float getMovementX(float xMove) {

        if (collisionWithEntity(xMove, 0f))
            return 0;

        if (xMove > 0) { // moving right

            // check the right edge of the map
            int rightEdge = handler.getWorld().getWidth() * Tile.TILESIZE;
            if (getRightBound() + xMove > rightEdge)
                return rightEdge - getRightBound();

            // right coordinate of bounding box
            float tx = (getRightBound() + xMove) / Tile.TILESIZE;

            // check the top of the platform for collisions with tiles
            if (collisionWithTile(tx, getTopBound() / Tile.TILESIZE))
                return (int) tx * Tile.TILESIZE - getRightBound();
        }
        else if (xMove < 0) { // moving left

            // check the left edge of the map
            if (getLeftBound() + xMove < 0)
                return -getLeftBound();

            // left coordinate of bounding box
            float tx = (getLeftBound() + xMove) / Tile.TILESIZE;

            // check the top of the platform for collisions with tiles
            if (collisionWithTile(tx, getTopBound() / Tile.TILESIZE))
                return (int) tx * Tile.TILESIZE + Tile.TILESIZE - getLeftBound();
        }

        return xMove;
    }
}
