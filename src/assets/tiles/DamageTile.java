package assets.tiles;

public class DamageTile extends Tile {

    /** Amount of damage caused by the tile. */
    private final int damage;

    /** Constructor. */
    public DamageTile(Tiles tile, int damage) {
        super(tile);
        this.damage = damage;
    }

    /** Returns the damaged produced by the tile. */
    @Override
    public int getDamage() {
        return damage;
    }
}
