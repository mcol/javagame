package game.scores;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import game.Game;
import game.input.KeyManager;

public class HighScoreManager {

    /** Name of the highscore file. */
    private static final String HIGHSCORE_FILE = "res/highscores.dat";

    /** Maximum number of scores stored in the highscore file. */
    private static final int MAX_SCORES = 10;

    /** List of scores. */
    private ArrayList<Score> scores;

    /** */
    private static final Scanner scanner = new Scanner(System.in);

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

    /** Adds a new score to the highscore list. */
    public boolean addScore(int score) {

        // check if the score can enter the list
        if (scores.size() >= MAX_SCORES &&
            score <= scores.get(MAX_SCORES - 1).getScore())
            return false;

        // get the player's name
        KeyManager.removeKeyBindings(Game.getDisplay(), new int[] { KeyEvent.VK_Q });
        System.out.print("Enter name: ");
        String name = scanner.next();

        // add the score to the list
        scores.add(new Score(name, score));
        Collections.sort(scores);
        updateHighScoreFile();
        return true;
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

    /** Creates the high score string. */
    public String[] getHighScores() {
        int nscores = Math.min(scores.size(), MAX_SCORES);
        String[] highScores = new String[nscores];
        for (int i = 0; i < nscores; i++) {
            Score score = scores.get(i);
            highScores[i] = String.format("%-5s %4d",
                                          score.getName(), score.getScore());
        }
        return highScores;
    }
}
