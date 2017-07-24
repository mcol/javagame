package assets.entities;

import utils.Utils;

public enum Items {

    // Row 1
    APPLE       ( 2,  0,  0,  5),
    BALL        ( 4,  1,  3,  5),
    BALLOON     ( 8,  4,  0, 15),
    BONE        ( 7,  0,  2, 25),
    BOOK        (15, 10,  1,  5),
    BOX         ( 3,  0,  1, 30),
    COLLAR      (10,  5,  1,  5),
    COOLER      ( 8,  2,  1,  5),
    EMPTY_1_2   ( 0,  0,  0,  0),
    EMPTY_1_1   ( 0,  0,  0,  0),

    // Row 2
    CUPCAKE     ( 5,  0,  2, 25),
    DISK        ( 4,  1,  1,  5),
    DRESS       ( 7,  0,  3,  5),
    EGG         ( 7,  2,  2, 10),
    FLOWER      (10,  1,  1,  5),
    GLASSES     ( 7,  0,  2,  5),
    GOLD        (50, 10, 10,  1),
    GRAPE       ( 4 , 3,  2,  10),
    EMPTY2_2    ( 0,  0,  0,  0),
    EMPTY2_1    ( 0,  0,  0,  0),

    // Row 3
    HAT         ( 5,  0,  1,  5),
    ICE         ( 2,  0,  0,  5),
    JAM         ( 4,  5,  1, 10),
    KEY         ( 8,  1,  1,  5),
    LEAF        (35,  0,  1,  3),
    LEAK        (20,  5,  3,  3),
    LETTER      (13,  1,  0,  5),
    MAGNET      (25, 10,  5,  5),
    EMPTY_3_2   ( 0,  0,  0,  0),
    EMPTY_3_1   ( 0,  0,  0,  0),

    // Row 4
    MINE        ( 3,  0,  3, 15),
    MUSHROOM    ( 1,  0,  0, 25),
    PILLS       ( 8,  5,  2,  5),
    POUCH       ( 5,  1,  1, 25),
    ROPE        (10,  0,  2,  5),
    RUBY        (30, 10,  5,  3),
    SAPPHIRE    (35,  5, 10,  2),
    SPOON       ( 3,  0,  1, 25),
    EMPTY_4_2   ( 0,  0,  0,  0),
    EMPTY_4_1   ( 0,  0,  0,  0),

    // Row 4
    STRAWBERRY  (11,  0,  2, 10),
    SUITCASE    ( 5,  5,  0,  5),
    SWEET       ( 9,  0,  0, 10),
    TICKET      (14,  0,  5,  5),
    TOMATO      ( 7,  2,  0, 15),
    VEST        ( 5,  0,  0, 15),
    WATCH       ( 1,  3,  3, 15),
    YARN        (17,  5,  5,  5),
    EMPTY_5_2   ( 0,  0,  0,  0),
    EMPTY_5_1   ( 0,  0,  0,  0);

    /** Number of items in each row of the spritesheet. */
    public static final int rowlength = 10;

    /** Total amount available across all items. */
    private static final int totalAvailability = sumAmounts();

    /** Value of the item. */
    private final int value;

    /** Amount of health provided by the item. */
    private final int health;

    /** Amount of poop provided by the item. */
    private final int poop;

    /** Available amount of the item. */
    private final int amount;

    /** Constructor. */
    Items(int value, int health, int poop, int amount) {
        this.value = value;
        this.health = health;
        this.poop = poop;
        this.amount = amount;
    }

    /** Computes the total rarity across all items. */
    private static int sumAmounts() {
        int sum = 0;
        for (Items item : values())
            sum += item.amount;
        return sum;
    }

    /** Returns an item at random according to its availability. */
    public static Items randomItem() {
        int rnd = Utils.randomInteger(0, totalAvailability - 1);
        int sum = 0;
        Items chosenItem = APPLE;
        for (Items item : values()) {
            sum += item.amount;
            if (sum > rnd) {
                chosenItem = item;
                break;
            }
        }

        return chosenItem;
    }

    // getters and setters

    /** Returns the value of the item. */
    public int getValue() {
        return value;
    }

    /** Returns the amount of health provided by the item. */
    public int getHealth() {
        return health;
    }

    /** Returns the amount of poop provided by the item. */
    public int getPoop() {
        return poop;
    }
}
