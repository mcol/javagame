package assets.entities;

import assets.Assets;
import gfx.Animation;
import utils.Utils;

public class Laser extends Enemy {

    /** Damage procured by the enemy. */
    private static final int LASER_DAMAGE = 10;

    /** Score awarded when killed. */
    private static final int LASER_SCORE = 15;

    /** Health bar colour. */
    private static final int BAR_COLOUR = 0x9beb7a;

    /** Movement of the laser. */
    private static final float LASER_SPEED = 0;

    /** Beam fired by the laser. */
    private Beam beam;

    /** Constructor. */
    public Laser(int x, int y, int health) {
        super(x, y, 50, 46, health, LASER_DAMAGE, LASER_SCORE, BAR_COLOUR);

        // enemy parameters
        this.facingRight = Utils.randomBoolean();
        this.imageShift = 3;
        this.switchOnCollision = false;
        this.hasGravity = true;

        // bounding box
        setBounds(12, 3, width - 20, height - 5);

        // animations
        animation = new Animation(Assets.laser_still, 1000);
        animDying = new Animation(Assets.laser_dying, 150);

        // movement
        xMove = LASER_SPEED;
    }

    @Override
    protected void moveToSolidGround() {
        super.moveToSolidGround();

        // start the laser beam
        beam = new Beam(this);
        handler.getEntityManager().addEntity(beam);
    }

    @Override
    public float getMovementX(float xMove) {
        return LASER_SPEED;
    }
}
