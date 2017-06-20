package assets.tiles;

import assets.Assets;

public class WallTile extends Tile {

    /** Constructor. */
    public WallTile() {
        super(Assets.wall);

    @Override
    public boolean isSolid() {
        return true;
    }
}
