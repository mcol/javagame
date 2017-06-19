package gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteSheet {

    private final BufferedImage sheet;

    /** Constructor. */
    public SpriteSheet(String path) {
        sheet = loadImage(path);
    }

    /**	Loads an image file into a BufferedImage. */
    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(SpriteSheet.class.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }

    /** Extracts a BufferedImage from a sprite sheet. */
    public BufferedImage crop(int x, int y, int width, int height) {
        return sheet.getSubimage(x, y, width, height);
    }
}
