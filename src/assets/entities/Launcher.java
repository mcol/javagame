package assets.entities;

import assets.Assets;
import game.Game;
import gfx.Animation;
import utils.Utils;

public class Launcher extends Enemy {

    /** Damage procured by the enemy. */
    private static final int LAUNCHER_DAMAGE = 7;

    /** Score awarded when killed. */
    private static final int LAUNCHER_SCORE = 20;

    /** Health bar colour. */
    private static final int BAR_COLOUR = 0xc8c8b8;

    /** Movement of the launcher. */
    private static final float LAUNCHER_SPEED = 0.00000001f;

    /** Animation states. */
    private final Animation animStill, animFiring;

    /** Time of the next launcher fire in ticks. */
    private long nextFireTime;

    /** Time of the next possible switch of direction. */
    private long nextSwitchTime;

    /** Whether the launcher can switch direction. */
    private boolean canSwitch;

    /** Constructor. */
    public Launcher(int x, int y, int health) {
        super(x, y, 125, 100, health, LAUNCHER_DAMAGE, LAUNCHER_SCORE, BAR_COLOUR);

        // enemy parameters
        this.facingRight = Utils.randomBoolean();
        this.imageShift = -48;
        this.switchOnCollision = false;
        this.hasGravity = true;
        this.canSwitch = false;
        this.nextFireTime = now + Utils.randomInteger(1, 4) * Game.FPS;
        this.nextSwitchTime = now + (nextFireTime - now) * 2 / 3;

        // bounding box
        setBounds(3, 38, 70, height - 40);

        // animations
        animation = new Animation(Assets.launcher_still, 300);
        animStill = new Animation(Assets.launcher_still, 300);
        animFiring = new Animation(Assets.launcher_firing, 100);
        animDying = new Animation(Assets.launcher_dying, 150);

        // movement
        xMove = 0;
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
        if (now > nextFireTime) {
            animation.assign(animFiring);
            float x = this.x + (facingRight ? 0 : imageShift);
            Missile m = new Missile(x, y, facingRight);
            handler.getEntityManager().addEntity(m);
            nextFireTime = now + Utils.randomInteger(1, 4) * Game.FPS;
            nextSwitchTime = now + (nextFireTime - now) * 2 / 3;
            canSwitch = true;
        }
        else if (animation.hasPlayedOnce()) {
            animation.assign(animStill);
        }
    }

    /** Switches the direction of the launcher at random. */
    private void switchDirection() {
        if (canSwitch && now > nextSwitchTime) {
            facingRight = Utils.randomBoolean();
            canSwitch = false;
        }
    }
}
