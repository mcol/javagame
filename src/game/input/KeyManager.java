package game.input;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class KeyManager {

    /** Associates actions to a keystroke press and release. */
    public static void addKeyBinding(JPanel panel, int keyStroke,
                                     ActionListener actionPressed,
                                     ActionListener actionReleased) {

        InputMap im = panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = panel.getActionMap();

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

    /** Associates an action to a keystroke press. */
    public static void addKeyBinding(JPanel panel, int keyStroke,
                                     ActionListener actionPressed) {
        addKeyBinding(panel, keyStroke, actionPressed, null);
    }

    /** Disables the actions associated to the given keystrokes. */
    public static void removeKeyBindings(JPanel panel, int[] keyStrokes) {
        InputMap im = panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        for (int keyStroke : keyStrokes)
            im.put(KeyStroke.getKeyStroke(keyStroke, 0, false), "none");
    }
}
