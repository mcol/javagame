package assets.entities;

import java.awt.Color;
import java.awt.Graphics;
import gfx.Animation;
import utils.Utils;

public abstract class Enemy extends Creature {

    /** Score awarded when killed. */
    protected int score;

    /** Health bar colour. */
    protected final Color colour;

    /** Animation when in a frenzy state. */
    protected Animation animFrenzy;

    /** Whether the enemy can switch direction when there is a collision. */
    protected boolean switchOnCollision;

    /** Time of the last switch of direction. */
    private long switchTime;

    /** Threshold health below which the enemy goes into a frenzy state. */
    protected int frenzyThreshold;

    /** Speed of movement when in a frenzy state. */
    protected float frenzySpeed;

    /** Damage produced when in a frenzy state. */
    protected int frenzyDamage;

    /** Whether the enemy is affected by gravity. */
    protected boolean hasGravity;

    /** Whether the enemy needs to find the ground. */
    protected boolean findSolidGround;

    /** Constructor. */
    public Enemy(int x, int y, int width, int height,
                 int health, int damage, int score, int colour) {
        super(handler, x, y, width, height);

        // enemy parameters
        this.health = health;
        this.damage = damage;
        this.score = score;
        this.colour = new Color(colour);
        this.switchOnCollision = true;
        this.switchTime = 0;
        this.frenzyThreshold = 0;
        this.frenzySpeed = FAST_SPEED;
        this.frenzyDamage = (int) (1.5 * damage);
        this.hasGravity = true;
        this.findSolidGround = true;

        // movement
        yMove = DEFAULT_SPEED;
        xMove = DEFAULT_SPEED;
    }

    @Override
    public void tick() {
        super.tick();

        if (isDead())
            return;

        // find solid ground
        if (findSolidGround) {
            moveToSolidGround();
            findSolidGround = false;
        }

        // frenzy state
        if (health < frenzyThreshold) {
            animation = animFrenzy;
            damage = frenzyDamage;
            xMove = Utils.clampAbsValue(xMove * 1.03f, frenzySpeed);
        }

        // compute the allowed horizontal movement
        float dx = getMovementX(xMove);

        // switch direction if no movement is possible or there is a collision with another entity
        if (switchOnCollision && (dx == 0 || collisionWithEntity(xMove, 0f)) &&
                now - switchTime > 5) {
            facingRight = !facingRight;
            xMove = -xMove;
            switchTime = now;
        }

        move();
        checkPlayerDamage();
    }

    @Override
    public void render(Graphics g) {

        // avoid unnecessary rendering
        if (isOffScreen())
            return;

        super.render(g);

        // health status
        final int xAdj = bounds.x + bounds.width / 2 + (facingRight ? 0 : -10);
        final int health = getHealth();
        final double hbar = (double) health / DEFAULT_HEALTH * 35;
        g.setColor(colour);
        g.fillRect(getGameX() + xAdj, getGameY() + bounds.y - 20,
                   health > 0 ? (int) hbar + 5 : 0, 10);
    }

    /** Moves the enemy. */
    private void move() {
        float xDir = xMove;
        float yDir = hasGravity ? DEFAULT_SPEED : yMove;

        // apply movement from the entity immediately below if any
        if (entityBelow != null) {
            xDir += entityBelow.getXMove();
            yDir = entityBelow.getYMove();
        }

        if (!collisionWithEntity(xDir, 0f))
            x += getMovementX(xDir);
        if (!collisionWithEntity(0f, yDir))
            y += getMovementY(yDir);
    }

    /** Moves the enemy vertically until it finds solid ground. */
    protected void moveToSolidGround() {
        float dy = getMovementY(yMove);
        while (dy != 0) {
            y += dy;
            dy = getMovementY(yMove);
        }
    }

    /** Decreases the enemy's health according to the damage received. */
    @Override
    protected void setDamage(int damage) {

        // avoid receiving damage after being dead
        if (isDead())
            return;

        health -= damage;

        // dying state
        if (health <= 0) {
            health = 0;
            xMove = 0;
            animation.assign(animDying);
            handler.getPlayer().increaseScore(score);

            // avoid generating collisions
            setBounds(0, 0, 0, 0);
        }
    }

    /** Returns whether the creature should be removed. */
    @Override
    public boolean shouldRemove() {
        if (isDead() && animation.hasPlayedOnce()) {

            // drop an item
            handler.getEntityManager()
                   .addEntity(new CollectableItem(Items.randomItem(),
                                                  (int) x, (int) y));
            handler.addMessage("" + score, x, y);
            return true;
        }

        return false;
    }
}
