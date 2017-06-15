package assets.tiles;

import assets.Assets;

public class WallTile extends Tile {

    /** Constructor. */
    public WallTile(int id) {
        super(Assets.wall, id);
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}
