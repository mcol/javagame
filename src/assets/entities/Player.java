package assets.entities;

import java.awt.event.KeyEvent;
import assets.Assets;
import game.Game;
import game.Handler;
import game.input.KeyManager;
import gfx.Animation;
import utils.Utils;

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

    /** Minimum speed allowed. */
    private static final float MIN_SPEED = 2.0f;

    /** Maximum speed allowed. */
    private static final float MAX_SPEED = 4.0f;

    /** Incremental speed change. */
    private static final float SPEED_CHANGE = 0.2f;

    /** Speed change factor. */
    private static final float SPEED_CHANGE_FACTOR = 1.03f;

    /** Keys pressed. */
    private boolean up, down, left, right;

    /** Animation states. */
    private final Animation animFlying, animFalling, animStill;

    /** Current score. */
    private int score;

    /** Current remaining time. */
    private int time;

    /** Current available poop. */
    private int poop;

    /** Whether the player is allowed to poop. */
    private boolean canPoop = false;

    /** Time of the last poop fire in ticks. */
    private long poopFireTime = 0;

    /** Time of the last poop increase in ticks. */
    private long poopRestoreTime;

    /** Constructor. */
    public Player(Handler handler, int x, int y) {
        super(handler, x, y, 75, 75);

        // bounding box
        setBounds(20, 28, width - 40, height - 42);

        // animations
        animFlying = new Animation(Assets.player_moving, 100);
        animStill = new Animation(Assets.player_still, 500);
        animFalling = new Animation(Assets.player_falling, 150);

        // current animation
        animation = animFalling;

        score = 0;
        time = 0;
        health = MAX_HEALTH;
        poop = MAX_POOP;
        poopRestoreTime = now;

        // start by falling
        yMove = MIN_SPEED;
    }

    /** Handle the input commands. */
    private void getInput() {

        // horizontal movement
        if (left) {
            if (xMove > 0)
                xMove = -xMove / 2;
            xMove -= SPEED_CHANGE;
        }
        if (right) {
            if (xMove < 0)
                xMove = -xMove / 2;
            xMove += SPEED_CHANGE;
        }
        xMove = Utils.clampAbsValue(xMove / SPEED_CHANGE_FACTOR, MAX_SPEED);

        // vertical movement
        if (up) {
            yMove -= SPEED_CHANGE;
        }
        if (down) {
            yMove += SPEED_CHANGE;
        }
        yMove = Utils.clampAbsValue(yMove / SPEED_CHANGE_FACTOR, MAX_SPEED);

        // facing direction
        if (!(left && right) && xMove != 0)
            facingRight = xMove > 0 ? true : false;

        // convergence to horizontal flight
        if (!up && !down) {
            if (yMove < 0)
                yMove += SPEED_CHANGE;
            yMove /= SPEED_CHANGE_FACTOR;
        }

        // friction
        if (!left && !right && Math.abs(xMove) < MIN_SPEED)
            xMove = 0;

        // gravity
        if (!left && !right && !up && !down) {
            yMove += SPEED_CHANGE;
            if (yMove > SLOW_SPEED)
                yMove = SLOW_SPEED;
        }

        // pooping
        canPoop = animation == animStill || yMove > SLOW_SPEED ? false : true;
    }

    private void move() {
        if (!collisionWithEntity(xMove, -3f))
            x += getMovementX();
        if (!collisionWithEntity(0f, yMove))
            y += getMovementY();
        else {
            // stop falling when colliding vertically with an entity
            animation = animStill;
            canPoop = false;

            // apply movement from entity below if any
            x += getEntityMovementX(0f, yMove);
            y += getEntityMovementY(xMove, 0f);
        }
        x = (int) x;
        y = (int) y;
        handler.getCamera().centreOnEntity(this);
    }

    private void firePoop() {

        // not in a position for pooping or out of poop
        if (!canPoop || poop == 0)
            return;

        // don't allow continuous pooping
        if (now - poopFireTime > POOP_FIRE_INTERVAL) {
            float xAdj = animation == animFalling ? 3 : facingRight ? 6 : 2;
            Poop p = new Poop(x + width / xAdj, y + height / 3 * 2 + 5);
            poop--;
            handler.getEntityManager().addEntity(p);
            poopFireTime = now;

            // lift the player up when pooping
            if (!left && !right) {
                yMove -= DEFAULT_SPEED;
                if (yMove < -MAX_SPEED)
                    yMove = -MAX_SPEED;
            }
        }
    }

    @Override
    public void tick() {

        // animation
        super.tick();
        chooseAnimation();

        // input handling
        getInput();
        move();

        if (handler.getWorld().hasDamageTiles())
            checkDamageTiles();
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
        else if (animation != animStill && yMove > 0)
            animation = animFalling;
        else
            animation = animStill;
    }

    /** Decreases the player's health if it touches a damage tile. */
    private void checkDamageTiles() {
        if (now - damageCheckTime < 8)
            return;

        float lb = getLeftBound(), rb = getRightBound();
        float tb = getTopBound(), bb = getBottomBound();
        int damage = handler.getWorld().getTile(lb, tb).getDamage()
                   + handler.getWorld().getTile(lb, bb).getDamage()
                   + handler.getWorld().getTile(rb, tb).getDamage()
                   + handler.getWorld().getTile(rb, bb).getDamage();

        setDamage((int) Math.ceil((double) damage / 2));
        damageCheckTime = now;
    }

    /** The player is always visible by default. */
    @Override
    public boolean isOffScreen() {
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
        increaseScore(time / Game.FPS);
        increaseHealth(MAX_HEALTH / 4);
        increasePoop(MAX_POOP / 4);
        handler.getWorld().increaseLevel();
        yMove = MIN_SPEED;
        animation = animFalling;
    }

    /** Updates the player's score. */
    public void increaseScore(int points) {
        score += points;
    }

    /** Increases the available time. */
    public void increaseTime(int amount) {
        time += amount * Game.FPS;
        if (time > MAX_TIME)
            time = MAX_TIME;
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
                                 (e) -> firePoop());
    }

    // getters and setters

    /** Returns the current level. */
    public int getLevel() {
        return handler.getWorld().getLevel();
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
