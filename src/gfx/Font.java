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
    private static final int charsize = 8;

    /** Size of each character when rendered. */
    private static final int fontsize = charsize * 3;

    /** Renders a message using the font characters. */
    public static void renderMessage(Graphics g, String msg, int x, int y,
                                     boolean leftAligned) {

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
        int rgb = colour.getRGB();
        BufferedImage sheet = font.getSheet();
        int[] pixels = sheet.getRGB(0, 0, width, height, null, 0, width);
        for (int i = 0; i < pixels.length; i++)
            pixels[i] = toRGB(pixels[i], rgb);
        sheet.setRGB(0, 0, width, height, pixels, 0, width);
    }

    /** Changes the colour of a non-transparent pixel. */
    private static int toRGB(int pixel, int rgb) {
        int alpha = ((pixel >> 24) & 0xff) << 24;
        return alpha | (rgb ^ 0xff000000);
    }
}
