package game;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import assets.Assets;
import game.input.KeyManager;
import game.states.GameState;
import game.states.State;
import gfx.Camera;

public class Game implements Runnable {

    private Display display;

    /** Dimensions of the game window in pixels. */
    private int width, height;
    public String title;

    private boolean running = false;
    private Thread thread;

    private BufferStrategy bs;
    private Graphics g;

    private int fps = 60;

    // States
    private State gameState;

    // Input
    private KeyManager keyManager;

    // Camera
    private Camera camera;

    // Handler
    private Handler handler;

    /** Constructor. */
    public Game(String title, int width, int height) {
        this.width = width;
        this.height = height;
        this.title = title;
        keyManager = new KeyManager();
    }

    private void init() {
        display = new Display(title, width, height);
        display.getFrame().addKeyListener(keyManager);
        Assets.init();

        handler = new Handler(this);
        camera = new Camera(handler);

        gameState = new GameState(handler);
        State.setState(gameState);
    }

    private void tick() {
        keyManager.tick();

        if (State.getState() != null)
            State.getState().tick();
    }

    private void render() {
        bs = display.getCanvas().getBufferStrategy();
        if (bs == null) {
            display.getCanvas().createBufferStrategy(3);
            return;
        }

        g = bs.getDrawGraphics();

        // clear screen
        g.clearRect(0, 0, width, height);

        // draw
        if (State.getState() != null)
            State.getState().render(g);

        bs.show();
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

    public KeyManager getKeyManager() {
        return keyManager;
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
}
