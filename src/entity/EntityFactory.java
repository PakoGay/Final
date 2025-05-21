package entity;

import entity.Enemy;
import main.GameplayScreen;
import java.awt.*;

public class EntityFactory {
    public static Enemy createEnemy(GameplayScreen gp, int worldX, int worldY) {
        Enemy enemy = new Enemy(gp, worldX, worldY);
        return enemy;
    }
    public static WheatNPC createWheatWalker(
            GameplayScreen gp,
            int startWorldX, int startWorldY,
            String spritePrefix,
            Rectangle fieldBounds) {
        return new WheatNPC(
                gp,
                startWorldX,
                startWorldY,
                spritePrefix,
                fieldBounds
        );
    }
}