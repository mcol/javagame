package game.scores;

import java.io.Serializable;

public class Score implements Serializable, Comparable<Score> {

    private static final long serialVersionUID = 1L;

    /** Maximum name length returned by the class. */
    private static final int MAX_NAME_LENGTH = 5;

    /** Name of the player. */
    private final String name;

    /** Score achieved. */
    private final int score;

    /** Constructor. */
    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }

    /** Comparator function. */
    @Override
    public int compareTo(Score score) {
        return ((Integer) score.getScore()).compareTo(getScore());
    }

    // getters and setters

    /** Returns the name of the player. */
    public String getName() {
        return name.substring(0, Math.min(name.length(), MAX_NAME_LENGTH));
    }

    /** Returns the score. */
    public int getScore() {
        return score;
    }
}
