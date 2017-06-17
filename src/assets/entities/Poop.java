package assets.entities;

import java.awt.Graphics;
import assets.Assets;
import game.Handler;
import gfx.Animation;

public class Poop extends Creature {

    /** Whether the poop needs to show the impact animation. */
    private boolean impact;

    private static final int poopDamage = 4;

    /** Constructor. */
    public Poop(Handler handler, int x, int y) {
        super(handler, x, y, 25, 25);

        // animation
        animation = new Animation(Assets.poop_falling, 100);

        // bounding box
        setBounds(5, 5, 15, 18);

        impact = false;
        yMove = FALL_SPEED;
    }

    @Override
    public void tick() {
        super.tick();

        if (impact)
            return;

        int dy = getMovementY();
        if (collidesWithEntity(0f, yMove) || dy == 0)
           setImpact();
        y += dy;
        checkPoopDamage();
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
    }

    private void checkPoopDamage() {

        for (Entity e : handler.getWorld().getEntityManager().getEntities()) {
            if (!(e instanceof Enemy))
                continue;

            Enemy enemy = (Enemy) e;

            // avoid explosions on a dead enemy
            if (enemy.isDead())
                continue;

            float enemyMove = enemy.getxMove();
            float yOffset = 10f;
            if (getCollisionRectangle(0f, yOffset)
                    .intersects(enemy.getCollisionRectangle(enemyMove, 0f))) {
                enemy.setDamage(poopDamage);
                setExplosion();

                // set the coordinates of the explosion to match those used in computing
                // the collision, and ensure that a front explosion doesn't get hidden by
                // the enemy moving horizontally
                if (!enemy.isDead()) {
                    x += enemyMove * 8;
                    y += yOffset;
                }
            }
        }
    }

    public void setImpact() {
        impact = true;
        animation.setFrames(Assets.poop_impact, 100);
    }

    public void setExplosion() {
        impact = true;
        animation.setFrames(Assets.poop_explosion, 100);
    }

    /** Whether the poop should be removed. */
    @Override
    public boolean shouldRemove() {
        return impact && animation.hasPlayedOnce();
    }
}