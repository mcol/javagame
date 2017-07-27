package game;

import java.awt.Graphics;
import java.io.FileInputStream;
import java.util.Properties;
import assets.entities.Boo;
import assets.entities.CollectableItem;
import assets.entities.EntityManager;
import assets.entities.Items;
import assets.entities.Player;
import assets.entities.Ram;
import assets.entities.Tree;
import assets.tiles.Tile;
import gfx.Background;
import utils.Utils;

public class World {

    /** Number of available worlds. */
    public static final int MAX_WORLDS = 7;

    private final Handler handler;

    /** The player. */
    private final Player player;

    /** The background image. */
    private static Background bg;

    /** Array of tiles that define the world. */
    private Tile[][] tiles;

    /** Dimensions of the world in tiles. */
    private int width, height;

    /** Container for all entities. */
    private static EntityManager entityManager;

    /** Container for all messages. */
    private static MessageManager messageManager;

    /** Time of the next item spawn in ticks. */
    private long nextItemSpawnTime;

    /** Constructor. */
    public World(Handler handler, Player player) {
        this.handler = handler;
        this.player = player;
        handler.setWorld(this);
        loadWorld(1);
    }

    /** Loads a new world. */
    public void loadWorld(int level) {

        // initialize the lists of alive entities and messages
        entityManager = new EntityManager(player);
        messageManager = new MessageManager();

        // load the world data from file
        int world = (level - 1) % MAX_WORLDS + 1;
        readWorldFile("res/worlds/world" + world + ".txt");
        player.setPosition(0, 0);
        nextItemSpawnTime = Game.getTicksTime() +
                            Utils.randomInteger(10, 15) * Game.FPS;
    }

    public void tick() {
        bg.tick();
        entityManager.tick();
        messageManager.tick();
        if (!this.isSafe())
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

        // background
        bg.render(g);

        // draw only the visible tiles
        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++)
                getTile(x, y).render(g,
                                     x * Tile.TILESIZE - (int) camera_xOffset,
                                     y * Tile.TILESIZE - (int) camera_yOffset);
        }

        // draw all entities and messages
        entityManager.render(g);
        messageManager.render(g);
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
    private void readWorldFile(String path) {
        Properties props = new Properties();
        try {
            FileInputStream in = new FileInputStream(path);
            props.load(in);
            in.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        // number of tiles in each dimension
        width = Utils.parseInt(props.getProperty("width"));
        height = Utils.parseInt(props.getProperty("height"));

        // time allowed
        player.setTime(Utils.parseInt(props.getProperty("time")));

        // background
        bg = new Background(props.getProperty("background"), -0.1f,
                            Game.WIDTH, Game.HEIGHT);

        // tiles
        String[] tokens = props.getProperty("tiles").split("\\s+");
        tiles = new Tile[width][height];
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                tiles[x][y] = Tile.getTile(tokens[x + y * width].charAt(0));

        // enemies and entities
        addEnemies(props);
        addEntities(props);
    }

    /** Adds enemies to the world. */
    private void addEnemies(Properties props) {
        String[] tokens = props.getProperty("enemies").split("\\s+");
        int nFields = 4, nEnemies = tokens.length / nFields;
        for (int i = 0; i < nEnemies; i++) {
            String type = tokens[i * nFields];
            int x = Utils.parseInt(tokens[i * nFields + 1]);
            int y = Utils.parseInt(tokens[i * nFields + 2]);
            int health = Utils.parseInt(tokens[i * nFields + 3]);
            switch (type) {
                case "boo":
                    entityManager.addEntity(new Boo(x, y, health));
                    break;
                case "ram":
                    entityManager.addEntity(new Ram(x, y, health));
                    break;
            }
        }
    }

    /** Adds entities to the world. */
    private void addEntities(Properties props) {
        String[] tokens = props.getProperty("entities").split("\\s+");
        int nFields = 3, nEntities = tokens.length / nFields;
        for (int i = 0; i < nEntities; i++) {
            String type = tokens[i * nFields];
            int x = Utils.parseInt(tokens[i * nFields + 1]);
            int y = Utils.parseInt(tokens[i * nFields + 2]);
            switch (type) {
                case "tree":
                    entityManager.addEntity(new Tree(x, y));
                    break;
            }
        }
    }

    /** Places a new item in the world. */
    private void spawnItem() {

        // check if it's time to spawn a new item
        long now = Game.getTicksTime();
        if (now < nextItemSpawnTime)
            return;

        int x, y;

        // avoid solid tiles
        do {
            x = Utils.randomInteger(0, width - 1);
            y = Utils.randomInteger(0, height - 1);
        } while (getTile(x, y).isSolid());

        // add the item to the list of alive entities
        entityManager.addEntity(new CollectableItem(Items.randomItem(),
                                                    x * Tile.TILESIZE,
                                                    y * Tile.TILESIZE));
        nextItemSpawnTime = now + Utils.randomInteger(10, 20) * Game.FPS;
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
    public static EntityManager getEntityManager() {
        return entityManager;
    }

    /** Returns the message manager. */
    public static MessageManager getMessageManager() {
        return messageManager;
    }

    /** Returns whether all enemies have been killed. */
    public boolean isSafe() {
        return entityManager.getEnemyCount() == 0 ;
    }

    /** Returns whether the current world has been cleared. */
    public boolean isCleared() {
        return entityManager.getEnemyCount() == 0 &&
               entityManager.getItemCount() == 0 &&
               messageManager.getMessageCount() == 0;
    }
}
