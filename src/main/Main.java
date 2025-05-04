package main;

public class Main {
    public static void main(String[] args) {
        GameplayScreen level1 = new GameplayScreen();
        GameCanvas    game   = new GameCanvas(level1);
        MainMenuScreen menu = new MainMenuScreen(level1, game);
        game.setScreen(menu);
        game.addMouseListener(menu);
        game.addKeyListener(level1.keyH);
        game.start();
    }
}
