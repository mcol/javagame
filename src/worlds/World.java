package worlds;

import java.awt.Graphics;
import assets.entities.Enemy;
import assets.entities.EntityManager;
import assets.entities.Player;
import assets.entities.Tree;
import assets.tiles.Tile;
import game.Handler;
import utils.Utils;

public class World {

    private Handler handler;

    /** Array of tiles that define the world. */
    private int[][] tiles;

    /** Dimensions of the world in tiles. */
    private int width, height;

    /** Container for all entities. */
    protected EntityManager entityManager;

    /** Constructor. */
    public World(Handler handler, String path) {

        // load world data from file
        int[] spawnCoords = loadWorld(path);

        this.handler = handler;
        entityManager = new EntityManager(new Player(handler, spawnCoords[0], spawnCoords[1]));

        addEntities();
    }

    /** Adds entities to the  world. */
    protected void addEntities() {
        // trees
        entityManager.addEntity(new Tree(handler, 150, 300));
        entityManager.addEntity(new Tree(handler, 225, 350));
        entityManager.addEntity(new Tree(handler, 620, 350));

        // enemies
        entityManager.addEntity(new Enemy(handler, 350, 500));
        entityManager.addEntity(new Enemy(handler, 300, 50));
    }

    public void tick() {
        entityManager.tick();
    }

    public void render(Graphics g) {
        float camera_xOffset = handler.getCamera().getxOffset();
        float camera_yOffset = handler.getCamera().getyOffset();

        // range of coordinates of the visible tiles
        int xStart = (int) Math.max(camera_xOffset / Tile.TILEWIDTH, 0);
        int xEnd = (int) Math.min((camera_xOffset + handler.getWidth()) / Tile.TILEWIDTH + 1,
                                  width);
        int yStart = (int) Math.max(camera_yOffset / Tile.TILEHEIGHT, 0);
        int yEnd = (int) Math.min((camera_yOffset + handler.getHeight()) / Tile.TILEHEIGHT + 1,
                                  height);

        // draw only the visible tiles
        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++)
                getTile(x, y).render(g,
                                     (int) (x * Tile.TILEWIDTH - camera_xOffset),
                                     (int) (y * Tile.TILEHEIGHT - camera_yOffset));
        }

        // draw all entities
        entityManager.render(g);
    }

    /** Returns the tile at the given tile coordinates. */
    public Tile getTile(int x, int y) {

        // prevent errors going past the boundaries of the map
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return Tile.grassTile;
        }

        Tile t = Tile.tileset[tiles[x][y]];
        if (t == null)
            t = Tile.wallTile;
        return t;
    }

    private int[] loadWorld(String path) {
        String file = Utils.loadFileAsString(path);
        String[] tokens = file.split("\\s+");
        width = Utils.parseInt(tokens[0]);
        height = Utils.parseInt(tokens[1]);
        int[] spawnCoords = new int[2];
        spawnCoords[0] = Utils.parseInt(tokens[2]);
        spawnCoords[1] = Utils.parseInt(tokens[3]);

        tiles = new int[width][height];
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                tiles[x][y] = Utils.parseInt(tokens[x + y * width + 4]);
        return spawnCoords;
    }

    /** Returns the width of the world in tiles. */
    public int getWidth() {
        return width;
    }

    /** Returns the height of the world in tiles. */
    public int getHeight() {
        return height;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
