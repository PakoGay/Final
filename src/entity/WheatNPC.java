package entity;

import main.GameplayScreen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * NPC, который блуждает внутри заданного прямоугольника и
 * анимированно ходит (5 кадров × 4 направления).
 */
public class WheatNPC extends Entity {
    private static final int ANIM_FRAMES = 5;
    private static final int UP    = 0, DOWN  = 1, LEFT  = 2, RIGHT = 3;

    // Тайловая скорость
    private static final int SPEED = 2;

    // Спрайты ходьбы [direction][frame]
    private final BufferedImage[][] frames = new BufferedImage[4][ANIM_FRAMES];

    // Счётчики анимации
    private int spriteCounter = 0;
    private int spriteNum     = 1;
    private final int animDelay = 10;

    // Для рандомного блуждания
    private String direction = "down";
    private int    moveTicker, moveDuration;
    private final Random rnd = new Random();

    // Зона, в пределах которой ходит NPC
    private final Rectangle bounds;

    public WheatNPC(GameplayScreen gp,
                         int startX, int startY,
                         String spritePathPrefix,
                         Rectangle fieldBounds) {
        super(gp);
        this.worldX = startX;
        this.worldY = startY;
        this.bounds = fieldBounds;

        // хитбокс под спрайт
        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);

        // Загрузка кадров анимации
        try {
            for (int i = 0; i < ANIM_FRAMES; i++) {
                frames[UP]   [i] = ImageIO.read(getClass().getResource(
                        spritePathPrefix + "up"    + (i+1) + ".png"));
                frames[DOWN] [i] = ImageIO.read(getClass().getResource(
                        spritePathPrefix + "down"  + (i+1) + ".png"));
                frames[LEFT] [i] = ImageIO.read(getClass().getResource(
                        spritePathPrefix + "left"  + (i+1) + ".png"));
                frames[RIGHT][i] = ImageIO.read(getClass().getResource(
                        spritePathPrefix + "right" + (i+1) + ".png"));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load wheat NPC frames", e);
        }
    }

    /** Здесь вся старая AI-логика переезжает вместе с анимацией */
    @Override
    public void defaultUpdate() {
        // 1) Если пора менять направление — выбираем новое
        if (moveTicker <= 0) {
            switch (rnd.nextInt(4)) {
                case 0 -> direction = "up";
                case 1 -> direction = "down";
                case 2 -> direction = "left";
                default-> direction = "right";
            }
            moveDuration = 30 + rnd.nextInt(90);
            moveTicker   = moveDuration;
        }
        // 2) Пробуем шагнуть
        int nx = worldX, ny = worldY;
        switch (direction) {
            case "up"    -> ny -= SPEED;
            case "down"  -> ny += SPEED;
            case "left"  -> nx -= SPEED;
            default      -> nx += SPEED;
        }
        if (bounds.contains(nx, ny)) {
            worldX = nx;
            worldY = ny;
        } else {
            // упёрлись в границу — сразу сменим направление
            moveTicker = 0;
        }
        moveTicker--;

        // 3) Анимация ходьбы
        spriteCounter++;
        if (spriteCounter > animDelay) {
            spriteNum = spriteNum % ANIM_FRAMES + 1;  // 1..5 по кругу
            spriteCounter = 0;
        }
    }

    /** Неподвижный стейт — все в defaultUpdate */
    public void update() {
        defaultUpdate();
    }

    public void draw(Graphics2D g2) {
        // 1) определяем, какой ряд кадров рисовать
        int dirIdx = switch (direction) {
            case "up"    -> UP;
            case "down"  -> DOWN;
            case "left"  -> LEFT;
            default      -> RIGHT;
        };

        // 2) вычисляем экранные координаты
        int sx = worldX - gp.player.worldX + gp.player.screenX;
        int sy = worldY - gp.player.worldY + gp.player.screenY;

        // 3) рисуем текущий кадр анимации
        BufferedImage img = frames[dirIdx][spriteNum - 1];
        g2.drawImage(img, sx, sy, gp.tileSize, gp.tileSize, null);
    }
}
