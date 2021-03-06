package gfx;

import assets.entities.Entity;
import assets.tiles.Tile;
import game.Game;
import game.Handler;
import utils.Utils;

public class Camera {

    /** Object handler. */
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

        // position the camera is aiming to move to
        float xTarget = e.getX() - Game.WIDTH / 2 + e.getWidth() / 2;
        float yTarget = e.getY() - Game.HEIGHT / 2 + e.getHeight() / 2;

        // check the map bounds to avoid showing blank space outside of the map
        xTarget = Utils.clampValue(xTarget, 0,
                                   handler.getWorld().getWidth() * Tile.TILESIZE -
                                   Game.WIDTH);
        yTarget = Utils.clampValue(yTarget, 0,
                                   handler.getWorld().getHeight() * Tile.TILESIZE -
                                   Game.HEIGHT);

        // move gradually towards the target
        float tween = 0.05f;
        xOffset += tween * (xTarget - xOffset);
        yOffset += tween * (yTarget - yOffset);
    }

    // getters and setters

    /** Returns the horizontal camera offset in pixels. */
    public int getxOffset() {
        return (int) xOffset;
    }

    /** Sets the horizontal camera offset in pixels. */
    public void setxOffset(float xOffset) {
        this.xOffset = xOffset;
    }

    /** Returns the vertical camera offset in pixels. */
    public int getyOffset() {
        return (int) yOffset;
    }

    /** Sets the vertical camera offset in pixels. */
    public void setyOffset(float yOffset) {
        this.yOffset = yOffset;
    }
}
