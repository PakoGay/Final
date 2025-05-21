package entity;

import main.GameplayScreen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;


public class WheatNPC extends Entity {
    private static final int ANIM_FRAMES = 5;
    private static final int UP    = 0, DOWN  = 1, LEFT  = 2, RIGHT = 3;
    private static final int SPEED = 2;
    private final BufferedImage[][] frames = new BufferedImage[4][ANIM_FRAMES];
    private int spriteCounter = 0;
    private int spriteNum     = 1;
    private final int animDelay = 10;
    private String direction = "down";
    private int    moveTicker, moveDuration;
    private final Random rnd = new Random();
    private final Rectangle bounds;

    public WheatNPC(GameplayScreen gp,
                         int startX, int startY,
                         String spritePathPrefix,
                         Rectangle fieldBounds) {
        super(gp);
        this.worldX = startX;
        this.worldY = startY;
        this.bounds = fieldBounds;

        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);

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
            moveTicker = 0;
        }
        moveTicker--;

        spriteCounter++;
        if (spriteCounter > animDelay) {
            spriteNum = spriteNum % ANIM_FRAMES + 1;
            spriteCounter = 0;
        }
    }
    public void update() {
        defaultUpdate();
    }

    public void draw(Graphics2D g2) {
        int dirIdx = switch (direction) {
            case "up"    -> UP;
            case "down"  -> DOWN;
            case "left"  -> LEFT;
            default      -> RIGHT;
        };

        int sx = worldX - gp.player.worldX + gp.player.screenX;
        int sy = worldY - gp.player.worldY + gp.player.screenY;

        BufferedImage img = frames[dirIdx][spriteNum - 1];
        g2.drawImage(img, sx, sy, gp.tileSize, gp.tileSize, null);
    }
}
