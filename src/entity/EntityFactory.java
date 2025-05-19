package entity;

import entity.Enemy;
import main.GameplayScreen;

public class EntityFactory {
    public static Enemy createEnemy(GameplayScreen gp, int worldX, int worldY) {
        Enemy enemy = new Enemy(gp, worldX, worldY);
        return enemy;
    }
}