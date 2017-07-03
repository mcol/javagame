package assets.entities;

import assets.Assets;
import assets.tiles.Tile;
import game.Handler;
import gfx.Animation;

public class Poop extends Creature {

    /** Whether the poop needs to show the impact animation. */
    private boolean impact;

    /** Constructor. */
    public Poop(Handler handler, float x, float y) {
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
        if (dy < -Tile.TILEHEIGHT / 2) // don't let it reach the tile above
            dy = 1;
        if (dy == 0 || collisionWithEntity(0f, yMove) || collisionWithPlayer())
           setImpact();
        y += dy;
        checkPoopDamage();
    }

    private boolean collisionWithPlayer() {
        Player player = handler.getEntityManager().getPlayer();
        if (player.getCollisionRectangle(0f, 0f)
                  .intersects(getCollisionRectangle(0f, -yMove))) {
            player.increasePoop(1);
            return true;
        }
        return false;
    }

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
        int ty = (int) (getBottomBound() + yMove + 1) / Tile.TILEHEIGHT;
        if (handler.getWorld()
                   .getTile((int) getLeftBound() / Tile.TILEWIDTH, ty).isBreakable())
            setImpact();
        if (handler.getWorld()
                   .getTile((int) getRightBound() / Tile.TILEWIDTH, ty).isBreakable())
            setImpact();
    }

    public void setImpact() {
        impact = true;
        animation.setFrames(Assets.poop_impact, 100);

        // avoid further collisions
        setBounds(0, 0, 0, 0);
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
