package assets.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import assets.Assets;

public class Tile {

    /** Size of a tile in pixels. */
    public static final int TILESIZE = 64;

    /** Tile types. */
    public enum Tiles {
        GRASS   (Assets.tiles[0], false),
        DIRT    (Assets.tiles[1], false),
        WALL    (Assets.tiles[2], true ),
        STONE   (Assets.tiles[3], false),
        WATER   (Assets.tiles[4], false),
        FIRE    (Assets.tiles[5], false),
        CARPET  (Assets.tiles[6], false),
        METAL   (Assets.tiles[7], true ),
        VOID    (null, false);

        /** Number of items in each row of the spritesheet. */
        public static final int rowlength = 4;

        /** Texture to be used for the tile. */
        private final BufferedImage texture;

        /** Whether the tile can collide with an entity. */
        private final boolean solid;

        /** Constructor. */
        Tiles(BufferedImage texture, boolean solid) {
            this.texture = texture;
            this.solid = solid;
        }

        /** Returns the texture used for the tile. */
        public BufferedImage getTexture() {
            return texture;
        }
    }

    /** Basic tiles. */
    private static final Tile grassTile = new Tile(Tiles.GRASS);
    private static final Tile dirtTile = new Tile(Tiles.DIRT);
    private static final Tile wallTile = new Tile(Tiles.WALL);
    private static final Tile waterTile = new Tile(Tiles.WATER);
    private static final Tile fireTile = new Tile(Tiles.FIRE);
    private static final Tile carpetTile = new Tile(Tiles.CARPET);
    private static final Tile metalTile = new Tile(Tiles.METAL);
    public static final Tile voidTile = new Tile(Tiles.VOID);

    /** Type of this tile. */
    protected final Tiles tile;

    /** Constructor. */
    protected Tile(Tiles tile) {
        this.tile = tile;
    }

    /** Returns a new tile corresponding to the id used in the world file. */
    public static Tile getTile(char id) {
        switch (id) {
        case '0':
            return grassTile;
        case '1':
            return dirtTile;
        case '2':
            return wallTile;
        case '3':
            return new BreakableTile(Tiles.STONE, Tiles.VOID);
        case '4':
            return waterTile;
        case '5':
            return fireTile;
        case '6':
            return carpetTile;
        case '7':
            return metalTile;
        default:
            return voidTile;
        }
    }

    public void render(Graphics g, int x, int y) {
        g.drawImage(tile.texture, x, y, TILESIZE, TILESIZE, null);
    }

    /** Returns whether the tile can collide with an entity. */
    public boolean isSolid() {
        return tile.solid;
    }

    /** Returns whether the tile can be destroyed. */
    public boolean isBreakable() {
        return false;
    }
}
