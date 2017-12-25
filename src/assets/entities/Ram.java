package assets.entities;

import assets.Assets;
import gfx.Animation;

public class Ram extends Enemy {

    /** Damage procured by the enemy. */
    private static final int RAM_DAMAGE = 5;

    /** Score awarded when killed. */
    private static final int RAM_SCORE = 6;

    /** Health bar colour. */
    private static final int BAR_COLOUR = 0x555555;

    /** Constructor. */
    public Ram(int x, int y, int health) {
        super(x, y, 90, 64, health, RAM_DAMAGE, RAM_SCORE, BAR_COLOUR);

        // enemy parameters
        this.imageShift = 25;
        this.frenzyThreshold = health / 3;

        // bounding box
        setBounds(30, 8, width - 35, height - 10);

        // animations
        animation = new Animation(Assets.ram_moving, 125);
        animFrenzy = new Animation(Assets.ram_frenzy, 125);
        animDying = new Animation(Assets.ram_dying, 100);
    }
}
