package assets.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Tile {

    /** Size of a tile in pixels. */
    public static final int TILEWIDTH = 64, TILEHEIGHT = 64;

    /** Array of all instantiated tiles. */
    public static Tile[] tileset = new Tile[64];

    public static Tile grassTile = new GrassTile(0);
    public static Tile dirtTile = new DirtTile(1);
    public static Tile wallTile = new WallTile(2);
    public static Tile stoneTile = new StoneTile(3);

    protected BufferedImage texture;
    protected final int id;

    /** Constructor. */
    public Tile(BufferedImage texture, int id) {
        this.texture = texture;
        this.id = id;

        tileset[id] = this;
    }

    public void tick() { /* nothing to do */ }

    public void render(Graphics g, int x, int y) {
        g.drawImage(texture, x, y, TILEWIDTH, TILEHEIGHT, null);
    }

    /** Returns whether the tile can collide with an entity. */
    public boolean isSolid() {
        return false;
    }

    public int getId() {
        return id;
    }
}
