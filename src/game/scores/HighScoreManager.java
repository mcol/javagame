package game.scores;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class HighScoreManager {

    /** Name of the highscore file. */
    private static final String HIGHSCORE_FILE = "res/highscores.dat";

    /** Maximum number of scores stored in the highscore file. */
    private static final int MAX_SCORES = 10;

    /** List of scores. */
    private ArrayList<Score> scores;

    /** Constructor. */
    @SuppressWarnings("unchecked")
    public HighScoreManager() {
        try {
            FileInputStream is = new FileInputStream(HIGHSCORE_FILE);
            ObjectInputStream in = new ObjectInputStream(is);
            scores = (ArrayList<Score>) in.readObject();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            scores = new ArrayList<Score>();
        }
    }

    /** Returns whether the score can enter the highscore list. */
    public boolean isHighScore(int score) {
        if (scores.size() >= MAX_SCORES &&
            score <= scores.get(MAX_SCORES - 1).getScore())
            return false;
        return true;
    }

    /** Adds a new score to the highscore list. */
    public int addScore(String name, int score) {

        if (!isHighScore(score))
            return -1;

        // add the score to the list
        Score newscore = new Score(name, score);
        scores.add(newscore);
        Collections.sort(scores);
        updateHighScoreFile();
        return scores.indexOf(newscore);
    }

    /** Writes the current highscore list to file. */
    private void updateHighScoreFile() {
        try {
            FileOutputStream os = new FileOutputStream(HIGHSCORE_FILE);
            ObjectOutputStream out = new ObjectOutputStream(os);
            out.writeObject(scores);
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Creates the highscore string. */
    public String[] getHighScores() {
        int nscores = Math.min(scores.size(), MAX_SCORES);
        String[] highScores = new String[nscores];
        for (int i = 0; i < nscores; i++) {
            Score score = scores.get(i);
            highScores[i] = String.format("%-" + Score.MAX_NAME_LENGTH + "s %4d",
                                          score.getName(), score.getScore());
        }
        return highScores;
    }
}
