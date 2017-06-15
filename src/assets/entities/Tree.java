package assets.entities;

import assets.Assets;
import game.Handler;

public class Tree extends StaticEntity {

    /** Constructor. */
    public Tree(Handler handler, int x, int y) {
        super(handler, x, y, Assets.tree, 128, 128);

        // bounding box
        setBounds(62, 5, 18, 90);
    }
}
