package game;

public class Launcher {
    public static void main(String[] args) {
        final int width = 800;
        Game game = new Game("Poop Game", width, width / 16 * 9);
        game.start();
    }
}
