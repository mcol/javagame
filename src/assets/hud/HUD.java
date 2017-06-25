package assets.hud;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import assets.entities.Player;

public class HUD {

    /** Width of the displayed graphics. */
    private static final int width = 150;

    /** Height of the displayed graphics. */
    private static final int height = 40;

    private Player player;
    private BufferedImage image;

    /** Constructor. */
    public HUD(Player player) {
        this.player = player;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/textures/hud.png"));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics g) {

        // shift to hide the rounded corners on the left side of the bars
        final int barOffset = 20;

        // size of the border in pixels
        final int border = 3;

        // diameter of the arc for the corners
        final int roundness = 25;

        // health bar
        float hbar = (float) player.getHealth() / Player.DEFAULT_HEALTH
                     * (width - barOffset);
        g.drawImage(image, -barOffset, 20, width, height, null);
        g.setColor(new Color(0xcc0000));
        g.fillRoundRect(-barOffset, 20 + border,
                        (int) hbar + barOffset - border, height - 2 * border,
                        roundness, roundness);

        // poop bar
        float pbar = (float) player.getPoop() / Player.MAX_POOP
                     * (width - barOffset);
        g.drawImage(image, -barOffset, 65, width, height, null);
        g.setColor(new Color(0x774824));
        g.fillRoundRect(-barOffset, 65 + border,
                        (int) pbar + barOffset - border, height - 2 * border,
                        roundness, roundness);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
