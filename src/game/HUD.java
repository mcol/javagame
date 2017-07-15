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

    /** Distance between the bars. */
    private static final int gap = 20;

    /** Size of the border. */
    private static final int border = 3;

    /** Bar colours. */
    private static final Color healthColor = new Color(0xcc0000);
    private static final Color healthTransparentColor = new Color(0x99cc0000, true);
    private static final Color poopColor = new Color(0x774824);
    private static final Color poopTransparentColor = new Color(0x99774824, true);

    /** Distance from the left side of the display. */
    private final int offset;

    /** The active player. */
    private final Player player;
    private BufferedImage image;

    /** Constructor. */
    public HUD(Player player, int gameWidth) {
        this.player = player;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/textures/hud.png"));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        offset = gameWidth - (2 * width + 2 * gap);
    }

    public void render(Graphics g) {

        // make the hud transparent when the player goes under it
        boolean transparent = player.getGameX() + player.getWidth() > offset &&
                              player.getY() < 25;

        // health bar
        int hbar = player.getHealth() * (width - border) / Player.MAX_HEALTH;
        g.drawImage(image, offset, 20, width, height, null);
        g.setColor(transparent ? healthTransparentColor : healthColor);
        g.fillRect(offset + border, 20 + border,
                   hbar - border, height - 2 * border);

        // poop bar
        int pbar = player.getPoop() * (width - border) / Player.MAX_POOP;
        g.drawImage(image, offset + width + gap, 20, width, height, null);
        g.setColor(transparent ? poopTransparentColor : poopColor);
        g.fillRect(offset + width + gap + border, 20 + border,
                   pbar - border, height - 2 * border);

        // score
        Font.renderMessage(g, "" + player.getScore(), offset - gap, 17,
                           Font.Size.SMALL, false);
    }
}
