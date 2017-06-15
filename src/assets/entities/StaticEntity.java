package assets.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import game.Handler;

public abstract class StaticEntity extends Entity {

    protected BufferedImage image;

    /** Constructor. */
    public StaticEntity(Handler handler, int x, int y,
                        BufferedImage image, int width, int height) {
        super(handler, x, y, width, height);
        this.image = image;
    }

    @Override
    public void tick() { /* nothing to do */ }

    @Override
    public void render(Graphics g) {

        // avoid unnecessary rendering
        if (offScreen())
            return;

        g.drawImage(image,
                    (int) (x - handler.getCamera().getxOffset()),
                    (int) (y - handler.getCamera().getyOffset()),
                    width, height, null);
        g.setColor(Color.RED);
//        g.fillRect((int) (x + bounds.x - handler.getCamera().getxOffset()),
//                   (int) (y + bounds.y - handler.getCamera().getyOffset()),
//                   bounds.width, bounds.height);
    }
}
