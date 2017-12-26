package assets.entities;

import java.awt.Graphics;
import assets.Assets;
import gfx.Animation;
import utils.Utils;

public class Missile extends Creature {

    /** Damage procured by the missile. */
    private static final int MISSILE_DAMAGE = 10;

    /** Interval before the animation should start in ticks. */
    private static final int ANIMATION_WAIT_INTERVAL = 15;

    /** Acceleration of the missile. */
    private final float speedIncrease;

    /** Whether the missile needs to show the explosion animation. */
    private boolean explosion;

    /** Constructor. */
    public Missile(float x, float y, boolean facingRight) {
        super(handler, x, y, 50, 26);

        // missile parameters
        this.facingRight = facingRight;
        this.speedIncrease = 1 + Utils.randomFloat(0.01f, 0.05f);
        this.explosion = false;

        // bounding box
        if (facingRight) {
            this.x += 65;
            this.y += 20;
            setBounds(33, 5, 15, 15);
        }
        else {
            this.x += 10;
            this.y += 20;
            setBounds(2, 4, 15, 15);
        }

        // animation
        animation = new Animation(Assets.missile_moving, 100);

        // movement
        xMove = facingRight ? FAST_SPEED : -FAST_SPEED;
        xMove *= Utils.randomFloat(1.0f, 1.2f);
        yMove = -FAST_SPEED / Utils.randomFloat(1.2f, 6);
    }

    /** Checks if the missile collides with the player. */
    private boolean checkDamageToPlayer() {
        Player p = handler.getPlayer();

        if (p.getCollisionRectangle(p.getXMove(), p.getYMove())
             .intersects(getCollisionRectangle(xMove, yMove))) {
            p.setDamage(MISSILE_DAMAGE);
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (now - spawnTime < ANIMATION_WAIT_INTERVAL || explosion)
            return;

        // increase speed
        xMove *= speedIncrease;
        yMove *= speedIncrease;

        // move in one direction before the other to deal with corner collisions
        float dx = getMovementX();
        x += dx;
        float dy = getMovementY();
        y += dy;

        // check if the missile has collided with anything
        boolean hit = checkDamageToPlayer();
        if (dx == 0 || dy == 0 || hit || collisionWithEntity(xMove, yMove))
            setExplosion();
    }

    @Override
    public void render(Graphics g) {
        if (now - spawnTime < ANIMATION_WAIT_INTERVAL)
            return;
        super.render(g);
    }

    /** Sets the explosion animation. */
    private void setExplosion() {
        explosion = true;
        animation.setFrames(Assets.missile_explosion, 100);
        setBounds(0, 0, 0, 0);
    }

    /** Returns whether the missile should be removed. */
    @Override
    public boolean shouldRemove() {
        return explosion && animation.hasPlayedOnce();
    }
}
