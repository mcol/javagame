package assets.entities;

import java.awt.Color;
import assets.Assets;
import gfx.Animation;
import utils.Utils;

public class Ram extends Enemy {

    /** Score awarded when killed. */
    private static final int RAM_SCORE = 6;

    /** Health bar colour. */
    private static final Color BAR_COLOUR = new Color(0x555555);

    /** Animations. */
    private final Animation animMoving, animFurious;

    /** Constructor. */
    public Ram(int x, int y, int spawnHealth) {
        super(x, y, 90, 64, spawnHealth, RAM_SCORE, BAR_COLOUR);

        // bounding box
        setBounds(5, 8, width - 11, height - 10);

        // animations
        animMoving = new Animation(Assets.enemy_moving, 125);
        animFurious = new Animation(Assets.enemy_furious, 125);
        animDying = new Animation(Assets.enemy_dying, 100);
        animation = animMoving;
    }

    @Override
    public void tick() {
        super.tick();

        // animation
        if (health < LOW_HEALTH) {
            animation = animFurious;
            xMove *= 1.03;
            xMove = Utils.clampValue(xMove, (int) -FAST_SPEED, (int) FAST_SPEED);
        }
    }
}
