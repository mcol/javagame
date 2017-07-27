package assets.entities;

import java.awt.Graphics;
import assets.tiles.Tile;
import game.Handler;
import gfx.Animation;

public abstract class Creature extends Entity {

    public static final int DEFAULT_HEALTH = 40;
    public static final int DEFAULT_DAMAGE = 5;
    public static final float DEFAULT_SPEED = 3.0f,
                              SLOW_SPEED = 2.0f,
                              FAST_SPEED = 6.0f;

    protected int health;
    protected int damage;

    /** Time of the last check for damage in ticks. */
    protected long damageCheckTime;

    // movement
    protected float xMove, yMove;

    /** Whether the creature faces right. */
    protected boolean facingRight;

    /** Animation representing the entity. */
    protected Animation animation;

    /** Animation representing the entity when dead. */
    protected Animation animDying;

    /** Constructor. */
    public Creature(Handler handler, float x, float y, int width, int height) {
        super(handler, x, y, width, height);
        health = DEFAULT_HEALTH;
        damage = DEFAULT_DAMAGE;
        xMove = 0;
        yMove = 0;
        facingRight = true;
        spawnTime = now;
        damageCheckTime = 0;
    }

    /** Computes the allowed movement in the horizontal direction. */
    protected float getMovementX() {

        if (xMove > 0) { // moving right

            // right coordinate of bounding box in tiles
            float tx = (getRightBound() + xMove) / Tile.TILESIZE;

            // check the right edge of the map and align to it
            if (tx > handler.getWorld().getWidth()) {
                return (int) tx * Tile.TILESIZE - getRightBound();
            }

            // check tiles at top right and bottom right corners of the creature's bounding box
            if (!collisionWithTile(tx, getTopBound() / Tile.TILESIZE) &&
                !collisionWithTile(tx, getBottomBound() / Tile.TILESIZE)) {
                return xMove;
            }

            // align the bounding box of the creature to the edge of the tile
            return (int) tx * Tile.TILESIZE - getRightBound() - 1;
        }
        else if (xMove < 0) { // moving left

            // left coordinate of bounding box in tiles
            float tx = (getLeftBound() + xMove) / Tile.TILESIZE;

            // check the left edge of the map and align to it
            if (tx < 0) {
                return -getLeftBound();
            }

            // check tiles at the top left and bottom left corners of the creature's bounding box
            if (!collisionWithTile(tx, getTopBound() / Tile.TILESIZE) &&
                !collisionWithTile(tx, getBottomBound() / Tile.TILESIZE)) {
                return xMove;
            }

            // align the bounding box of the creature to the edge of the tile
            return (int) tx * Tile.TILESIZE + Tile.TILESIZE - getLeftBound();
        }

        // no horizontal movement
        return 0;
    }

    /** Computes the allowed movement in the vertical direction. */
    protected float getMovementY() {

        if (yMove < 0) { // moving up

            // top coordinate of bounding box in tiles
            float ty = (getTopBound() + yMove) / Tile.TILESIZE;

            // check the top edge of the map and align to it
            if (ty < 0) {
                return -getTopBound();
            }

            // check tiles at the top left and top right corners of the creature's bounding box
            if (!collisionWithTile(getLeftBound() / Tile.TILESIZE, ty) &&
                !collisionWithTile(getRightBound() / Tile.TILESIZE, ty)) {
                return yMove;
            }

            // align the bounding box of the creature to the edge of the tile
            return (int) ty * Tile.TILESIZE + Tile.TILESIZE - getTopBound();
        }
        else if (yMove > 0) { // moving down

            // bottom coordinate of bounding box in tiles
            float ty = (getBottomBound() + yMove) / Tile.TILESIZE;

            // check the bottom edge of the map and align to it
            if (ty > handler.getWorld().getHeight()) {
                yMove = 0;
                return (int) ty * Tile.TILESIZE - getBottomBound();
            }

            // check tiles at bottom left and bottom right corners of the creature's bounding box
            if (!collisionWithTile(getLeftBound() / Tile.TILESIZE, ty) &&
                !collisionWithTile(getRightBound() / Tile.TILESIZE, ty)) {
                return yMove;
            }

            // align the bounding box of the creature to the edge of the tile
            yMove = 0;
            return (int) ty * Tile.TILESIZE - getBottomBound() - 1;
        }

        return 0;
    }

    @Override
    public void tick() {
        super.tick();
        animation.tick();
    }

    @Override
    public void render(Graphics g) {

        // avoid unnecessary rendering
        if (offScreen())
            return;

        if (facingRight)
            g.drawImage(animation.getCurrentFrame(),
                        (int) getGameX(), (int) getGameY(),
                        width, height, null);
        else
            g.drawImage(animation.getCurrentFrame(),
                        (int) getGameX() + width, (int) getGameY(),
                        -width, height, null);
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

    /** Returns the desired movement in the horizontal direction. */
    public float getxMove() {
        return xMove;
    }

    /** Returns the desired movement in the vertical direction. */
    public float getyMove() {
        return yMove;
    }

    /** Returns whether the creature is dead. */
    public boolean isDead() {
        return health == 0;
    }

    /** Returns whether the creature should be removed. */
    @Override
    public boolean shouldRemove() {
        return isDead() && animation.hasPlayedOnce();
    }
}
