package assets.entities;

import java.awt.Graphics;
import java.util.ArrayList;
import assets.Assets;
import game.Handler;
import gfx.Animation;

public class Player extends Creature {

    private static final float dampingFactor = 0.96f;
    private static final float liftspeed = 1.4f;
    private boolean goingUp = false;
    private boolean canPoop = false;

    // Animations
    private Animation animFlying, animFalling, animStill;

    // Pooping
    ArrayList<Poop> poops = new ArrayList<Poop>();
    long lastPoopTime = 0;
    long lastPoopCheck = 0;

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
    }

    private void getInput() {

        //
        // horizontal movement
        //
        if (handler.getKeyManager().left) {
            canPoop = true;
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
            canPoop = true;
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
            canPoop = true;
            xMove *= dampingFactor;
            if (Math.abs(xMove) < MIN_SPEED) {
                xMove = 0;
                canPoop = false;
            }

            // gradual descent unless trying to go up
            if (!handler.getKeyManager().up)
                yMove += SPEED_CHANGE;
        }

        //
        // vertical movement
        //
        if (handler.getKeyManager().up) {
            canPoop = true;
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
            canPoop = true;
            yMove *= dampingFactor;
            if (yMove > -MIN_SPEED * 2) {
                yMove = SPEED_CHANGE; // start falling
                canPoop = false;
            }
        }
        else { // falling or still
            canPoop = true; // let it poop while still slowing down
            goingUp = false;
            if (isFalling()) {
                yMove += SPEED_CHANGE;
                if (yMove > FALL_SPEED) {
                    yMove = FALL_SPEED;
                    canPoop = false;
                }
            }
            if (xMove == 0) // completely still
                canPoop = false;
        }

        if (handler.getKeyManager().space) {
            firePoop();
        }
    }

    private void firePoop() {
        if (!canPoop)
            return;

        // don't allow continuous pooping
        long poopTime = System.currentTimeMillis();
        if (poopTime - lastPoopTime > 200) {
            poops.add(new Poop(handler,
                               facingRight ? x + width / 6 : x + width - width / 2,
                                y + height / 3 * 2 + 5));
            lastPoopTime = poopTime;
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

        // poop
        for (Poop p : poops)
            p.tick();

        for (int i = poops.size() - 1; i >= 0; i--) {
            if (poops.get(i).shouldRemove())
                poops.remove(i);
        }
    }

    @Override
    public void render(Graphics g) {

        // render the player
        super.render(g);

        // render the poop
        for (Poop p : poops)
            p.render(g);
    }

    private void chooseAnimation() {
        if (xMove != 0 || yMove < 0)
            animation = animFlying;
        else if (isFalling())
            animation = animFalling;
        else
            animation = animStill;
    }
}
