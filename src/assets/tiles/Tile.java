package assets.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Tile {

    /** Size of a tile in pixels. */
    public static final int TILEWIDTH = 64, TILEHEIGHT = 64;

    /** Tile with no texture. */
    public static Tile voidTile = new Tile(null);

    /** The texture to be used for the tile. */
    protected BufferedImage texture;

    /** Constructor. */
    protected Tile(BufferedImage texture) {
        this.texture = texture;
    }

    /** Create a new tile corresponding to the id used in the world file. */
    public static Tile getTile(int id) {
        switch (id) {
        case 0:
            return new GrassTile();
        case 1:
            return new DirtTile();
        case 2:
            return new WallTile();
        case 3:
            return new StoneTile();
        default:
            return voidTile;
        }
    }

    public void tick() { /* nothing to do */ }

    public void render(Graphics g, int x, int y) {
        g.drawImage(texture, x, y, TILEWIDTH, TILEHEIGHT, null);
    }

    /** Returns whether the tile can collide with an entity. */
    public boolean isSolid() {
        return false;
    }

    /** Returns whether the tile can be destroyed. */
    public boolean isBreakable() {
        return false;
    }
}
