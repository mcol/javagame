package gfx;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Background {

    /** Image to display in the background. */
    private BufferedImage image;

    /** Horizontal dimension of the game in pixels. */
    private final int gameWidth;

    /** Vertical dimension of the game in pixels. */
    private final int gameHeight;

    /** Current position of the background. */
    private float x, y;

    /** Horizontal movement of the background. */
    private final float dx;

    /** Constructor. */
    public Background(String path, float dx,
                      int gameWidth, int gameHeight) {
        try {
            image = ImageIO.read(getClass().getResourceAsStream(path));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.dx = dx;
    }

    public void tick() {
        x = (gameWidth + x - dx) % gameWidth;
    }

    public void render(Graphics g) {
        g.drawImage(image.getSubimage((int) x, (int) y, gameWidth, gameHeight),
                    0, 0, gameWidth, gameHeight, null);
    }
}
