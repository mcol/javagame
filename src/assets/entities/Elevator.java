package assets.entities;

import assets.tiles.Tile;
import utils.Utils;

public class Elevator extends MovingSurface {

    /** Constructor. */
    public Elevator(float x, float y) {
        super(x, y);

        // movement
        yMove = Utils.randomInteger(1, 3) * Utils.randomSign();
    }

    @Override
    public void tick() {
        super.tick();
        float dy = getMovementY();
        if (dy == 0)
            yMove = -yMove;
        y += dy;
    }

    /** Computes the allowed movement in the vertical direction. */
    private float getMovementY() {

        if (yMove < 0) { // moving up

            // check the top edge of the map
            int paddingTop = 33;
            if (getTopBound() + yMove < paddingTop)
                return paddingTop - getTopBound();

            // top coordinate of bounding box
            float ty = (getTopBound() + yMove) / Tile.TILESIZE;

            // check the left of the platform for collisions with tiles
            if (collisionWithTile(getLeftBound() / Tile.TILESIZE, ty))
                return (int) ty * Tile.TILESIZE + Tile.TILESIZE - getTopBound();
        }
        else if (yMove > 0) { // moving down

            if (collisionWithEntity(0f, yMove))
                return 0;

            // check the bottom edge of the map
            int bottomEdge = handler.getWorld().getHeight() * Tile.TILESIZE;
            if (getBottomBound() + yMove > bottomEdge)
                return bottomEdge - getBottomBound();

            // bottom coordinate of bounding box in tiles
            float ty = (getBottomBound() + yMove) / Tile.TILESIZE;

            // check the left of the platform for collisions with tiles
            if (collisionWithTile(getLeftBound() / Tile.TILESIZE, ty))
                return (int) ty * Tile.TILESIZE - getBottomBound() - 1;
        }

        return yMove;
    }
}
