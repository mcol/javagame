package assets;

import java.awt.image.BufferedImage;
import assets.entities.Items;
import assets.tiles.Tile.Tiles;
import gfx.SpriteSheet;

public class Assets {

    private static final int tilesize = 64;
    private static final int itemsize = 32;
    private static final int playerwidth = 185, playerheight = 175;

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
    public static BufferedImage[] poop_moving, poop_impact, poop_explosion;

    // enemies
    public static BufferedImage[] boo_moving, boo_dying;
    public static BufferedImage[] launcher_still, launcher_firing, launcher_dying;
    public static BufferedImage[] missile_moving, missile_explosion;
    public static BufferedImage[] ram_moving, ram_frenzy, ram_dying;
    public static BufferedImage[] tank_moving, tank_frenzy, tank_dying;

    public static void init() {

        int w, h;

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
        // boo animations
        //
        SpriteSheet boo = new SpriteSheet("/textures/boo.png");
        int boowidth = 32, booheight = 32;
        boo_moving = new BufferedImage[6];
        for (int i = 0; i < boo_moving.length; i++)
            boo_moving[i] = boo.crop(boowidth * i, 0, boowidth, booheight);

        boo_dying = new BufferedImage[8];
        for (int i = 0; i < boo_dying.length; i++)
            boo_dying[i] = boo.crop(boowidth * i, booheight, boowidth, booheight);

        //
        // launcher animations
        //
        SpriteSheet launcher = new SpriteSheet("/textures/launcher.png");
        w = 86;
        h = 62;
        launcher_still = new BufferedImage[2];
        for (int i = 0; i < launcher_still.length; i++)
            launcher_still[i] = launcher.crop(w * i, 0, w, h);
        launcher_firing = new BufferedImage[7];
        for (int i = 0; i < launcher_firing.length; i++)
            launcher_firing[i] = launcher.crop(w * i, h, w, h);
        launcher_dying = new BufferedImage[7];
        for (int i = 0; i < launcher_dying.length; i++)
            launcher_dying[i] = launcher.crop(w * i, h * 2, w, h);

        //
        // missile animations
        //
        SpriteSheet missile = new SpriteSheet("/textures/missile.png");
        w = 30;
        h = 16;
        missile_moving = new BufferedImage[3];
        for (int i = 0; i < missile_moving.length; i++)
            missile_moving[i] = missile.crop(w * i, 0, w, h);
        missile_explosion = new BufferedImage[3];
        for (int i = 0; i < missile_explosion.length; i++)
            missile_explosion[i] = missile.crop(w * i, h, w, h);

        //
        // poop animations
        //
        SpriteSheet poop = new SpriteSheet("/textures/poop.png");
        w = 25;
        h = 25;

        poop_moving = new BufferedImage[12];
        for (int i = 0; i < 2; i++)
            poop_moving[i] = poop.crop(w * i, 0, w, h);
        for (int i = 2; i < poop_moving.length; i++)
            poop_moving[i] = poop.crop(w * 2, 0, w, h);

        poop_impact = new BufferedImage[4];
        for (int i = 0; i < poop_impact.length; i++)
            poop_impact[i] = poop.crop(w * i, h, w, h);

        poop_explosion = new BufferedImage[4];
        for (int i = 0; i < poop_explosion.length; i++)
            poop_explosion[i] = poop.crop(w * i, h * 2, w, h);

        //
        // ram animations
        //
        SpriteSheet ram = new SpriteSheet("/textures/ram.png");
        int ramwidth = 90, ramheight = 64;

        ram_moving = new BufferedImage[3];
        for (int i = 0; i < ram_moving.length; i++)
            ram_moving[i] = ram.crop(105 * i, 0, ramwidth, ramheight);

        ram_frenzy = new BufferedImage[3];
        for (int i = 0; i < ram_frenzy.length; i++)
            ram_frenzy[i] = ram.crop(105 * i, ramheight, ramwidth, ramheight);

        ram_dying = new BufferedImage[8];
        for (int i = 0; i < ram_dying.length; i++)
            ram_dying[i] = ram.crop(105 * (i % 4), ramheight * (i / 4 + 2),
                                    ramwidth, ramheight);

        //
        // tank animations
        //
        SpriteSheet tank = new SpriteSheet("/textures/tank.png");
        w = 75;
        h = 64;

        tank_moving = new BufferedImage[4];
        for (int i = 0; i < tank_moving.length; i++)
            tank_moving[i] = tank.crop(w * i, 0, w, h);

        tank_frenzy = new BufferedImage[4];
        for (int i = 0; i < tank_frenzy.length; i++)
            tank_frenzy[i] = tank.crop(w * i, h, w, h);

        tank_dying = new BufferedImage[8];
        for (int i = 0; i < tank_dying.length; i++)
            tank_dying[i] = tank.crop(w * (i % 4), h * (i / 4 + 2), w, h);
    }
}
