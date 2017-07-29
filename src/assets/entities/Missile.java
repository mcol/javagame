package assets.entities;

import java.awt.Graphics;
import assets.Assets;
import gfx.Animation;

public class Missile extends Creature {

    /** Damage procured by the missile. */
    private static final int MISSILE_DAMAGE = 10;

    /** Interval before the animation should start in ticks. */
    private static final int ANIMATION_WAIT_INTERVAL = 15;

    /** Whether the missile needs to show the explosion animation. */
    private boolean explosion;

    /** Constructor. */
    public Missile(float x, float y, boolean facingRight) {
        super(handler, x, y, 50, 26);

        // missile parameters
        this.facingRight = facingRight;
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
        yMove = -FAST_SPEED / 2;
    }

    /** Checks if the missile collides with the player. */
    private void checkDamageToPlayer() {
        Player p = handler.getPlayer();
        if (p.getCollisionRectangle(p.getxMove(), p.getyMove())
             .intersects(getCollisionRectangle(xMove, yMove))) {
            p.setDamage(MISSILE_DAMAGE);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (now - spawnTime < ANIMATION_WAIT_INTERVAL || explosion)
            return;

        // increase speed
        xMove *= 1.01f;
        yMove *= 1.01f;

        // move in one direction before the other to deal with corner collisions
        float dx = getMovementX();
        x += dx;
        float dy = getMovementY();
        y += dy;
        if (dx == 0 || dy == 0 || collisionWithEntity(xMove, yMove))
            setExplosion();

        checkDamageToPlayer();
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
