package assets.entities;

import java.awt.event.KeyEvent;
import assets.Assets;
import game.Game;
import game.Handler;
import game.input.KeyManager;
import gfx.Animation;

public class Player extends Creature {

    /** Maximum amount of time available for a level. */
    public static final int MAX_TIME = 60 * Game.FPS;

    /** Maximum amount of health of the player. */
    public static final int MAX_HEALTH = 40;

    /** Maximum amount of poop that the player can carry. */
    public static final int MAX_POOP = 25;

    /** Interval between each poop fire in ticks. */
    private static final long POOP_FIRE_INTERVAL = 15;

    /** Interval between each poop increase in ticks. */
    private static final long POOP_RESTORE_INTERVAL = 15 * Game.FPS;

    /** Maximum speed allowed. */
    private static final float MAX_SPEED = 4.0f;

    private static final float dampingFactor = 0.96f;
    private static final float liftspeed = 1.4f;

    /** Keys pressed. */
    private boolean up, down, left, right, space;

    private boolean goingUp = false;
    private boolean canPoop = false;

    // Animations
    private final Animation animFlying, animFalling, animStill;

    /** Current level. */
    private int level;

    /** Current score. */
    private int score;

    /** Current remaining time. */
    private int time;

    /** Current available poop. */
    private int poop;

    /** Whether the player has fired any poop. */
    private boolean hasPooped = false;

    /** Time of the last poop fire in ticks. */
    private long poopFireTime = 0;

    /** Time of the last poop increase in ticks. */
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

        level = 1;
        score = 0;
        time = 0;
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
                if (yMove > SLOW_SPEED) {
                    yMove = SLOW_SPEED;
                }
            }
            else if (xMove == 0 && yMove >= 0) {
                canPoop = false;
            }
        }

        if (space) {
            firePoop();
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
        if (now - poopFireTime > POOP_FIRE_INTERVAL) {
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
        if (now - damageCheckTime > 8) {
            int damage = handler.getWorld().getTile(getLeftBound(),
                                                    getTopBound())
                                           .getDamage() +
                         handler.getWorld().getTile(getLeftBound(),
                                                    getBottomBound())
                                           .getDamage() +
                         handler.getWorld().getTile(getRightBound(),
                                                    getTopBound())
                                           .getDamage() +
                         handler.getWorld().getTile(getRightBound(),
                                                    getBottomBound())
                                           .getDamage();
            setDamage((int) Math.ceil((double) damage / 2));
            damageCheckTime = now;
        }

        restorePoop();

        // decrease the remaining time
        time -= 1;

        // decrease the health when out of time if enemies are still present
        if (time < 0 && now % 25 == 0 && !handler.getWorld().isSafe())
            health -= 1;
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

    /** Increases the player's poop load. */
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

    /** Increases the current level. */
    public void increaseLevel() {
        level += 1;
        increaseScore(25 + time / Game.FPS);
        increaseHealth(MAX_HEALTH / 4);
        increasePoop(MAX_POOP / 4);
        handler.getWorld().loadWorld(level);
    }

    /** Updates the player's score. */
    public void increaseScore(int points) {
        score += points;
    }

    /** Increases the health of the player. */
    public void increaseHealth(int amount) {
        health += amount;
        if (health > MAX_HEALTH)
            health = MAX_HEALTH;
    }

    /** Increases the amount of available poop. */
    public void increasePoop(int amount) {
        poop += amount;
        if (poop > MAX_POOP)
            poop = MAX_POOP;
    }

    /** Adds the key bindings to control the player. */
    public void addKeyBindings() {

        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_UP,
                                 (e) -> up = true, (e) -> up = false);

        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_DOWN,
                                 (e) -> down = true, (e) -> down = false);

        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_LEFT,
                                 (e) -> left = true, (e) -> left = false);

        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_RIGHT,
                                 (e) -> right = true, (e) -> right = false);

        KeyManager.addKeyBinding(handler.getDisplay(), KeyEvent.VK_SPACE,
                                 (e) -> space = true, (e) -> space = false);
    }

    // getters and setters

    /** Returns the current level. */
    public int getLevel() {
        return level;
    }

    /** Returns the current score. */
    public int getScore() {
        return score;
    }

    /** Returns the amount of remaining time. */
    public int getTime() {
        return time;
    }

    /** Sets the amount of available time. */
    public void setTime(int time) {
        this.time = time * Game.FPS;
    }

    /** Returns the amount of available poop. */
    public int getPoop() {
        return poop;
    }
}
