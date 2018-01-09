package assets.entities;

import java.awt.Graphics;
import assets.tiles.Tile;
import game.Handler;
import gfx.Animation;

public abstract class Creature extends Entity {

    /** Initial health amount. */
    public static final int DEFAULT_HEALTH = 40;

    /** Available movement speeds. */
    public static final float DEFAULT_SPEED = 3.0f,
                              SLOW_SPEED = 2.0f,
                              FAST_SPEED = 6.0f;

    /** Interval between each damage check in ticks. */
    private static final int DAMAGE_CHECK_INTERVAL = 25;

    /** Health status. */
    protected int health;

    /** Amount of damage produced. */
    protected int damage;

    /** Time of the last check for damage in ticks. */
    protected long damageCheckTime;

    /** Whether the creature faces right. */
    protected boolean facingRight;

    /** Shift of the image in pixels to be applied when it's mirrored. */
    protected int imageShift;

    /** Animation representing the entity. */
    protected Animation animation;

    /** Animation representing the entity when dead. */
    protected Animation animDying;

    /** Constructor. */
    public Creature(Handler handler, float x, float y, int width, int height) {
        super(handler, x, y, width, height);
        health = DEFAULT_HEALTH;
        facingRight = true;
        imageShift = 0;
        damageCheckTime = 0;
    }

    /** Computes the allowed movement in the horizontal direction. */
    protected float getMovementX(float xMove) {

        if (xMove > 0) { // moving right

            // right coordinate of bounding box in tiles
            float tx = (getRightBound() + xMove) / Tile.TILESIZE;

            // check the right edge of the map and align to it
            if (tx > handler.getWorld().getWidth()) {
                return (int) tx * Tile.TILESIZE - getRightBound();
            }

            // check tiles at right of the creature's bounding box
            if (!collisionWithTiles(tx, tx,
                                    getTopBound() / Tile.TILESIZE,
                                    getBottomBound() / Tile.TILESIZE)) {
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

            // check tiles at the left of the creature's bounding box
            if (!collisionWithTiles(tx, tx,
                                    getTopBound() / Tile.TILESIZE,
                                    getBottomBound() / Tile.TILESIZE)) {
                return xMove;
            }

            // align the bounding box of the creature to the edge of the tile
            return (int) tx * Tile.TILESIZE + Tile.TILESIZE - getLeftBound();
        }

        // no horizontal movement
        return 0;
    }

    /** Computes the allowed movement in the vertical direction. */
    protected float getMovementY(float yMove) {

        if (yMove < 0) { // moving up

            // top coordinate of bounding box in tiles
            float ty = (getTopBound() + yMove) / Tile.TILESIZE;

            // check the top edge of the map and align to it
            if (ty < 0) {
                return -getTopBound();
            }

            // check tiles at the top of the creature's bounding box
            if (!collisionWithTiles(getLeftBound() / Tile.TILESIZE,
                                    getRightBound() / Tile.TILESIZE,
                                    ty, ty)) {
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
                this.yMove = 0;
                return (int) ty * Tile.TILESIZE - getBottomBound();
            }

            // check tiles at bottom of the creature's bounding box
            if (!collisionWithTiles(getLeftBound() / Tile.TILESIZE,
                                    getRightBound() / Tile.TILESIZE,
                                    ty, ty)) {
                return yMove;
            }

            // align the bounding box of the creature to the edge of the tile
            this.yMove = 0;
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
        if (isOffScreen())
            return;

        if (facingRight)
            g.drawImage(animation.getCurrentFrame(),
                        getGameX(), getGameY(),
                        width, height, null);
        else
            g.drawImage(animation.getCurrentFrame(),
                        getGameX() + width + imageShift, getGameY(),
                        -width, height, null);
    }

    /** Decreases the creature's health according to the damage received. */
    protected void setDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
            xMove = 0;
        }
    }

    /** Checks if the creature damages the player. */
    protected void checkPlayerDamage() {

        if (now - damageCheckTime < DAMAGE_CHECK_INTERVAL)
            return;

        Player p = handler.getPlayer();

        // the player can't be damaged by an entity immediately below
        if (p.getEntityBelow() == this)
            return;

        if (p.getCollisionRectangle(p.getXMove(), p.getYMove())
             .intersects(getCollisionRectangle(xMove, yMove))) {
            p.setDamage(damage);
            this.setDamage(1);
            damageCheckTime = now;
        }
    }

    // getters and setters

    /** Returns the current health of the creature. */
    public int getHealth() {
        return health;
    }

    /** Sets the health status of the creature. */
    public void setHealth(int health) {
        this.health = health;
    }

    /** Returns whether the creature is facing right. */
    public boolean getFacingRight() {
        return facingRight;
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
