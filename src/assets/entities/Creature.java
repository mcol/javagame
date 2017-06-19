package assets.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import assets.tiles.Tile;
import game.Handler;
import gfx.Animation;

public abstract class Creature extends Entity {

    public static final int DEFAULT_HEALTH = 10;
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

    /** Time of the last switch of direction. */
    protected long switchTime;

    // movement
    protected float xMove, yMove;
    protected float speed;
    protected boolean facingRight;
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
        switchTime = System.currentTimeMillis();

        // placeholder empty animation
        animation = new Animation(new BufferedImage[1], 1);
    }

    public void move() {
        if (!collidesWithEntity(xMove, 0f))
            x += getMovementX();
        if (!collidesWithEntity(0f, yMove))
            y += getMovementY();
        else if (yMove > 0) {
            // stop falling when colliding vertically with an entity
            falling = false;
        }
    }

    /** Computes the allowed movement in the horizontal direction. */
    public float getMovementX() {

        if (xMove > 0) { // moving right

            falling = true;

            // right coordinate of bounding box in tiles
            float tx = (x + xMove + bounds.x + bounds.width) / Tile.TILEWIDTH;

            // check the right edge of the map and align to it
            if (tx > handler.getWorld().getWidth()) {
                return (int) tx * Tile.TILEWIDTH - x - bounds.x - bounds.width;
            }

            // check tiles at top right and bottom right corners of the creature's bounding box
            if (!collisionWithTile(tx, (y + bounds.y) / Tile.TILEHEIGHT) &&
                !collisionWithTile(tx, (y + bounds.y + bounds.height) / Tile.TILEHEIGHT)) {
                return xMove;
            }

            // align the bounding box of the creature to the edge of the tile
            return (int) tx * Tile.TILEWIDTH - bounds.x - bounds.width - 1 - x;
        }
        else if (xMove < 0) { // moving left

            falling = true;

            // left coordinate of bounding box in tiles
            float tx = (x + xMove + bounds.x) / Tile.TILEWIDTH;

            // check the left edge of the map and align to it
            if (tx < 0) {
                return 0 - x - bounds.x;
            }

            // check tiles at the top left and bottom left corners of the creature's bounding box
            if (!collisionWithTile(tx, (y + bounds.y) / Tile.TILEHEIGHT) &&
                !collisionWithTile(tx, (y + bounds.y + bounds.height) / Tile.TILEHEIGHT)) {
                return xMove;
            }

            // align the bounding box of the creature to the edge of the tile
            return (int) tx * Tile.TILEWIDTH + Tile.TILEWIDTH - bounds.x - x;
        }

        // no horizontal movement
        return 0;
    }

    /** Computes the allowed movement in the vertical direction. */
    public float getMovementY() {

        if (yMove < 0) { // moving up

            falling = true;

            // top coordinate of bounding box in tiles
            float ty = (y + yMove + bounds.y) / Tile.TILEHEIGHT;

            // check the top edge of the map and align to it
            if (ty < 0) {
                return 0 - y - bounds.y;
            }

            // check tiles at the top left and top right corners of the creature's bounding box
            if (!collisionWithTile((x + bounds.x) / Tile.TILEWIDTH, ty) &&
                !collisionWithTile((x + bounds.x + bounds.width) / Tile.TILEWIDTH, ty)) {
                return yMove;
            } else {
                // align the bounding box of the creature to the edge of the tile
                return (int) ty * Tile.TILEHEIGHT + Tile.TILEHEIGHT - bounds.y - y;
            }
        }
        else if (yMove > 0) { // moving down

            // bottom coordinate of bounding box in tiles
            float ty = (y + yMove + bounds.y + bounds.height) / Tile.TILEHEIGHT;

            // check the bottom edge of the map and align to it
            if (ty > handler.getWorld().getHeight()) {
                yMove = 0;
                falling = false;
                return (int) ty * Tile.TILEHEIGHT - y - bounds.y - bounds.height;
            }

            // check tiles at bottom left and bottom right corners of the creature's bounding box
            if (!collisionWithTile((x + bounds.x) / Tile.TILEWIDTH, ty) &&
                !collisionWithTile((x + bounds.x + bounds.width) / Tile.TILEWIDTH, ty)) {
                return yMove;
            }
            else {
                // align the bounding box of the creature to the edge of the tile
                yMove = 0;
                falling = false;
                return (int) ty * Tile.TILEHEIGHT - bounds.y - bounds.height - 1 - y;
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
            g.drawImage(getCurrentAnimationFrame(),
                        (int) (x - handler.getCamera().getxOffset()),
                        (int) (y - handler.getCamera().getyOffset()),
                        width, height, null);
        else
            g.drawImage(getCurrentAnimationFrame(),
                        (int) (x - handler.getCamera().getxOffset() + width),
                        (int) (y - handler.getCamera().getyOffset()),
                        -width, height, null);

        g.setColor(Color.RED);
//        g.fillRect((int) (x + bounds.x - handler.getCamera().getxOffset()),
//                   (int) (y + bounds.y - handler.getCamera().getyOffset()),
//                   bounds.width, bounds.height);
    }

    protected BufferedImage getCurrentAnimationFrame() {
        return animation.getCurrentFrame();
    };

    /** Returns whether there is a collision with a solid tile. */
    protected boolean collisionWithTile(float x, float y) {
        return handler.getWorld().getTile((int) x, (int) y).isSolid();
    }

    protected void setDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
            xMove = 0;
        }
    }

    // getters and setters

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

    public float getxMove() {
        return xMove;
    }

    public boolean isFalling() {
        return falling;
    }

    /** Whether the creature is dead. */
    public boolean isDead() {
        return health == 0;
    }

    /** Whether the creature should be removed. */
    @Override
    public boolean shouldRemove() {
        return isDead() && animation.hasPlayedOnce();
    }
}
