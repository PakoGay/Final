package main;

import entity.Player;
import tile.TileManager;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import entity.EntityFactory;
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

    public final Clip walkSound  = SoundManager.getInstance().getClip("walk");
    public final Clip musicLoop  = SoundManager.getInstance().getClip("music");

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
        enemy = EntityFactory.createEnemy(this, tileSize * 44, tileSize * 46);
        enemies.add(enemy);
        enemies.add(EntityFactory.createEnemy(this, tileSize * 46, tileSize * 46));
        enemies.add(EntityFactory.createEnemy(this, tileSize * 45, tileSize * 50));
        enemies.add(EntityFactory.createEnemy(this, tileSize * 47, tileSize * 39));
        enemies.add(EntityFactory.createEnemy(this, tileSize * 45, tileSize * 40));
        enemies.add(EntityFactory.createEnemy(this, tileSize * 56, tileSize * 114));
        enemies.add(EntityFactory.createEnemy(this, tileSize * 57, tileSize * 115));
        enemies.add(EntityFactory.createEnemy(this, tileSize * 58, tileSize * 119));
        enemies.add(EntityFactory.createEnemy(this, tileSize * 128, tileSize * 79));
        enemies.add(EntityFactory.createEnemy(this, tileSize * 125, tileSize * 82));
        enemies.add(EntityFactory.createEnemy(this, tileSize * 129, tileSize * 83));
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