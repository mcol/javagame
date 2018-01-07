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
            float dy = getMovementY(yMove);
            while (dy != 0) {
                y += dy;
                dy = getMovementY(yMove);
            }
            findSolidGround = false;
        }

        // check if it's possible to move down
        if (hasGravity) {
            if (!collisionWithEntity(0f, DEFAULT_SPEED)) {
                yMove = DEFAULT_SPEED;
                y += getMovementY(yMove);
            }
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
        else
            x += dx;

        // apply movement from entity below if any
        Entity e = findEntityBelow();
        if (e != null) {
            x += e.getXMove();
            y += e.getYMove();
        }
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

    /** Checks if the enemy collides with the player. */
    private void checkPlayerDamage() {

        if (now - damageCheckTime < DAMAGE_CHECK_INTERVAL)
            return;

        Player p = handler.getPlayer();

        // the player can't be damaged by an enemy immediately below
        if (p.getEntityBelow() == this)
            return;

        if (p.getCollisionRectangle(p.getXMove(), p.getYMove())
             .intersects(getCollisionRectangle(xMove, yMove))) {
            p.setDamage(damage);
            this.setDamage(1);
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
            handler.addMessage("" + score, x, y);
            return true;
        }

        return false;
    }
}
