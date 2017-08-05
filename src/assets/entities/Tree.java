package assets.entities;

import assets.Assets;

public class Tree extends StaticEntity {

    /** Constructor. */
    public Tree(float x, float y) {
        super(Assets.tree, x, y, 128, 128);
        setBounds(62, 5, 18, 90);
    }
}
