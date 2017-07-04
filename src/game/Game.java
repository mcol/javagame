package game;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import assets.Assets;
import game.states.GameState;
import game.states.MenuState;
import game.states.State;
import gfx.Camera;

public class Game implements Runnable {

    /** Dimensions of the game window in pixels. */
    private final int width, height;

    /** The name of the game. */
    private final String title;

    private boolean running = false;
    private Thread thread;

    private final Display display;
    private BufferStrategy bs;
    private Graphics g;

    /** The number of frames per seconds to be rendered. */
    private final int fps = 60;

    // States
    private static State menuState;

    // Camera
    private static Camera camera;

    // Handler
    private static Handler handler;

    /** Constructor. */
    public Game(String title, int width, int height) {
        this.width = width;
        this.height = height;
        this.title = title;
        display = new Display(title, width, height);
    }

    private void init() {
        Assets.init();

        handler = new Handler(this);
        camera = new Camera(handler);

        menuState = new MenuState(handler);
        State.setState(menuState);
    }

    private void tick() {
        if (State.getState() != null)
            State.getState().tick();
    }

    private void render() {
        bs = display.getCanvas().getBufferStrategy();
        if (bs == null) {
            display.getCanvas().createBufferStrategy(3);
            return;
        }

        // get the current graphics context
        g = bs.getDrawGraphics();

        // draw graphics
        g.clearRect(0, 0, width, height);
        if (State.getState() != null)
            State.getState().render(g);

        // display the buffer
        bs.show();

        // dispose the graphics context
        g.dispose();
    }

    @Override
    public void run() {
        init();

        final double timePerTick = 1_000_000_000.0 / fps;
        double delta = 0.0;
        long lastTime = System.nanoTime(), now;
        long timer = 0;
        int ticks = 0;
        int frames = 0;

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
            frames++;

            if (timer > 1_000_000_000) {
                display.setTitle(title + " | " + ticks + " ups, " + frames + " fps");
                ticks = 0;
                timer = 0;
                frames = 0;
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

    public Display getDisplay() {
        return display;
    }

    public Camera getCamera() {
        return camera;
    }

    /** Returns the width of the game window in pixels. */
    public int getWidth() {
        return width;
    }

    /** Returns the height of the game window in pixels. */
    public int getHeight() {
        return height;
    }

    /** Returns the title of the game. */
    public String getTitle() {
        return title;
    }

    public void setGameState() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {}
        State.setState(new GameState(handler));
    }

    public void setMenuState() {
        State.setState(menuState);
    }
}
