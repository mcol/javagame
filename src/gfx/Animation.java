package gfx;

import java.awt.image.BufferedImage;

public class Animation {

    /** Array of images to animate. */
    private BufferedImage[] frames;

    /** Animation speed in milliseconds. */
    private int speed;

    /** Index of the current animation frame. */
    private int index;

    /** Whether the animation has been played at least once. */
    private boolean playedOnce;
    private long timer, lastTime;

    /** Constructor. */
    public Animation(BufferedImage[] frames, int speed) {
        this.frames = frames;
        this.speed = speed;
        index = 0;
        playedOnce = false;
        timer = 0;
        lastTime = System.currentTimeMillis();
    }

    public void tick() {
        long currentTime = System.currentTimeMillis();
        timer += currentTime - lastTime;
        lastTime = currentTime;

        // check if it's time to change frame
        if (timer > speed) {
            timer = 0;
            index++;
            if (index >= frames.length) {
                index = 0;
                playedOnce = true;
            }
        }
    }

    /** Returns the current animation frame. */
    public BufferedImage getCurrentFrame() {
        return frames[index];
    }

    /** Assigns the animation parameters from another animation. */
    public void assign(Animation animation) {
        this.frames = animation.frames;
        this.speed = animation.speed;
        this.reset();
    }

    /** Changes the animation frames. */
    public void setFrames(BufferedImage[] frames, int speed) {
        this.frames = frames;
        this.speed = speed;
        this.reset();
    }

    /** Resets the state of the animation. */
    private void reset() {
        index = 0;
        playedOnce = false;
    }

    /** Returns whether the animation has played at least once. */
    public boolean hasPlayedOnce() {
        return playedOnce;
    }
}
