package entity;

import main.GameplayScreen;
import entity.CharacterState;
import entity.IdleState;
import entity.AttackState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Enemy extends Entity {

    private static final int en_speed   = 3;   //шаг в тайлах
    public static final int ai_rate = 30;  //раз в N кадров пересчёт направления
    private static final int tile_detect = 12;
    public long lastAttackTime = 0;
    public long attackCooldown;


    public int aiCounter = 0;
    public static final int anim_frames = 5;

    /* 0-UP,1-DOWN,2-LEFT,3-RIGHT  ×  8 кадров */
    private final BufferedImage[][] frames = new BufferedImage[4][anim_frames];
    public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;

    public static final int ATTACK_FRAMES = 3;
    public BufferedImage[][] attackFrames = new BufferedImage[4][ATTACK_FRAMES];

    private final PathFinder pathFinder;
    private       List<Point> path       = Collections.emptyList();
    private       int         pathIndex  = 0;

    private int  attackSpriteNum    = 0;
    public int attackFrameCounter = 0;
    public final int attackFrameDelay = 3;

    public Enemy(GameplayScreen gp, int startWorldX, int startWorldY) {
        super(gp);
        this.worldX = startWorldX;
        this.worldY = startWorldY;
        this.speed  = en_speed;
        this.direction = "down";
        this.health = 15;
        this.attackCooldown = 2000;
        this.attackDamage   = 1;

        solidArea = new Rectangle(8, 16, 32, 32);   // хит-бокс поменьше
        pathFinder = new PathFinder(gp);
        loadImages();
        changeState(new IdleState());
    }
    //ресурс лоадер
    private void loadImages() {
        try {
            for (int i = 0; i < anim_frames; i++) {
                changeState(new IdleState());
                frames[UP][i]    = ImageIO.read(getClass().getResource("/orc/up"    + (i + 1) + ".png"));
                frames[DOWN][i]  = ImageIO.read(getClass().getResource("/orc/down"  + (i + 1) + ".png"));
                frames[LEFT][i]  = ImageIO.read(getClass().getResource("/orc/left"  + (i + 1) + ".png"));
                frames[RIGHT][i] = ImageIO.read(getClass().getResource("/orc/right" + (i + 1) + ".png"));
            }
            for (int i = 0; i < ATTACK_FRAMES; i++) {
                attackFrames[UP]   [i] = ImageIO.read(getClass().getResource("/orc/attackup"    + (i+1) + ".png"));
                attackFrames[DOWN] [i] = ImageIO.read(getClass().getResource("/orc/attackdown"  + (i+1) + ".png"));
                attackFrames[LEFT] [i] = ImageIO.read(getClass().getResource("/orc/attackleft"  + (i+1) + ".png"));
                attackFrames[RIGHT][i] = ImageIO.read(getClass().getResource("/orc/attackright" + (i+1) + ".png"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        state.update(this);
        if (!(state instanceof IdleState)) {
            return;
        }
        if (!isPlayerInRange()) {
            return;
        }
        int startCol = worldX / gp.tileSize;
        int startRow = worldY / gp.tileSize;
        int goalCol  = gp.player.worldX / gp.tileSize;
        int goalRow  = gp.player.worldY / gp.tileSize;

        if (path.isEmpty() || pathIndex >= path.size()) {
            path      = pathFinder.findPath(startCol, startRow, goalCol, goalRow);
            pathIndex = 0;
        }

        if (!path.isEmpty() && pathIndex < path.size()) {
            Point next = path.get(pathIndex);
            int tx = next.x * gp.tileSize;
            int ty = next.y * gp.tileSize;

            if      (worldX < tx) direction = "right";
            else if (worldX > tx) direction = "left";
            else if (worldY < ty) direction = "down";
            else if (worldY > ty) direction = "up";

            switch (direction) {
                case "up"    -> worldY = Math.max(worldY - speed, ty);
                case "down"  -> worldY = Math.min(worldY + speed, ty);
                case "left"  -> worldX = Math.max(worldX - speed, tx);
                default      -> worldX = Math.min(worldX + speed, tx);
            }

            if (worldX == tx && worldY == ty) {
                pathIndex++;
            }

            spriteCounter++;
            if (spriteCounter > 10) {
                spriteNum     = (spriteNum % anim_frames) + 1;
                spriteCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (screenX + gp.tileSize < 0 || screenX > gp.screenWidth ||
                screenY + gp.tileSize < 0 || screenY > gp.screenHeight) {
            return;
        }

        int dir = switch (direction) {
            case "up"    -> UP;
            case "down"  -> DOWN;
            case "left"  -> LEFT;
            default      -> RIGHT;
        };
        if (state instanceof AttackState) {
            // удар
            BufferedImage atkImg = attackFrames[dir][attackSpriteNum];
            g2.drawImage(atkImg, screenX, screenY, gp.tileSize, gp.tileSize, null);
        } else {
            // ходьба
            BufferedImage walkImg = frames[dir][spriteNum - 1];
            g2.drawImage(walkImg, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
        String hpText = String.valueOf(health);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));   // можно подобрать свой шрифт/размер
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth  = fm.stringWidth(hpText);
        int textHeight = fm.getAscent();
        int textX = screenX + (gp.tileSize - textWidth) / 2;
        int textY = screenY - 5;
        g2.drawString(hpText, textX, textY);
    }
    public boolean isPlayerInRange() {
        int dx = gp.player.worldX - worldX;
        int dy = gp.player.worldY - worldY;
        double distance = Math.hypot(dx, dy);
        return distance <= tile_detect * gp.tileSize;
    }
    @Override
    public Enemy clone() {
        return (Enemy) super.clone();
    }
    @Override
    public void defaultUpdate() {
    }
}
