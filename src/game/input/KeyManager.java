package game.input;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

public class KeyManager {

    /** Associates actions to a keystroke press and release. */
    public static void addKeyBinding(JFrame frame, int keyStroke,
                                     ActionListener actionPressed,
                                     ActionListener actionReleased) {

        InputMap im = frame.getRootPane()
                           .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = frame.getRootPane().getActionMap();

        if (actionPressed != null) {
            String id = keyStroke + "pressed";
            im.put(KeyStroke.getKeyStroke(keyStroke, 0, false), id);
            am.put(id, new AbstractAction() {
                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent e) {
                    actionPressed.actionPerformed(e);
                }
            });
        }

        if (actionReleased != null) {
            String id = keyStroke + "released";
            im.put(KeyStroke.getKeyStroke(keyStroke, 0, true), id);
            am.put(id, new AbstractAction() {
                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent e) {
                    actionReleased.actionPerformed(e);
                }
            });
        }
    }

    /** Disables the actions associated to the given keystrokes. */
    public static void removeKeyBindings(JFrame frame, int[] keyStrokes) {
        InputMap im = frame.getRootPane()
                           .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        for (int keyStroke : keyStrokes)
            im.put(KeyStroke.getKeyStroke(keyStroke, 0, false), "none");
    }
}
