package assets.entities;

import java.awt.Color;
import java.awt.Graphics;
import gfx.Animation;
import utils.Utils;

public abstract class Enemy extends Creature {

    /** Interval between each damage check in ticks. */
    private static final int DAMAGE_CHECK_INTERVAL = 25;

    /** Score awarded when killed. */
    protected int score;

    /** Health bar colour. */
    protected final Color colour;

    /** Time of the last switch of direction. */
    protected long switchTime;

    /** Animation when in a frenzy state. */
    protected Animation animFrenzy;

    /** Threshold health below which the enemy goes into a frenzy state. */
    protected int frenzyThreshold;

    /** Whether the enemy is affected by gravity. */
    protected boolean hasGravity;

    /** Whether the enemy needs to find the ground. */
    protected boolean findSolidGround;

    /** Constructor. */
    public Enemy(int x, int y, int width, int height,
                 int health, int score, Color colour) {
        super(handler, x, y, width, height);
        this.health = health;
        this.score = score;
        this.colour = colour;
        this.switchTime = 0;
        this.frenzyThreshold = 0;
        this.hasGravity = true;
        this.findSolidGround = true;

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
            float dy = getMovementY();
            while (dy != 0) {
                y += dy;
                dy = getMovementY();
            }
            findSolidGround = false;
        }

        // check if it's possible to move down
        if (hasGravity) {
            if (!collisionWithEntity(0f, DEFAULT_SPEED)) {
                yMove = DEFAULT_SPEED;
                y += getMovementY();
            }
        }

        // frenzy state
        if (health < frenzyThreshold) {
            animation = animFrenzy;
            xMove *= 1.03;
            xMove = Utils.clampValue(xMove, (int) -FAST_SPEED, (int) FAST_SPEED);
        }

        // compute the allowed horizontal movement
        float dx = getMovementX();

        // switch direction if no movement is possible or there is a collision with another entity
        if ((dx == 0 || collisionWithEntity(xMove, 0f)) &&
                now - switchTime > 5) {
            facingRight = !facingRight;
            xMove = -xMove;
            switchTime = now;
        }
        else
            x += dx;

        checkPlayerDamage();
    }

    @Override
    public void render(Graphics g) {

        // avoid unnecessary rendering
        if (offScreen())
            return;

        super.render(g);

        // health status
        final int xAdj = facingRight ? width / 2 : width / 8;
        final int health = getHealth();
        final double hbar = (double) health / DEFAULT_HEALTH * 35;
        g.setColor(colour);
        g.fillRect((int) (x - handler.getCamera().getxOffset() + xAdj),
                   (int) (y - handler.getCamera().getyOffset() - 10),
                   health > 0 ? (int) hbar + 5 : 0, 10);
    }

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

    /** Checks if the enemy collides with the player. */
    private void checkPlayerDamage() {

        if (now - damageCheckTime < DAMAGE_CHECK_INTERVAL)
            return;

        Player p = handler.getPlayer();
        if (p.getCollisionRectangle(p.getxMove(), p.getyMove())
             .intersects(getCollisionRectangle(xMove, yMove))) {
            p.setDamage(damage);
            damageCheckTime = now;
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
            return true;
        }

        return false;
    }
}
