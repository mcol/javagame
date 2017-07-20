package worlds;

import java.awt.Graphics;
import assets.entities.CollectableItem;
import assets.entities.Enemy;
import assets.entities.EntityManager;
import assets.entities.Items;
import assets.entities.Player;
import assets.entities.Tree;
import assets.tiles.Tile;
import game.Game;
import game.Handler;
import utils.Utils;

public class World {

    private final Handler handler;

    /** Array of tiles that define the world. */
    private Tile[][] tiles;

    /** Dimensions of the world in tiles. */
    private int width, height;

    /** Container for all entities. */
    private final EntityManager entityManager;

    /** Time of the next item spawn in ticks. */
    private long nextItemSpawnTime;

    /** Constructor. */
    public World(Handler handler, String path) {

        this.handler = handler;

        // load world data from file
        int[] spawnCoords = loadWorld(path);

        // add the entities
        entityManager = new EntityManager(new Player(handler, spawnCoords[0], spawnCoords[1]));
        addEntities();
        nextItemSpawnTime = handler.getGame().getTicksTime() +
                            Handler.randomInteger(10, 15) * Game.FPS;
    }

    /** Adds entities to the  world. */
    protected void addEntities() {
        // trees
        entityManager.addEntity(new Tree(150, 300));
        entityManager.addEntity(new Tree(225, 350));
        entityManager.addEntity(new Tree(525, 350));

        // enemies
        entityManager.addEntity(new Enemy(handler, 350, 500));
        entityManager.addEntity(new Enemy(handler, 300, 50));
    }

    public void tick() {
        entityManager.tick();
        spawnItem();
    }

    public void render(Graphics g) {
        float camera_xOffset = handler.getCamera().getxOffset();
        float camera_yOffset = handler.getCamera().getyOffset();

        // range of coordinates of the visible tiles
        int xStart = (int) Math.max(camera_xOffset / Tile.TILESIZE, 0);
        int xEnd = (int) Math.min((camera_xOffset + Game.WIDTH) / Tile.TILESIZE + 1,
                                  width);
        int yStart = (int) Math.max(camera_yOffset / Tile.TILESIZE, 0);
        int yEnd = (int) Math.min((camera_yOffset + Game.HEIGHT) / Tile.TILESIZE + 1,
                                  height);

        // draw only the visible tiles
        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++)
                getTile(x, y).render(g,
                                     (int) (x * Tile.TILESIZE - camera_xOffset),
                                     (int) (y * Tile.TILESIZE - camera_yOffset));
        }

        // draw all entities
        entityManager.render(g);
    }

    /** Returns the tile at the given tile coordinates. */
    public Tile getTile(int x, int y) {

        // prevent errors going past the boundaries of the map
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return Tile.voidTile;
        }

        Tile t = tiles[x][y];
        if (t == null)
            t = Tile.voidTile;
        return t;
    }

    /** Returns the tile at the given world coordinates. */
    public Tile getTile(float x, float y) {
        return getTile((int) x / Tile.TILESIZE, (int) y / Tile.TILESIZE);
    }

    /** Loads a world file. */
    private int[] loadWorld(String path) {
        String file = Utils.loadFileAsString(path);
        String[] tokens = file.split("\\s+");
        width = Utils.parseInt(tokens[0]);
        height = Utils.parseInt(tokens[1]);
        int[] spawnCoords = new int[2];
        spawnCoords[0] = Utils.parseInt(tokens[2]);
        spawnCoords[1] = Utils.parseInt(tokens[3]);

        tiles = new Tile[width][height];
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                tiles[x][y] = Tile.getTile(Utils.parseInt(tokens[x + y * width + 4]));
        return spawnCoords;
    }

    /** Places a new item in the world. */
    private void spawnItem() {

        // check if it's time to spawn a new item
        long now = handler.getGame().getTicksTime();
        if (now < nextItemSpawnTime)
            return;

        int x, y;

        // avoid solid tiles
        do {
            x = Handler.randomInteger(0, width);
            y = Handler.randomInteger(0, height);
        } while (getTile(x, y).isSolid());

        // add the item to the list of alive entities
        entityManager.addEntity(new CollectableItem(Items.randomItem(),
                                                    x  * Tile.TILESIZE,
                                                    y  * Tile.TILESIZE));
        nextItemSpawnTime = now + Handler.randomInteger(10, 20) * Game.FPS;
    }

    // getters and setters

    /** Returns the width of the world in tiles. */
    public int getWidth() {
        return width;
    }

    /** Returns the height of the world in tiles. */
    public int getHeight() {
        return height;
    }

    /** Returns the entity manager. */
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
