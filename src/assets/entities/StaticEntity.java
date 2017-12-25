package assets.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class StaticEntity extends Entity {

    /** Image representing the entity. */
    protected BufferedImage image;

    /** Constructor. */
    public StaticEntity(BufferedImage image, float x, float y,
                        int width, int height) {
        super(handler, x, y, width, height);
        this.image = image;
    }

    @Override
    public void render(Graphics g) {

        // avoid unnecessary rendering
        if (isOffScreen())
            return;

        g.drawImage(image, getGameX(), getGameY(), width, height, null);
    }
}
