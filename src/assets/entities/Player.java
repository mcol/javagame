package assets.entities;

import java.awt.event.KeyEvent;
import assets.Assets;
import game.Handler;
import game.input.KeyManager;
import gfx.Animation;

public class Player extends Creature {

    /** Maximum amount of health of the player. */
    public static final int MAX_HEALTH = 40;

    /** Maximum amount of poop that the player can carry. */
    public static final int MAX_POOP = 25;

    /** Interval between each poop increase in milliseconds. */
    public static final long POOP_RESTORE_INTERVAL = 15000;

    private static final float dampingFactor = 0.96f;
    private static final float liftspeed = 1.4f;

    /** Keys pressed. */
    private boolean up, down, left, right, space, help;

    private boolean goingUp = false;
    private boolean canPoop = false;

    // Animations
    private final Animation animFlying, animFalling, animStill;

    /** Current score. */
    private int score;

    /** Current available poop. */
    private int poop;

    /** Whether the player has fired any poop. */
    private boolean hasPooped = false;

    /** Time of the last poop fire. */
    private long poopFireTime = 0;

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

        score = 0;
        health = MAX_HEALTH;
        poop = MAX_POOP;
    }

    private void getInput() {

        canPoop = true;

        //
        // horizontal movement
        //
        if (left) {
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
        else if (right) {
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
            if (!up)
                yMove += SPEED_CHANGE;
        }

        //
        // vertical movement
        //
        if (up) {
            if (!goingUp) { // switched direction
                goingUp = true;
                yMove = -yMove / 2;
            }
            yMove -= SPEED_CHANGE;
            if (yMove < -MAX_SPEED)
                yMove = -MAX_SPEED;
        }
        else if (down) {
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

        if (space) {
            firePoop();
        }

        if (help) {
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

        // not in a position for pooping or out of poop
        if (!canPoop || poop == 0)
            return;

        // don't allow continuous pooping
        if (now - poopFireTime > 200) {
            Poop p = new Poop(facingRight ? x + width / 6
                                          : x + width - width / 2,
                              y + height / 3 * 2 + 5);
            poop--;
            handler.getEntityManager().addEntity(p);
            poopFireTime = now;

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

        // check if the player is on a damage tile
        if (now - damageCheckTime > 150) {
            setDamage(handler.getWorld().getTile(x, y).getDamage());
            damageCheckTime = now;
        }

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
        if (now - poopRestoreTime > POOP_RESTORE_INTERVAL) {
            poop++;
            poopRestoreTime = now;
        }
    }

    /** Update the player's score. */
    public void increaseScore(int points) {
        score += points;
    }

    /** Increase the health of the player. */
    public void increaseHealth(int amount) {
        health += amount;
        if (health > MAX_HEALTH)
            health = MAX_HEALTH;
    }

    /** Increase the amount of available poop. */
    public void increasePoop(int amount) {
        poop += amount;
        if (poop > MAX_POOP)
            poop = MAX_POOP;
    }

    private void help() {
    }

    /** Add the key bindings to control the player. */
    public void addKeyBindings() {

        KeyManager.addKeyBinding(handler.getFrame(), KeyEvent.VK_UP,
                                 (e) -> up = true, (e) -> up = false);

        KeyManager.addKeyBinding(handler.getFrame(), KeyEvent.VK_DOWN,
                                 (e) -> down = true, (e) -> down = false);

        KeyManager.addKeyBinding(handler.getFrame(), KeyEvent.VK_LEFT,
                                 (e) -> left = true, (e) -> left = false);

        KeyManager.addKeyBinding(handler.getFrame(), KeyEvent.VK_RIGHT,
                                 (e) -> right = true, (e) -> right = false);

        KeyManager.addKeyBinding(handler.getFrame(), KeyEvent.VK_SPACE,
                                 (e) -> space = true, (e) -> space = false);

        KeyManager.addKeyBinding(handler.getFrame(), KeyEvent.VK_H,
                                 (e) -> help = true, (e) -> help = false);
    }

    // getters and setters

    /** Returns the current score. */
    public int getScore() {
        return score;
    }

    /** Returns the amount of available poop. */
    public int getPoop() {
        return poop;
    }
}
