package game;

import assets.Assets;
import game.scores.HighScoreManager;
import game.states.GameOverState;
import game.states.GameState;
import game.states.HelpState;
import game.states.HighScoreState;
import game.states.MenuState;
import game.states.State;
import gfx.Camera;

public class Game implements Runnable {

    /** Dimensions of the game window in pixels. */
    public static final int WIDTH = 1100, HEIGHT = WIDTH / 16 * 9;

    /** Number of frames to be rendered per second. */
    public static final int FPS = 60;

    /** The name of the game. */
    public static final String TITLE = "Poop game";

    /** The game display. */
    private static Display display;

    /** The object handler. */
    private static Handler handler;

    /** The camera. */
    private static Camera camera;

    /** List of high scores. */
    private static HighScoreManager highScoreManager;

    /** The menu state. */
    private static MenuState menuState;

    /** The main thread. */
    private Thread thread;

    /** Whether the game loop is running. */
    private boolean running = false;

    /** Current time in ticks. */
    private static long now = 0;

    /** Main function. */
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

    /** Constructor. */
    public Game() {
        display = new Display(TITLE, WIDTH, HEIGHT);
    }

    private void init() {
        Assets.init();

        handler = new Handler();
        camera = new Camera(handler);
        highScoreManager = new HighScoreManager();
        menuState = new MenuState(handler);
        setMenuState();
    }

    private void tick() {
        State.getState().tick();
        now++;
    }

    private void render() {
        display.repaint();
    }

    @Override
    public void run() {
        init();

        final double timePerTick = 1_000_000_000.0 / FPS;
        double delta = 0.0;
        long lastTime = System.nanoTime(), now;
        long timer = 0;
        int ticks = 0;

        while (running) {
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            timer += now - lastTime;
            lastTime = now;

            while (delta >= 1) {
                tick();
                ticks++;
                delta--;
            }
            render();
            sleep();

            if (timer > 1_000_000_000) {
                display.setTitle(TITLE + " | " + ticks + " ups, " +
                                 display.frames + " fps");
                ticks = 0;
                timer = 0;
                display.frames = 0;
            }
        }

        stop();
    }

    public synchronized void start() {
        if (running)
            return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running)
            return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void sleep() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** Adds a score to the highscore list. */
    public static int addHighScore(String name) {
        return highScoreManager.addScore(name, handler.getPlayer().getScore());
    }

    // getters and setters

    /** Returns the game display. */
    public static Display getDisplay() {
        return display;
    }

    /** Returns the camera. */
    public static Camera getCamera() {
        return camera;
    }

    /** Returns the time of the game in ticks. */
    public static long getTicksTime() {
        return now;
    }

    /** Returns whether the score can enter the highscore list. */
    public static boolean isHighScore(int score) {
        return highScoreManager.isHighScore(score);
    }

    /** Returns the highscore list. */
    public static String[] getHighScores() {
        return highScoreManager.getHighScores();
    }

    public static void setGameState() {
        State.setState(new GameState());
    }

    public static void setGameOverState(int score) {
        State.setState(new GameOverState(score));
    }

    public static void setHelpState() {
        State.setState(new HelpState(State.getState()));
    }

    public static void setHighScoreState(int score, int index) {
        State.setState(new HighScoreState(score, index));
    }

    public static void setMenuState() {
        State.setState(menuState);
    }
}
