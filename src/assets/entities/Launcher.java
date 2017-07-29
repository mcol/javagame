package assets.entities;

import assets.Assets;
import game.Game;
import gfx.Animation;
import utils.Utils;

public class Launcher extends Enemy {

    /** Damage procured by the enemy. */
    private static final int LAUNCHER_DAMAGE = 12;

    /** Score awarded when killed. */
    private static final int LAUNCHER_SCORE = 20;

    /** Health bar colour. */
    private static final int BAR_COLOUR = 0xc8c8b8;

    /** Interval between each launcher fire in ticks. */
    private static final int LAUNCHER_FIRE_INTERVAL = 3 * Game.FPS;

    /** Movement of the launcher. */
    private static final float LAUNCHER_SPEED = 0.00000001f;

    /** Animations. */
    private final Animation animStill, animFiring;

    /** Original horizontal coordinate. */
    private final int origX;

    /** Time of the last launcher fire in ticks. */
    private long fireTime;

    /** Whether the launcher can switch direction. */
    private boolean canSwitch;

    /** Constructor. */
    public Launcher(int x, int y, int health) {
        super(x, y, 125, 100, health, LAUNCHER_DAMAGE, LAUNCHER_SCORE, BAR_COLOUR);

        // enemy parameters
        this.facingRight = Utils.randomBoolean();
        this.switchOnCollision = false;
        this.hasGravity = false;
        this.origX = x;
        this.fireTime = now;
        this.canSwitch = false;

        // bounding box
        setBoundingBox();

        // animations
        animation = new Animation(Assets.launcher_still, 300);
        animStill = new Animation(Assets.launcher_still, 300);
        animFiring = new Animation(Assets.launcher_firing, 100);
        animDying = new Animation(Assets.launcher_dying, 150);
    }

    @Override
    public float getMovementX() {
        return LAUNCHER_SPEED;
    }

    @Override
    public void tick() {
        super.tick();

        if (isDead())
            return;

        switchDirection();
        fireLauncher();
    }

    /** Fires a missile from the launcher. */
    private void fireLauncher() {
        if (now - fireTime > LAUNCHER_FIRE_INTERVAL) {
            animation.assign(animFiring);
            Missile m = new Missile(x, y, facingRight);
            handler.getEntityManager().addEntity(m);
            fireTime = now;
            canSwitch = true;
        }
        else if (animation.hasPlayedOnce()) {
            animation.assign(animStill);
        }
    }

    /** Switches the direction of the launcher at random. */
    private void switchDirection() {
        if (canSwitch && now - fireTime > LAUNCHER_FIRE_INTERVAL / 2) {
            facingRight = Utils.randomBoolean();
            setBoundingBox();
            canSwitch = false;
        }
    }

    /** Sets the collision bounding box. */
    private void setBoundingBox() {
        if (facingRight) {
            x = origX;
            setBounds(3, 38, 70, height - 40);
        }
        else {
            x = origX - 48;
            setBounds(52, 38, 70, height - 40);
        }
    }
}
