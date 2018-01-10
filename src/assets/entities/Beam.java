package assets.entities;

import assets.Assets;
import gfx.Animation;

public class Beam extends Creature {

    /** Damage procured by the beam. */
    private static final int BEAM_DAMAGE = 2;

    /** Movement of the beam. */
    private static final float BEAM_SPEED = 0;

    /** Laser firing the beam. */
    private final Laser laser;

    /** Whether the laser is facing right. */
    private final boolean facingRight;

    /** Horizontal coordinate of the laser nozzle. */
    private float xOrig;

    /** Constructor. */
    public Beam(Laser laser) {
        super(handler, 0, 0, 0, 5);

        // enemy parameters
        this.laser = laser;
        this.facingRight = laser.getFacingRight();
        this.damage = BEAM_DAMAGE;

        // animation
        animation = new Animation(Assets.beams, 50);

        // movement
        xMove = BEAM_SPEED;
        yMove = BEAM_SPEED;
    }

    @Override
    public void tick() {
        super.tick();
        computeWidth();
        checkPlayerDamage();
    }

    /** Computes the width of the beam. */
    private void computeWidth() {
        xOrig = facingRight ? laser.getRightBound() : laser.getLeftBound();
        y = laser.getY() + 10;
        width = (int) Math.min(distanceToEntityX(xOrig, y, facingRight),
                               distanceToTileX(xOrig, y, facingRight));
        bounds.width = width;
        x = facingRight ? xOrig : xOrig - width;
    }

    // no collisions with anything
    @Override
    protected boolean collisionWithEntity(float xOffset, float yOffset) {
        return false;
    }

    // getters and setters

    /** Returns the horizontal movement of the beam in pixels. */
    @Override
    public float getXMove() {
        return laser.getXMove();
    }

    /** Returns the vertical movement of the beam in pixels. */
    @Override
    public float getYMove() {
        return laser.getYMove();
    }

    /** Whether the beam should be removed. */
    @Override
    public boolean shouldRemove() {
        return laser.isDead();
    }
}
