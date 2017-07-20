package assets.tiles;

import java.awt.Graphics;

public class BreakableTile extends Tile {

    protected boolean broken = false;
    protected final Tiles brokenTile;

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

    @Override
    public boolean isSolid() {
        return !broken;
    }

    @Override
    public boolean isBreakable() {
        if (broken)
            return false;
        broken = true;
        return true;
    }
}
