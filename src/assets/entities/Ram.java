package assets.entities;

import assets.Assets;
import gfx.Animation;

public class Ram extends Enemy {

    /** Score awarded when killed. */
    private static final int RAM_SCORE = 6;

    /** Health bar colour. */
    private static final int BAR_COLOUR = 0x555555;

    /** Constructor. */
    public Ram(int x, int y, int spawnHealth) {
        super(x, y, 90, 64, spawnHealth, RAM_SCORE, BAR_COLOUR);

        // bounding box
        setBounds(5, 8, width - 11, height - 10);

        // enemy parameters
        this.frenzyThreshold = spawnHealth / 3;

        // animations
        animation = new Animation(Assets.ram_moving, 125);
        animFrenzy = new Animation(Assets.ram_frenzy, 125);
        animDying = new Animation(Assets.ram_dying, 100);
    }
}
