package assets.entities;

import assets.Assets;

public abstract class MovingSurface extends StaticEntity {

    /** Constructor. */
    public MovingSurface(float x, float y) {
        super(Assets.platform, x, y, 95, 32);
    }
}
