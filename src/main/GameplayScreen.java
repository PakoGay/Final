package main;

import entity.Player;
import tile.TileManager;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import entity.Enemy;
//Логика уровня

public class GameplayScreen implements Screen {
    public final int originalTileSize = 16;
    public final int scale = 3;

    public final int tileSize = originalTileSize * scale;       // 48
    public final int maxScreenCol = 32;
    public final int maxScreenRow = 21;
    public final int screenWidth  = maxScreenCol * tileSize;    // 1635
    public final int screenHeight = maxScreenRow * tileSize;    // 1024

    public final int maxWorldCol = 150;
    public final int maxWorldRow = 150;

    public final int FPS = 60;// целевой FPS

    public final Clip walkSound  = SoundManager.get("/sound/walk.wav");
    public final Clip musicLoop = SoundManager.get("/sound/music.wav");

    public final TileManager tileM;
    public final CollisionChecker cChecker;
    public final Player player;
    public final Enemy enemy;
    public final KeyHandler keyH = new KeyHandler();
    public final List<Enemy> enemies = new ArrayList<>();

    public GameplayScreen() {
        tileM    = new TileManager(this);
        cChecker = new CollisionChecker(this);
        player   = new Player(this, keyH);
        musicLoop.loop(Clip.LOOP_CONTINUOUSLY);
        enemy = new Enemy(this, tileSize * 44, tileSize * 46);
        Enemy clone = (Enemy) enemy.clone();
        Enemy clone1 = (Enemy) enemy.clone();
        Enemy clone2= (Enemy) enemy.clone();
        Enemy clone3 = (Enemy) enemy.clone();
        Enemy clone4 = (Enemy) enemy.clone();
        Enemy clone5 = (Enemy) enemy.clone();
        Enemy clone6 = (Enemy) enemy.clone();
        Enemy clone7 = (Enemy) enemy.clone();
        Enemy clone8 = (Enemy) enemy.clone();
        Enemy clone9 = (Enemy) enemy.clone();
        clone.worldX = tileSize * 46;   // можно менять стартовую позицию
        clone.worldY = tileSize * 46;
        clone1.worldX = tileSize * 45;
        clone1.worldY = tileSize * 50;
        clone2.worldX = tileSize * 47;
        clone2.worldY = tileSize * 39;
        clone3.worldX = tileSize * 45;
        clone3.worldY = tileSize * 40;
        clone4.worldX = tileSize * 56;
        clone4.worldY = tileSize * 114;
        clone5.worldX = tileSize * 57;
        clone5.worldY = tileSize * 115;
        clone6.worldX = tileSize * 58;
        clone6.worldY = tileSize * 119;
        clone7.worldX = tileSize * 128;
        clone7.worldY = tileSize * 79;
        clone8.worldX = tileSize * 125;
        clone8.worldY = tileSize * 82;
        clone9.worldX = tileSize * 129;
        clone9.worldY = tileSize * 83;
        enemies.add(enemy);
        enemies.add(clone);
        enemies.add(clone1);
        enemies.add(clone2);
        enemies.add(clone3);
        enemies.add(clone4);
        enemies.add(clone5);
        enemies.add(clone6);
        enemies.add(clone7);
        enemies.add(clone8);
        enemies.add(clone9);

    }

    @Override public void update(float dt) {
        player.update();
        for (Enemy e : enemies) e.update();
    }

    @Override public void render(Graphics2D g) {
        tileM.draw(g);
        for (Enemy e : enemies) e.draw(g);
        player.draw(g);
        Font font = new Font("Arial", Font.BOLD, 26);  // имя шрифта, стиль, размер
        g.setFont(font);
        g.setColor(Color.RED);
        g.drawString("Health: " + player.health, screenWidth - 800, 25);
    }
}