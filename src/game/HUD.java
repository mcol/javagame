package game;

import java.awt.Color;
import java.awt.Graphics;
import assets.Assets;
import assets.entities.Player;
import gfx.Font;

public class HUD {

    /** Width of the displayed graphics. */
    private static final int width = 100;

    /** Height of the displayed graphics. */
    private static final int height = 25;

    /** Dimension of the icon. */
    private static final int icon = 25;

    /** Distance between the bars. */
    private static final int gap = 20;

    /** Size of the border. */
    private static final int border = 3;

    /** Minimum size of the bar. */
    private static final int minbar = 5;

    /** Bar colours. */
    private static final Color[] tColor = {new Color(0xeebb22),
                                           new Color(0x88eebb22, true)};
    private static final Color[] hColor = {new Color(0xcc0000),
                                           new Color(0x88cc0000, true)};
    private static final Color[] pColor = {new Color(0x774824, true),
                                           new Color(0x88774824, true)};

    /** Distance from the left side of the display. */
    private final int offset;

    /** Current player. */
    private final Player player;

    /** Values currently displayed. */
    private int score, time, health, poop;

    /** Constructor. */
    public HUD(Player player, int gameWidth) {
        this.player = player;
        offset = gameWidth - 3 * (width + icon + gap);
    }

    public void tick() {

        // always update score and time
        if (score < player.getScore())
            score++;
        int timeTarget = player.getTime();
        time = time < timeTarget ? time += 20 : timeTarget;

        // don't update the rest at every call
        if (Game.getTicksTime() % 5 != 0)
            return;

        int healthTarget = player.getHealth();
        health = health < healthTarget ? health + 1
               : health > healthTarget ? health - 1 : healthTarget;
        int poopTarget = player.getPoop();
        poop = poop < poopTarget ? poop + 1 : poopTarget;
    }

    public void render(Graphics g) {

        // make the HUD transparent when the player goes under it
        boolean transparent = player.getGameX() + player.getWidth() > offset - icon &&
                              player.getY() < 25;
        int idx = transparent ? 1 : 0;
        int barOffset;

        // time bar
        int tbar = time * (width - border) / Player.MAX_TIME + minbar;
        barOffset = offset + icon;
        if (player.getTime() > 10 * Game.FPS || Game.getTicksTime() % 50 > 10)
            g.drawImage(Assets.tIcon[idx], barOffset - icon - 5, 20, icon, icon, null);
        g.drawImage(Assets.hudBar[idx], barOffset, 20, width + minbar, height, null);
        g.setColor(tColor[idx]);
        g.fillRect(barOffset + border, 20 + border,
                   tbar - border, height - 2 * border);

        // health bar
        int hbar = health * (width - border) / Player.MAX_HEALTH + minbar;
        barOffset += width + icon + gap;
        if (player.getHealth() > 10 || Game.getTicksTime() % 50 > 10)
            g.drawImage(Assets.hIcon[idx], barOffset - icon - 5, 20, icon, icon, null);
        g.drawImage(Assets.hudBar[idx], barOffset, 20, width + minbar, height, null);
        g.setColor(hColor[idx]);
        g.fillRect(barOffset + border, 20 + border,
                   health > 0 ? hbar - border : 0, height - 2 * border);

        // poop bar
        int pbar = poop * (width - border) / Player.MAX_POOP + minbar;
        barOffset += width + icon + gap;
        if (player.getPoop() > 5 || Game.getTicksTime() % 50 > 10)
            g.drawImage(Assets.pIcon[idx], barOffset - icon - 5, 20, icon, icon, null);
        g.drawImage(Assets.hudBar[idx], barOffset, 20, width + minbar, height, null);
        g.setColor(pColor[idx]);
        g.fillRect(barOffset + border, 20 + border,
                   poop > 0 ? pbar - border : 0, height - 2 * border);

        // score
        Font.setColour(Color.WHITE, transparent);
        Font.renderMessage(g, "" + score, offset - gap, 17,
                           Font.Size.SMALL, false);

        // level
        transparent = player.getGameX() < 165 && player.getY() < 25;
        Font.setColour(Color.WHITE, transparent);
        Font.renderMessage(g, "Lev " + player.getLevel(), gap, 17,
                           Font.Size.SMALL, true);
    }

    // Returns whether the health bar reports the current value
    public boolean isHealthCurrent() {
        return health == player.getHealth();
    }
}
