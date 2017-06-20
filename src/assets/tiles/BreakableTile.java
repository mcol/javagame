package assets.tiles;

import java.awt.image.BufferedImage;

public abstract class BreakableTile extends Tile {

    protected boolean broken = false;
    protected BufferedImage brokenTexture;

    public BreakableTile(BufferedImage initialTexture,
                         BufferedImage brokenTexture) {
        super(initialTexture);
        this.brokenTexture = brokenTexture;
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
        this.texture = brokenTexture;
        return true;
    }
}
