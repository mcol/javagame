package assets.entities;

import java.awt.Color;
import java.awt.Graphics;
import assets.tiles.Tile;
import game.Handler;

public abstract class Creature extends Entity {

    public static final int DEFAULT_HEALTH = 40,
                            LOW_HEALTH = 10;
    public static final int DEFAULT_DAMAGE = 5;
    public static final float DEFAULT_SPEED = 3.0f,
                              MIN_SPEED = 0.5f,
                              MAX_SPEED = 4.0f,
                              FALL_SPEED = 2.0f,
                              SPEED_CHANGE = 0.1f;

    protected int health;
    protected int damage;

    /** Time of the last check for damage. */
    protected long damageCheckTime;

    // movement
    protected float xMove, yMove;
    protected float speed;

    /** Whether the creature faces right. */
    protected boolean facingRight;

    /** Whether the creature is falling. */
    private boolean falling;

    /** Constructor. */
    public Creature(Handler handler, float x, float y, int width, int height) {
        super(handler, x, y, width, height);
        health = DEFAULT_HEALTH;
        damage = DEFAULT_DAMAGE;
        speed = DEFAULT_SPEED;
        xMove = 0;
        yMove = 0;
        falling = true;
        facingRight = true;
        spawnTime = System.currentTimeMillis();
        damageCheckTime = 0;
    }

    /** Computes the allowed movement in the horizontal direction. */
    protected float getMovementX() {

        if (xMove > 0) { // moving right

            falling = true;

            // right coordinate of bounding box in tiles
            float tx = (getRightBound() + xMove) / Tile.TILEWIDTH;

            // check the right edge of the map and align to it
            if (tx > handler.getWorld().getWidth()) {
                return (int) tx * Tile.TILEWIDTH - getRightBound();
            }

            // check tiles at top right and bottom right corners of the creature's bounding box
            if (!collisionWithTile(tx, getTopBound() / Tile.TILEHEIGHT) &&
                !collisionWithTile(tx, getBottomBound() / Tile.TILEHEIGHT)) {
                return xMove;
            }

            // align the bounding box of the creature to the edge of the tile
            return (int) tx * Tile.TILEWIDTH - getRightBound() - 1;
        }
        else if (xMove < 0) { // moving left

            falling = true;

            // left coordinate of bounding box in tiles
            float tx = (getLeftBound() + xMove) / Tile.TILEWIDTH;

            // check the left edge of the map and align to it
            if (tx < 0) {
                return -getLeftBound();
            }

            // check tiles at the top left and bottom left corners of the creature's bounding box
            if (!collisionWithTile(tx, getTopBound() / Tile.TILEHEIGHT) &&
                !collisionWithTile(tx, getBottomBound() / Tile.TILEHEIGHT)) {
                return xMove;
            }

            // align the bounding box of the creature to the edge of the tile
            return (int) tx * Tile.TILEWIDTH + Tile.TILEWIDTH - getLeftBound();
        }

        // no horizontal movement
        return 0;
    }

    /** Computes the allowed movement in the vertical direction. */
    protected float getMovementY() {

        if (yMove < 0) { // moving up

            falling = true;

            // top coordinate of bounding box in tiles
            float ty = (getTopBound() + yMove) / Tile.TILEHEIGHT;

            // check the top edge of the map and align to it
            if (ty < 0) {
                return -getTopBound();
            }

            // check tiles at the top left and top right corners of the creature's bounding box
            if (!collisionWithTile(getLeftBound() / Tile.TILEWIDTH, ty) &&
                !collisionWithTile(getRightBound() / Tile.TILEWIDTH, ty)) {
                return yMove;
            } else {
                // align the bounding box of the creature to the edge of the tile
                return (int) ty * Tile.TILEHEIGHT + Tile.TILEHEIGHT - y - bounds.y;
            }
        }
        else if (yMove > 0) { // moving down

            // bottom coordinate of bounding box in tiles
            float ty = (getBottomBound() + yMove) / Tile.TILEHEIGHT;

            // check the bottom edge of the map and align to it
            if (ty > handler.getWorld().getHeight()) {
                yMove = 0;
                falling = false;
                return (int) ty * Tile.TILEHEIGHT - getBottomBound();
            }

            // check tiles at bottom left and bottom right corners of the creature's bounding box
            if (!collisionWithTile(getLeftBound() / Tile.TILEWIDTH, ty) &&
                !collisionWithTile(getRightBound() / Tile.TILEWIDTH, ty)) {
                return yMove;
            }
            else {
                // align the bounding box of the creature to the edge of the tile
                yMove = 0;
                falling = false;
                return (int) ty * Tile.TILEHEIGHT - getBottomBound() - 1;
            }
        }

        return 0;
    }

    @Override
    public void render(Graphics g) {

        // avoid unnecessary rendering
        if (offScreen())
            return;

        if (facingRight)
            g.drawImage(animation.getCurrentFrame(),
                        (int) (x - handler.getCamera().getxOffset()),
                        (int) (y - handler.getCamera().getyOffset()),
                        width, height, null);
        else
            g.drawImage(animation.getCurrentFrame(),
                        (int) (x - handler.getCamera().getxOffset() + width),
                        (int) (y - handler.getCamera().getyOffset()),
                        -width, height, null);

        g.setColor(health < LOW_HEALTH ? Color.RED : Color.WHITE);
//        g.fillRect((int) (x + bounds.x - handler.getCamera().getxOffset()),
//                   (int) (y + bounds.y - handler.getCamera().getyOffset()),
//                   bounds.width, bounds.height);
    }

    /** Returns whether there is a collision with a solid tile. */
    protected boolean collisionWithTile(float x, float y) {
        return handler.getWorld().getTile((int) x, (int) y).isSolid();
    }

    /** Decreases the creature's health according to the damage received. */
    protected void setDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
            xMove = 0;
        }
    }

    // getters and setters

    /** Returns the current health of the creature. */
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /** Returns the desired movement in the horizontal direction. */
    public float getxMove() {
        return xMove;
    }

    /** Returns the desired movement in the vertical direction. */
    public float getyMove() {
        return yMove;
    }

    /** Returns whether the creature is falling. */
    public boolean isFalling() {
        return falling;
    }

    /** Sets the creature's falling status. */
    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    /** Whether the creature is dead. */
    public boolean isDead() {
        return health == 0;
    }

    /** Returns whether the creature should be removed. */
    @Override
    public boolean shouldRemove() {
        return isDead() && animation.hasPlayedOnce();
    }
}
