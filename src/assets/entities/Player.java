package assets.entities;

import assets.Assets;
import game.Handler;
import gfx.Animation;

public class Player extends Creature {

    /** Maximum amount of poop that the player can carry. */
    public static final int MAX_POOP = 25;

    /** Interval between each poop increase in milliseconds. */
    public static final long POOP_RESTORE_INTERVAL = 15000;

    private static final float dampingFactor = 0.96f;
    private static final float liftspeed = 1.4f;
    private boolean goingUp = false;
    private boolean canPoop = false;

    // Animations
    private final Animation animFlying, animFalling, animStill;

    /** Current available poop. */
    private int poop;

    /** Whether the player has fired any poop. */
    private boolean hasPooped = false;

    /** Time of the last poop increase. */
    private long poopRestoreTime = 0;

    /** Constructor. */
    public Player(Handler handler, int x, int y) {
        super(handler, x, y, 75, 75);

        // bounding box
        int hPadding = width / 4 + 10;
        setBounds(hPadding, 26, width - hPadding * 2, height - 40);

        // animations
        animFlying = new Animation(Assets.player_flying, 100);
        animStill = new Animation(Assets.player_still, 500);
        animFalling = new Animation(Assets.player_falling, 150);

        // current animation
        animation = animFalling;

        poop = MAX_POOP;
    }

    private void getInput() {

        canPoop = true;

        //
        // horizontal movement
        //
        if (handler.getKeyManager().left) {
            if (facingRight) { // switched direction
                facingRight = false;
                xMove = -xMove / 2;
            }
            xMove -= SPEED_CHANGE;
            if (xMove < -MAX_SPEED)
                xMove = -MAX_SPEED;
            goingUp = true;
            yMove = -liftspeed; // lift up
        }
        else if (handler.getKeyManager().right) {
            if (!facingRight) { // switched direction
                facingRight = true;
                xMove = -xMove / 2;
            }
            xMove += SPEED_CHANGE;
            if (xMove > MAX_SPEED)
                xMove = MAX_SPEED;
            goingUp = true;
            yMove = -liftspeed; // lift up
        }
        else if (xMove != 0) { // slow down the horizontal movement
            xMove *= dampingFactor;
            if (Math.abs(xMove) < MIN_SPEED) {
                xMove = 0;
            }

            // gradual descent unless trying to go up
            if (!handler.getKeyManager().up)
                yMove += SPEED_CHANGE;
        }

        //
        // vertical movement
        //
        if (handler.getKeyManager().up) {
            if (!goingUp) { // switched direction
                goingUp = true;
                yMove = -yMove / 2;
            }
            yMove -= SPEED_CHANGE;
            if (yMove < -MAX_SPEED)
                yMove = -MAX_SPEED;
        }
        else if (handler.getKeyManager().down) {
            canPoop = false;
            if (goingUp) { // switched direction
                goingUp = false;
                yMove = -yMove / 2;
            }
            if (yMove > 0) { // avoid moving down when stationary
                yMove += SPEED_CHANGE;
                if (yMove > MAX_SPEED)
                    yMove = MAX_SPEED;
            }
        }
        else if (yMove < 0) { // slow down the vertical movement
            yMove *= dampingFactor;
            if (yMove > -MIN_SPEED * 2) {
                yMove = SPEED_CHANGE; // start falling
            }
        }
        else { // falling or still
            goingUp = false;
            if (isFalling()) {
                yMove += SPEED_CHANGE;
                if (yMove > FALL_SPEED) {
                    yMove = FALL_SPEED;
                }
            }
            else if (xMove == 0 && yMove >= 0) {
                canPoop = false;
            }
        }

        if (handler.getKeyManager().space) {
            firePoop();
        }

        if (handler.getKeyManager().help) {
            help();
        }
    }

    private void move() {
        if (!collisionWithEntity(xMove, 0f))
            x += getMovementX();
        if (!collisionWithEntity(0f, yMove))
            y += getMovementY();
        else if (yMove > 0) {
            // stop falling when colliding vertically with an entity
            setFalling(false);
        }
    }

    private void firePoop() {

        // not in a position for pooping
        if (!canPoop)
            return;

        // out of poop
        if (poop == 0)
            return;

        // don't allow continuous pooping
        long now = System.currentTimeMillis();
        if (now - damageCheckTime > 200) {
            Poop p = new Poop(handler,
                              facingRight ? x + width / 6 : x + width - width / 2,
                              y + height / 3 * 2 + 5);
            poop--;
            handler.getEntityManager().addEntity(p);
            damageCheckTime = now;

            // lift the player up when pooping
            goingUp = true;
            yMove -= DEFAULT_SPEED;
            if (yMove < - MAX_SPEED)
                yMove = -MAX_SPEED;
        }

        if (!hasPooped) {
            poopRestoreTime = now;
            hasPooped = true;
        }
    }

    @Override
    public void tick() {

        // animation
        super.tick();
        chooseAnimation();

        // movement
        getInput();
        move();
        handler.getCamera().centreOnEntity(this);

        restorePoop();
    }

    /** Decides which animation should be shown. */
    private void chooseAnimation() {
        if (xMove != 0 || yMove < 0)
            animation = animFlying;
        else if (isFalling())
            animation = animFalling;
        else
            animation = animStill;
    }

    /** The player is always visible by default. */
    @Override
    public boolean offScreen() {
        return false;
    }

    /** Increase the player's poop load. */
    private void restorePoop() {

        // nothing to restore
        if (poop == MAX_POOP)
            return;

        // check if it's time to restore some poop
        long now = System.currentTimeMillis();
        if (now - poopRestoreTime > POOP_RESTORE_INTERVAL) {
            poop++;
            poopRestoreTime = now;
        }
    }

    private void help() {
    }

    // getters and setters

    /** Returns the amount of available poop. */
    public int getPoop() {
        return poop;
    }

    /** Increase the amount of available poop. */
    public void increasePoop() {
        poop++;
        if (poop > MAX_POOP)
            poop = MAX_POOP;
    }
}
