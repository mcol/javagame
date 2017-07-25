package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import gfx.Font;
import utils.Utils;

public class MessageManager {

    private static class Message {

        /** Offset in pixels to centre the message. */
        private final static int OFFSET = 16;

        /** The message to be displayed. */
        public final String message;

        /** Coordinates where the message should be displayed. */
        public int x, y;

        /** Time when the message was added to the list. */
        private final long time;

        /** Constructor. */
        public Message(String message, int x, int y) {
            this.message = message;
            this.x = x + OFFSET;
            this.y = y + OFFSET;
            this.time = Game.getTicksTime();
        }
    }

    /** Interval for which the message is displayed in ticks. */
    private static final int MESSAGE_DISPLAY_INTERVAL = 1 * Game.FPS;

    /** The list of messages. */
    private static final ArrayList<Message> messages = new ArrayList<Message>();

    /** Add a message to the list. */
    public void addMessage(String message, float x, float y) {
        messages.add(new Message(message, (int) x, (int) y));
    }

    public void tick() {
        long now = Game.getTicksTime();
        for (int i = 0; i < messages.size(); i++) {
            Message msg = messages.get(i);
            msg.y -= Utils.randomInteger(1, 3);
            msg.x += Utils.randomInteger(-1, 1);
            if (now - msg.time > MESSAGE_DISPLAY_INTERVAL) {
                messages.remove(i);
                i--;
            }
        }
    }

    public void render(Graphics g) {
        for (int i = 0; i < messages.size(); i++) {
            Message msg = messages.get(i);
            Font.setColour(Color.WHITE, false);
            Font.renderMessage(g, msg.message, msg.x, msg.y,
                               Font.Size.MICRO, true);
        }
    }
}
