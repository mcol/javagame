package gfx;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import utils.Utils;

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

    /** Current horizontal position of the background. */
    private float x;

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
        this.x = Utils.randomFloat(0, dx > 0 ? imageWidth : -imageWidth);
        this.dx = dx;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.minWidth = Math.min(gameWidth, imageWidth);
        this.minHeight = Math.min(gameHeight, imageHeight);
    }

    public void tick() {
        x = (x + dx) % imageWidth;
    }

    public void render(Graphics g) {
        BufferedImage sub;
        int x0 = (int) x, x1, subWidth;
        if (dx < 0) {
            // left-align the images
            subWidth = Math.min(imageWidth + x0, minWidth);
            x1 = 0;
            sub = image.getSubimage(-x0, 0, subWidth, minHeight);
            g.drawImage(sub, x1, 0, subWidth, gameHeight, null);

            // fill in any remaining area
            while (x1 + subWidth < gameWidth) {
                x1 += subWidth;
                subWidth = Math.min(gameWidth - x1, imageWidth);
                sub = image.getSubimage(0, 0, subWidth, minHeight);
                g.drawImage(sub, x1, 0, subWidth, gameHeight, null);
            }
        }
        else if (dx > 0) {
            // right-align  the images
            subWidth = Math.min(imageWidth - x0, minWidth);
            x1 = gameWidth - subWidth;
            sub = image.getSubimage(imageWidth - subWidth - x0, 0,
                                    subWidth, minHeight);
            g.drawImage(sub, x1, 0, subWidth, gameHeight, null);

            // fill in any remaining area
            while (x1 > 0) {
                subWidth = Math.min(x1, minWidth);
                x1 -= subWidth;
                sub = image.getSubimage(imageWidth - subWidth, 0,
                                        subWidth, minHeight);
                g.drawImage(sub, x1, 0, subWidth, gameHeight, null);
            }
        }
    }
}
