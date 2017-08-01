package game;

import assets.Assets;
import game.states.GameState;
import game.states.HelpState;
import game.states.MenuState;
import game.states.State;
import gfx.Camera;

public class Game implements Runnable {

    /** Dimensions of the game window in pixels. */
    public static final int WIDTH = 1100, HEIGHT = WIDTH / 16 * 9;

    /** Number of frames to be rendered per second. */
    public static final int FPS = 60;

    /** The name of the game. */
    private static final String title = "Poop game";

    /** The game display. */
    private static Display display;

    /** The object handler. */
    private static Handler handler;

    /** The camera. */
    private static Camera camera;

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
        display = new Display(title, WIDTH, HEIGHT);
    }

    private void init() {
        Assets.init();

        handler = new Handler();
        camera = new Camera(handler);

        setMenuState(-1);
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

            if (timer > 1_000_000_000) {
                display.setTitle(title + " | " + ticks + " ups, " +
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

    /** Returns the title of the game. */
    public String getTitle() {
        return title;
    }

    public static void setGameState() {
        State.setState(new GameState(handler));
    }

    public static void setHelpState() {
        State.setState(new HelpState(handler, State.getState()));
    }

    public static void setMenuState(int score) {
        State.setState(new MenuState(handler, score == -1 ? title
                                                          : "Score: " + score));
    }
}
