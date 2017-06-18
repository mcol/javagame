package assets.entities;

import assets.Assets;
import game.Handler;
import gfx.Animation;
import utils.Utils;

public class Enemy extends Creature {

    private boolean justSpawned;

    // Animations
    private Animation animMoving, animFurious;

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
        health = 25;
    }

    @Override
    public void tick() {

        super.tick();

        if (isDead())
            return;

        // animation
        if (health < 15) {
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
        if (!collidesWithEntity(0f, FALL_SPEED) ) {
            yMove = FALL_SPEED;
            y += getMovementY();
        }

        // compute the allowed horizontal movement
        float dx = getMovementX();

        // switch direction if no movement is possible or there is a collision with another entity
        if ((dx == 0 || collidesWithEntity(xMove, 0f)) &&
                System.currentTimeMillis() - switchTime > 150) {
            facingRight = !facingRight;
            xMove = -xMove;
            switchTime = System.currentTimeMillis();
        }
        else
            x += dx;
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

            // avoid generating collisions
            setBounds(0, 0, 0, 0);
        }
    }
}
