package gfx;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Font {

    /** Characters stored in the font sheet. */
    private static final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ "
                                      + "0123456789.,:;'\"!?$%()-=+/ ";

    /** Number of characters in each row. */
    private static final int rowlength = chars.length() / 2;

    /** Font sheet. */
    private static final SpriteSheet font = new SpriteSheet("/textures/font.png");

    /** Dimensions of the font sheet. */
    private static final int width = font.getSheet().getWidth();
    private static final int height = font.getSheet().getHeight();

    /** Size of each character in the font sheet. */
    private static final int charsize = 9;

    /** Available font sizes. */
    public enum Size {

        /** Available sizes */
        TITLE(8), LARGE(6), MEDIUM(4), SMALL(3), MINI(2), MICRO(1.7), TINY(1);

        /** Scaling factor. */
        private final double scale;

        /** Constructor. */
        Size(double scale) {
            this.scale = scale;
        }

        /** Returns the scaling to be applied to the font. */
        private double getScale() {
            return scale;
        }
    }

    /** Renders a message using the font characters. */
    public static void renderMessage(Graphics g, String msg, int x, int y,
                                     Size size, boolean leftAligned) {

        // size of the font in pixels
        int fontsize = (int) (charsize * size.getScale());

        // adjust the position if the message is right aligned
        if (!leftAligned)
            x -= msg.length() * fontsize;

        // convert to uppercase before matching
        msg = msg.toUpperCase();

        for (int i = 0; i < msg.length(); i++) {
            int charIndex = chars.indexOf(msg.charAt(i));

            // don't try to render a character not in the set
            if (charIndex < 0)
                continue;

            BufferedImage ch = font.crop((charIndex % rowlength) * charsize,
                                         (charIndex / rowlength) * charsize,
                                         charsize, charsize);
            g.drawImage(ch, x + i * fontsize, y, fontsize, fontsize, null);
        }
    }

    /** Sets the colour of the font. */
    public static void setColour(Color colour) {
        setColour(colour, false);
    }

    /** Sets the colour of the font with an optional alpha. */
    public static void setColour(Color colour, boolean transparent) {
        int rgb = colour.getRGB();
        BufferedImage sheet = font.getSheet();
        int[] pixels = sheet.getRGB(0, 0, width, height, null, 0, width);
        for (int i = 0; i < pixels.length; i++)
            pixels[i] = toRGB(pixels[i], rgb, transparent ? 0x88000000 : 0);
        sheet.setRGB(0, 0, width, height, pixels, 0, width);
    }

    /** Changes the colour of a non-transparent pixel. */
    private static int toRGB(int pixel, int rgb, int alpha) {
        boolean transparent = (pixel & 0xff000000) == 0;
        return transparent ? 0 : rgb ^ alpha;
    }
}
