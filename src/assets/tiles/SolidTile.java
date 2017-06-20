package assets.tiles;

import java.awt.image.BufferedImage;

public abstract class SolidTile extends Tile {

    public SolidTile(BufferedImage texture) {
        super(texture);
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}
