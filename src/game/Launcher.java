package game;

public class Launcher {
    public static void main(String[] args) {
        Game game = new Game("Poop Game", 800, 800 / 16 * 9);
        game.start();
    }
}
