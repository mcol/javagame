package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import assets.entities.Player;
import gfx.Font;

public class HUD {

    /** Width of the displayed graphics. */
    private static final int width = 100;

    /** Height of the displayed graphics. */
    private static final int height = 25;

    /** Dimension of the icon. */
    private static final int icon = 25;

    /** Distance between the bars. */
    private static final int gap = 20;

    /** Size of the border. */
    private static final int border = 3;

    /** Minimum size of the bar. */
    private static final int minbar = 5;

    /** Bar colours. */
    private static final Color timeColor = new Color(0xeebb22);
    private static final Color timeTransparentColor = new Color(0x99eebb22, true);
    private static final Color healthColor = new Color(0xcc0000);
    private static final Color healthTransparentColor = new Color(0x99cc0000, true);
    private static final Color poopColor = new Color(0x774824);
    private static final Color poopTransparentColor = new Color(0x99774824, true);

    /** Distance from the left side of the display. */
    private final int offset;

    /** The active player. */
    private final Player player;

    /** Images used in the hud. */
    private BufferedImage hudBar, timeIcon, healthIcon, poopIcon;

    /** Constructor. */
    public HUD(Player player, int gameWidth) {
        BufferedImage image;
        this.player = player;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/textures/hud.png"));
            hudBar = image.getSubimage(0, 0, 200, 50);
            timeIcon = image.getSubimage(200, 0, 50, 50);
            healthIcon = image.getSubimage(250, 0, 50, 50);
            poopIcon = image.getSubimage(300, 0, 50, 50);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        offset = gameWidth - 3 * (width + icon + gap);
    }

    public void render(Graphics g) {

        // make the hud transparent when the player goes under it
        boolean transparent = player.getGameX() + player.getWidth() > offset - icon &&
                              player.getY() < 25;
        int barOffset;

        // time bar
        int time = player.getTime();
        int tbar = time * (width - border) / Player.MAX_TIME + minbar;
        barOffset = offset + icon;
        g.drawImage(timeIcon, barOffset - icon - 5, 20, icon, icon, null);
        g.drawImage(hudBar, barOffset, 20, width + minbar, height, null);
        g.setColor(transparent ? timeTransparentColor : timeColor);
        g.fillRect(barOffset + border, 20 + border,
                   tbar - border, height - 2 * border);

        // health bar
        int health = player.getHealth();
        int hbar = health * (width - border) / Player.MAX_HEALTH + minbar;
        barOffset += width + icon + gap;
        g.drawImage(healthIcon, barOffset - icon - 5, 20, icon, icon, null);
        g.drawImage(hudBar, barOffset, 20, width + minbar, height, null);
        g.setColor(transparent ? healthTransparentColor : healthColor);
        g.fillRect(barOffset + border, 20 + border,
                   health > 0 ? hbar - border : 0, height - 2 * border);

        // poop bar
        int poop = player.getPoop();
        int pbar = poop * (width - border) / Player.MAX_POOP + minbar;
        barOffset += width + icon + gap;
        g.drawImage(poopIcon, barOffset - icon - 5, 20, icon, icon, null);
        g.drawImage(hudBar, barOffset, 20, width  + minbar, height, null);
        g.setColor(transparent ? poopTransparentColor : poopColor);
        g.fillRect(barOffset + border, 20 + border,
                   poop > 0 ? pbar - border : 0, height - 2 * border);

        // score
        Font.setColour(Color.WHITE, transparent);
        Font.renderMessage(g, "" + player.getScore(), offset - gap, 17,
                           Font.Size.SMALL, false);

        // level
        transparent = player.getGameX() < 125 && player.getY() < 25;
        Font.setColour(Color.WHITE, transparent);
        Font.renderMessage(g, "Lev " + player.getLevel(), gap, 17,
                           Font.Size.SMALL, true);
    }
}
