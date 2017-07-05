package assets.entities;

import assets.Assets;
import assets.tiles.Tile;
import gfx.Animation;

public class Poop extends Creature {

    /** Whether the poop needs to show the impact animation. */
    private boolean impact;

    /** Constructor. */
    public Poop(float x, float y) {
        super(handler, x, y, 25, 25);

        // animation
        animation = new Animation(Assets.poop_falling, 100);

        // bounding box
        setBounds(5, 10, 15, 14);

        // poop damage
        damage = 3;

        impact = false;
        yMove = FALL_SPEED;
    }

    @Override
    public void tick() {
        super.tick();

        if (impact)
            return;

        float dy = getMovementY();
        if (dy < -Tile.TILESIZE / 2) // don't let it reach the tile above
            dy = 1;
        if (dy == 0 || collisionWithEntity(0f, yMove) || collisionWithPlayer())
           setImpact();
        y += dy;
        checkPoopDamage();
    }

    /** Returns whether the player collides with the poop. */
    private boolean collisionWithPlayer() {
        Player player = handler.getPlayer();
        if (player.getCollisionRectangle(0f, 0f)
                  .intersects(getCollisionRectangle(0f, -yMove))) {
            player.increasePoop(1);
            return true;
        }
        return false;
    }

    /** Checks if the poop damages an enemy. */
    private void checkPoopDamage() {

        // enemies
        for (Entity e : handler.getEntities()) {

            // can only damage enemies
            if (!(e instanceof Enemy))
                continue;

            Enemy enemy = (Enemy) e;

            // avoid explosions on a dead enemy
            if (enemy.isDead())
                continue;

            float enemyMove = enemy.getxMove();
            if (getCollisionRectangle(0f, yMove)
                    .intersects(enemy.getCollisionRectangle(enemyMove, 0f))) {
                enemy.setDamage(damage);
                setExplosion();
                handler.getPlayer().increaseScore(1);
            }
        }

        // breakable tiles
        int ty = (int) (getBottomBound() + yMove + 1) / Tile.TILESIZE;
        if (handler.getWorld()
                   .getTile((int) getLeftBound() / Tile.TILESIZE, ty)
                   .isBreakable())
            setImpact();
        if (handler.getWorld()
                   .getTile((int) getRightBound() / Tile.TILESIZE, ty)
                   .isBreakable())
            setImpact();
    }

    /** Sets the impact animation. */
    public void setImpact() {
        impact = true;
        animation.setFrames(Assets.poop_impact, 100);

        // avoid further collisions
        setBounds(0, 0, 0, 0);
    }

    /** Sets the explosion animation. */
    public void setExplosion() {
        impact = true;
        animation.setFrames(Assets.poop_explosion, 100);
    }

    /** Returns whether the poop should be removed. */
    @Override
    public boolean shouldRemove() {
        return impact && animation.hasPlayedOnce();
    }
}
