package assets.tiles;

import java.awt.Graphics;

public class BreakableTile extends Tile {

    /** Whether the tile has been destroyed. */
    private boolean broken = false;

    /** Texture to be used for the tile when destroyed. */
    private final Tiles brokenTile;

    /** Constructor. */
    public BreakableTile(Tiles tile, Tiles brokenTile) {
        super(tile);
        this.brokenTile = brokenTile;
    }

    @Override
    public void render(Graphics g, int x, int y) {
        g.drawImage(broken ? brokenTile.getTexture() : tile.getTexture(),
                    x, y, TILESIZE, TILESIZE, null);
    }

    /** Returns whether the tile can collide with an entity. */
    @Override
    public boolean isSolid() {
        return !broken;
    }

    /** Returns whether the tile can be destroyed. */
    @Override
    public boolean isBreakable() {
        if (broken)
            return false;
        broken = true;
        return true;
    }
}
