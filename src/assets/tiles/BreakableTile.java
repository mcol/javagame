package assets.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BreakableTile extends Tile {

    protected boolean broken = false;
    protected final BufferedImage brokenTexture;

    public BreakableTile(Tiles tile,
                         BufferedImage brokenTexture) {
        super(tile);
        this.brokenTexture = brokenTexture;
    }

    @Override
    public void render(Graphics g, int x, int y) {
        g.drawImage(broken ? brokenTexture : tile.getTexture(),
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
