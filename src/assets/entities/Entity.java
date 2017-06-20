package assets.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import game.Handler;
import gfx.Animation;

public abstract class Entity {

    protected Handler handler;

    /** Coordinates of the entity in pixels. */
    protected float x, y;

    /** Size of the entity in pixels. */
    protected int width, height;

    /** Collision bounding box. */
    protected Rectangle bounds;

    /** When the entity was generated. */
    protected long spawnTime;

    /** Animation representing the entity. */
    protected Animation animation;

    /** Constructor. */
    public Entity(Handler handler, float x, float y, int width, int height) {
        this.handler = handler;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        // the default bounding box corresponds to the size of the entity
        bounds = new Rectangle(0, 0, width, height);
    }

    public void tick() {
        animation.tick();
    }

    public abstract void render(Graphics g);

    public boolean collidesWithEntity(float xOffset, float yOffset) {
        for (Entity e : handler.getEntities()) {
            // don't check for collisions against itself
            if (e.equals(this)) {
                continue;
            }
            if (e.getCollisionRectangle(0f, 0f)
                 .intersects(getCollisionRectangle(xOffset, yOffset))) {
                return true;
            }
        }
        return false;
    }

    public Rectangle getCollisionRectangle(float xOffset, float yOffset) {
        return new Rectangle((int) (x + bounds.x + xOffset),
                             (int) (y + bounds.y + yOffset),
                             bounds.width, bounds.height);
    }

    /** Whether the entity is currently not visible. */
    public boolean offScreen() {
        return  x - handler.getCamera().getxOffset() + width < 0 ||
                x - handler.getCamera().getxOffset() > handler.getGame().getWidth() ||
                y - handler.getCamera().getyOffset() + height < 0 ||
                y - handler.getCamera().getyOffset() >  handler.getGame().getHeight();
    }

    // getters and setters

    /** Returns the horizontal coordinate of the entity in pixels. */
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    /** Returns the vertical coordinate of the entity in pixels. */
    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    /** Returns the width of the entity in pixels. */
    public int getWidth() {
        return width;
    }

    /** Returns the height of the entity in pixels. */
    public int getHeight() {
        return height;
    }

    /** Returns the left coordinate of the entity's bounding box in pixels. */
    protected float getLeftBound() {
        return x + bounds.x;
    }

    /** Returns the right coordinate of the entity's bounding box in pixels. */
    protected float getRightBound() {
        return x + bounds.x + bounds.width;
    }

    /** Returns the top coordinate of the entity's bounding box in pixels. */
    protected float getTopBound() {
        return y + bounds.y;
    }

    /** Returns the bottom coordinate of the entity's bounding box in pixels. */
    protected float getBottomBound() {
        return y + bounds.y + bounds.height;
    }

    /** Whether the entity should be removed. */
    public boolean shouldRemove() {
        return false;
    };

    /** Sets the collision bounding box. */
    protected void setBounds(int x, int y, int width, int height) {
        bounds.x = x;
        bounds.y = y;
        bounds.width = width;
        bounds.height = height;
    }
}
