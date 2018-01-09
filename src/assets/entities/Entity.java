package assets.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import game.Game;
import game.Handler;

public abstract class Entity {

    /** Object handler. */
    protected static Handler handler;

    /** Coordinates of the top left corner of the entity in pixels. */
    protected float x, y;

    /** Size of the entity in pixels. */
    protected int width, height;

    /** Collision bounding box. */
    protected Rectangle bounds;

    /** Horizontal movement. */
    protected float xMove;

    /** Vertical movement. */
    protected float yMove;

    /** Current time in ticks. */
    protected long now;

    /** Time when the entity was generated in ticks. */
    protected long spawnTime;

    /** Constructor. */
    public Entity(Handler handler, float x, float y, int width, int height) {
        Entity.handler = handler;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.xMove = 0;
        this.yMove = 0;
        this.now = Game.getTicksTime();
        this.spawnTime = now;

        // the default bounding box corresponds to the size of the entity
        bounds = new Rectangle(0, 0, width, height);
    }

    public void tick() {
        now = Game.getTicksTime();
    }

    public abstract void render(Graphics g);

    /** Returns whether the entity collides with another at the given offset. */
    protected boolean collisionWithEntity(float xOffset, float yOffset) {
        for (Entity e : handler.getEntities()) {
            // don't check for collisions against itself
            if (e.equals(this)) {
                continue;
            }
            if (e.getCollisionRectangle(0f, 0f)
                 .intersects(getCollisionRectangle(xOffset, yOffset))) {

                if (e instanceof CollectableItem) {
                    // collect the item
                    if (this instanceof Player)
                        ((CollectableItem) e).collectItem((Player) this);

                    // destroy the item
                    else if (this instanceof Missile)
                        ((CollectableItem) e).collectItem();

                    // no collision between enemies and collectable items
                    else if (this instanceof Enemy)
                        return false;

                    // no collision between platforms and collectable items
                    else if (this instanceof MovingSurface)
                        return false;
                }
                else if (e instanceof ExitItem) {
                    if (this instanceof Player)
                        ((ExitItem) e).take((Player) this);
                }

                return true;
            }
        }
        return false;
    }

    /** Returns whether the entity collides with a static entity. */
    protected boolean collisionWithStaticEntity(float xOffset, float yOffset) {
        for (Entity e : handler.getEntities()) {
            // don't check for collisions with moving entities or with itself
            if (!(e instanceof StaticEntity) || e.equals(this))
                continue;
            if (e.getCollisionRectangle(0f, 0f)
                 .intersects(getCollisionRectangle(xOffset, yOffset))) {
                return true;
            }
        }
        return false;
    }

    /** Returns the entity immediately below if any. */
    public Entity findEntityBelow() {
        Rectangle bottom = new Rectangle((int) getLeftBound() + 1,
                                         (int) getBottomBound() + 1,
                                         bounds.width - 2, 1);
        for (int i = 0; i < handler.getEntities().size(); i++) {
            Entity e = handler.getEntities().get(i);
            if (e instanceof CollectableItem || e instanceof ExitItem)
                continue;
            if (e.getCollisionRectangle(0f, 0f)
                 .intersects(bottom))
                return e;
        }

        return null;
    }

    /** Returns the collision rectangle at the given position offset. */
    protected Rectangle getCollisionRectangle(float xOffset, float yOffset) {
        return new Rectangle((int) (x + bounds.x + xOffset),
                             (int) (y + bounds.y + yOffset),
                             bounds.width, bounds.height);
    }

    /** Returns whether there is a collision with a solid tile. */
    protected boolean collisionWithTile(float x, float y) {
        return handler.getWorld().getTile((int) x, (int) y).isSolid();
    }

    /** Returns whether there is a collision with any tile in the interval. */
    protected boolean collisionWithTiles(float xMin, float xMax,
                                         float yMin, float yMax) {
        for (int x = (int) xMin; x <= (int) xMax; x++) {
            for (int y = (int) yMin; y <= (int) yMax; y++)
                if (collisionWithTile(x, y))
                    return true;
        }
        return false;
    }

    /** Returns whether the entity is currently outside of the visible area. */
    public boolean isOffScreen() {
        return getGameX() + width < 0 || getGameX() > Game.WIDTH ||
               getGameY() + height < 0 || getGameY() > Game.HEIGHT;
    }

    // getters and setters

    /** Returns the horizontal coordinate of the entity in pixels. */
    public float getX() {
        return x;
    }

    /** Returns the horizontal game coordinate of the entity in pixels. */
    public int getGameX() {
        return (int) x - handler.getCamera().getxOffset();
    }

    /** Returns the horizontal movement of the entity in pixels. */
    public float getXMove() {
        return xMove;
    }

    /** Returns the vertical coordinate of the entity in pixels. */
    public float getY() {
        return y;
    }

    /** Returns the vertical game coordinate of the entity in pixels. */
    public int getGameY() {
        return (int) y - handler.getCamera().getyOffset();
    }

    /** Returns the vertical movement of the entity in pixels. */
    public float getYMove() {
        return yMove;
    }

    /** Sets the entity coordinates in pixels. */
    public void setPosition(float x, float y) {
        this.x = x;
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

    /** Returns whether the entity should be removed. */
    public boolean shouldRemove() {
        return false;
    }

    /** Sets the collision bounding box. */
    protected void setBounds(int x, int y, int width, int height) {
        bounds.x = x;
        bounds.y = y;
        bounds.width = width;
        bounds.height = height;
    }
}
