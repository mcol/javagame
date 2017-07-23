package game;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import game.states.State;

public class Display extends JPanel {

    private static final long serialVersionUID = 1L;
    private JFrame frame;

    /** The title string to be displayed. */
    private final String title;

    /** Dimensions of the window in pixels. */
    private final int width, height;

    /** The current game state. */
    private State state;

    /** Number of frames rendered per second. */
    public int frames;

    /** Constructor. */
    public Display(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.state = null;
        this.frames = 0;

        createDisplay();
    }

    /** Sets up the display. */
    private void createDisplay() {
        Dimension size = new Dimension(width, height);
        this.setPreferredSize(size);
        this.setMaximumSize(size);
        this.setMinimumSize(size);
        this.setFocusable(false);

        frame = new JFrame(title);
        frame.setResizable(false);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (state != null)
            state.render(g);
        frames++;
    }

    // getters and setters

    /** Sets the title string. */
    public void setTitle(String title) {
        frame.setTitle(title);
    }

    /** Sets the current state. */
    public void setState(State state) {
        this.state = state;
    }
}
