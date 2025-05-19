package entity;

import main.GameplayScreen;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Простая неподвижная NPC с набором строк-диалогов.
 */
public class NPC {
    private final GameplayScreen gp;
    public final int worldX, worldY;
    public final BufferedImage sprite;
    public final String[] dialogues;

    public NPC(GameplayScreen gp,
               int worldX, int worldY,
               BufferedImage sprite,
               String[] dialogues) {
        this.gp        = gp;
        this.worldX    = worldX;
        this.worldY    = worldY;
        this.sprite    = sprite;
        this.dialogues = dialogues;
    }

    /** NPC не ходит */
    public void update() { }

    /** Рисуем спрайт NPC */
    public void draw(Graphics2D g2) {
        int sx = worldX - gp.player.worldX + gp.player.screenX;
        int sy = worldY - gp.player.worldY + gp.player.screenY;
        g2.drawImage(sprite, sx, sy, gp.tileSize, gp.tileSize, null);
    }

    /** Проверяем, что игрок в пределах одного тайла */
    public boolean isPlayerNear() {
        int dx = Math.abs(gp.player.worldX - worldX);
        int dy = Math.abs(gp.player.worldY - worldY);
        return dx <= gp.tileSize && dy <= gp.tileSize;
    }
}
