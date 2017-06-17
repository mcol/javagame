package game;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Display {
    private JFrame frame;
    private Canvas canvas;

    private String title;

    /** Dimensions of the window in pixels. */
    private int width, height;

    /** Constructor. */
    public Display(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;

        createDisplay();
    }

    private void createDisplay() {
        Dimension size = new Dimension(width, height);
        canvas = new Canvas();
        canvas.setPreferredSize(size);
        canvas.setMaximumSize(size);
        canvas.setMinimumSize(size);
        canvas.setFocusable(false);

        frame = new JFrame(title);
        frame.setResizable(false);
        frame.add(canvas);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public JFrame getFrame() {
        return frame;
    }

    /** Sets the title string. */
    public void setTitle(String title) {
        frame.setTitle(title);
    }
}
