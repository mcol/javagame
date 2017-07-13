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

    /** Dimensions of the background image. */
    private final int imageWidth, imageHeight;

    /** Minimum dimensions between the background image and the game window. */
    private final int minWidth, minHeight;

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
        finally {
            imageWidth = image.getWidth();
            imageHeight = image.getHeight();
        }
        this.dx = dx;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.minWidth = Math.min(gameWidth, imageWidth);
        this.minHeight = Math.min(gameHeight, imageHeight);
    }

    public void tick() {
        // limit the movement to the available image size
        x = (x - dx) % (imageWidth - minWidth);
    }

    public void render(Graphics g) {
        g.drawImage(image.getSubimage((int) x, (int) y, minWidth, minHeight),
                    0, 0, gameWidth, gameHeight, null);
    }
}
