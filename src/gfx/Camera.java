package gfx;

import assets.entities.Entity;
import assets.tiles.Tile;
import game.Handler;
import utils.Utils;

public class Camera {

    private final Handler handler;

    /** Offset of the camera in pixels with respect to the game coordinates. */
    private float xOffset, yOffset;

    /** Constructor. */
    public Camera(Handler handler) {
        this.handler = handler;
        this.xOffset = 0;
        this.yOffset = 0;
    }

    /** Moves the camera so that the entity is at the centre. */
    public void centreOnEntity(Entity e) {
        xOffset = e.getX() - handler.getWidth() / 2 + e.getWidth() / 2;
        yOffset = e.getY() - handler.getHeight() / 2 + e.getHeight() / 2;

        // check the map bounds to avoid showing blank space outsie of the map
        xOffset = Utils.clampValue(xOffset, 0, handler.getWorld().getWidth() * Tile.TILEWIDTH - handler.getWidth());
        yOffset = Utils.clampValue(yOffset, 0, handler.getWorld().getHeight() * Tile.TILEHEIGHT - handler.getHeight());
    }

    // getters and setters

    /** Returns the horizontal camera offset in pixels. */
    public float getxOffset() {
        return xOffset;
    }

    public void setxOffset(float xOffset) {
        this.xOffset = xOffset;
    }

    /** Returns the vertical camera offset in pixels. */
    public float getyOffset() {
        return yOffset;
    }

    public void setyOffset(float yOffset) {
        this.yOffset = yOffset;
    }
}
