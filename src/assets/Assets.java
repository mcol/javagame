package assets;

import java.awt.image.BufferedImage;
import assets.entities.Items;
import assets.tiles.Tile.Tiles;
import gfx.SpriteSheet;

public class Assets {

    private static final int tilesize = 64;
    private static final int itemsize = 32;
    private static final int playerwidth = 185, playerheight = 175;
    private static final int poopwidth = 25, poopheight = 25;
    private static final int enemywidth = 90, enemyheight = 64;

    // tiles
    public static BufferedImage tiles[] = new BufferedImage[8];

    // entities
    public static BufferedImage tree;

    // items
    public static BufferedImage items[] = new BufferedImage[Items.values().length];

    // player
    public static BufferedImage[] player_flying;
    public static BufferedImage[] player_still;
    public static BufferedImage[] player_falling;

    // poop
    public static BufferedImage[] poop_falling;
    public static BufferedImage[] poop_impact;
    public static BufferedImage[] poop_explosion;

    // enemy
    public static BufferedImage[] enemy_moving;
    public static BufferedImage[] enemy_furious;
    public static BufferedImage[] enemy_dying;
    public static BufferedImage[] enemy_explosion;

    public static void init() {

        //
        // tiles
        //
        SpriteSheet tileSheet = new SpriteSheet("/textures/tiles.png");
        for (int i = 0; i < tiles.length; i++)
            tiles[i] = tileSheet.crop((i % Tiles.rowlength) * tilesize,
                                      (i / Tiles.rowlength) * tilesize,
                                      tilesize, tilesize);

        //
        // trees
        //
        SpriteSheet trees = new SpriteSheet("/textures/trees.png");
        tree = trees.crop(240, 0, 80, 120);

        //
        // items
        //
        SpriteSheet itemSheet = new SpriteSheet("/textures/items.png");
        for (int i = 0; i < items.length; i++)
            items[i] = itemSheet.crop((i % Items.rowlength) * itemsize,
                                      (i / Items.rowlength) * itemsize,
                                      itemsize, itemsize);

        //
        // player animations
        //
        SpriteSheet player = new SpriteSheet("/textures/player.png");

        player_flying = new BufferedImage[5];
        for (int i = 0; i < player_flying.length; i++)
            player_flying[i] = player.crop(playerwidth * i, 0, playerwidth, playerheight);

        player_still = new BufferedImage[6];
        for (int i = 0; i < 2; i++)
            player_still[i] = player.crop(playerwidth * i, playerheight, playerwidth, playerheight);
        for (int i = 2; i < player_still.length; i++)
            player_still[i] = player.crop(playerwidth * 0, playerheight, playerwidth, playerheight);

        player_falling = new BufferedImage[2];
        for (int i = 0; i < player_falling.length; i++)
            player_falling[i] = player.crop(playerwidth * (2 + i), playerheight, playerwidth, playerheight);

        //
        // poop animations
        //
        SpriteSheet poop = new SpriteSheet("/textures/player.png");

        poop_falling = new BufferedImage[12];
        for (int i = 0; i < 2; i++)
            poop_falling[i] = poop.crop(playerwidth * 4 + poopwidth * 0, playerheight,
                                        poopwidth, poopheight);
        for (int i = 2; i < 5; i++)
            poop_falling[i] = poop.crop(playerwidth * 4 + poopwidth * (i - 2), playerheight,
                                        poopwidth, poopheight);
        for (int i = 5; i < poop_falling.length; i++)
            poop_falling[i] = poop.crop(playerwidth * 4 + poopwidth * 0, playerheight,
                                        poopwidth, poopheight);

        poop_impact = new BufferedImage[4];
        for (int i = 0; i < poop_impact.length; i++)
            poop_impact[i] = poop.crop(playerwidth * 4 + poopwidth * (3 + i), playerheight,
                                       poopwidth, poopheight);

        poop_explosion = new BufferedImage[4];
        for (int i = 0; i < poop_explosion.length; i++)
            poop_explosion[i] = poop.crop(playerwidth * 4 + poopwidth * (3 + i), playerheight + poopheight,
                                          poopwidth, poopheight);

        //
        // enemy animations
        //
        SpriteSheet enemy = new SpriteSheet("/textures/ram.png");

        enemy_moving = new BufferedImage[3];
        for (int i = 0; i < enemy_moving.length; i++)
            enemy_moving[i] = enemy.crop(105 * i, 0, enemywidth, enemyheight);

        enemy_furious = new BufferedImage[3];
        for (int i = 0; i < enemy_furious.length; i++)
            enemy_furious[i] = enemy.crop(105 * i, enemyheight, enemywidth, enemyheight);

        enemy_dying = new BufferedImage[8];
        for (int i = 0; i < 4; i++)
            enemy_dying[i] = enemy.crop(105 * i, enemyheight * 2, enemywidth, enemyheight);
        for (int i = 4; i < enemy_dying.length; i++)
            enemy_dying[i] = enemy.crop(105 * (i - 4), enemyheight * 3, enemywidth, enemyheight);
    }
}
