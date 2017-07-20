package assets.entities;

import java.awt.Color;
import java.awt.Graphics;
import assets.Assets;
import game.Handler;
import gfx.Animation;
import utils.Utils;

public class Enemy extends Creature {

    /** Interval between each damage check in ticks. */
    private static final int DAMAGE_CHECK_INTERVAL = 25;

    /** Health when spawned. */
    private static final int spawnHealth = 25;

    /** Health bar colour. */
    private static final Color healthColor = new Color(0x555555);

    /** Time of the last switch of direction. */
    protected long switchTime;

    /** Whether the enemy has just been spawned. */
    private boolean justSpawned;

    /** Animations. */
    private final Animation animMoving, animFurious;

    /** Constructor. */
    public Enemy(Handler handler, int x, int y) {
        super(handler, x, y, 90, 64);

        // bounding box
        setBounds(5, 8, width - 11, height - 10);

        // animations
        animMoving = new Animation(Assets.enemy_moving, 125);
        animFurious = new Animation(Assets.enemy_furious, 125);

        animation = animMoving;
        justSpawned = true;
        yMove = FALL_SPEED;
        xMove = DEFAULT_SPEED;
        health = spawnHealth;
        switchTime = 0;
    }

    @Override
    public void tick() {

        super.tick();

        if (isDead())
            return;

        // animation
        if (health < LOW_HEALTH) {
            animation = animFurious;
            xMove *= 1.03;
            xMove = Utils.clampValue(xMove, -2 * (int) DEFAULT_SPEED, 2 * (int) DEFAULT_SPEED);
        }

        // find solid ground
        if (justSpawned) {
            float dy = getMovementY();
            while (dy != 0) {
                y += dy;
                dy = getMovementY();
            }
            justSpawned = false;
        }

        // check if it's possible to move down
        if (!collisionWithEntity(0f, FALL_SPEED)) {
            yMove = FALL_SPEED;
            y += getMovementY();
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
        super.render(g);

        // health status
        final int xAdj = facingRight ? width / 2 : width / 8;
        final int health = getHealth();
        final double hbar = (double) health / spawnHealth * 35;
        g.setColor(healthColor);
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
        if (health <= 0) {
            health = 0;
            xMove = 0;
            animation.setFrames(Assets.enemy_dying, 100);
            handler.getPlayer().increaseScore(6);

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
            handler.getWorld()
                   .getEntityManager()
                   .addEntity(new CollectableItem(Items.randomItem(),
                                                  (int) x, (int) y));
            return true;
        }

        return false;
    }
}
