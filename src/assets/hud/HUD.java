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
        // health bar
        float hbar = (float) player.getHealth() / Player.DEFAULT_HEALTH * width;
        g.drawImage(image, 0, 20, width, height, null);
        g.setColor(new Color(0xcc0000));
        g.fillRoundRect(-20, 20, (int) hbar - 10, height, 30, 30);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
