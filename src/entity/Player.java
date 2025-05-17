package entity;

import main.GameplayScreen;
import main.KeyHandler;
import entity.CharacterState;
import entity.IdleState;
import entity.AttackState;
import main.SoundManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{
    KeyHandler keyH;
    private AttackStrategy attackStrategy = new MeleeAttackStrategy();

    public final int screenX;
    public final int screenY;

    public Player(GameplayScreen gp, KeyHandler keyH){
        super(gp);
        this.keyH=keyH;

        screenX = gp.screenWidth / 2-(gp.tileSize/2);
        screenY = gp.screenHeight / 2-(gp.tileSize/2);

        solidArea=new Rectangle();
        solidArea.x=20;
        solidArea.y=25;
        solidArea.width=15;
        solidArea.height=15;
        setDefaulValues();

        getPlayerImage();
        changeState(new IdleState());
    }
    public void performAttack(Entity target) {
        attackStrategy.attack(this, target);
    }
    public void setDefaulValues(){
        worldX = gp.tileSize*76;
        worldY = gp.tileSize*77;
        speed = 5;
        direction = "down";
        health = 20;
        attackCooldown = 1200;
        attackDamage   = 10;
    }
    BufferedImage[][] frames = new BufferedImage[4][8]; // 0-UP,1-DOWN,2-LEFT,3-RIGHT

    private static final int UP=0, DOWN=1, LEFT=2, RIGHT=3;
    public int  attackSpriteNum    = 0;
    public int  attackFrameCounter    = 0;
    public static final int ATTACK_FRAMES = 6;
    public BufferedImage[][] attackFrames = new BufferedImage[4][ATTACK_FRAMES];
    public void getPlayerImage(){
        try{
            for (int i = 0; i < 8; i++) {

                frames[UP][i]    = ImageIO.read(getClass().getResource("/player/up"    + (i+1) + ".png"));
                frames[DOWN][i]  = ImageIO.read(getClass().getResource("/player/down"  + (i+1) + ".png"));
                frames[LEFT][i]  = ImageIO.read(getClass().getResource("/player/left"  + (i+1) + ".png"));
                frames[RIGHT][i] = ImageIO.read(getClass().getResource("/player/right" + (i+1) + ".png"));
            }
            for (int i = 0; i < ATTACK_FRAMES; i++) {
                attackFrames[UP]   [i] = ImageIO.read(getClass().getResource("/player/attackup"   + (i+1) + ".png"));
                attackFrames[DOWN] [i] = ImageIO.read(getClass().getResource("/player/attackdown" + (i+1) + ".png"));
                attackFrames[LEFT] [i] = ImageIO.read(getClass().getResource("/player/attackleft" + (i+1) + ".png"));
                attackFrames[RIGHT][i] = ImageIO.read(getClass().getResource("/player/attackright"+ (i+1) + ".png"));
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void update() {
        state.update(this);

        boolean moving = keyH.upPressed || keyH.downPressed ||
                keyH.leftPressed || keyH.rightPressed;

        if (moving) {
            if (keyH.upPressed)    direction = "up";
            if (keyH.downPressed)  direction = "down";
            if (keyH.leftPressed)  direction = "left";
            if (keyH.rightPressed) direction = "right";

            collisionOn = false;
            gp.cChecker.checkTile(this);

            if (!collisionOn) {
                switch (direction) {
                    case "up"    -> worldY -= speed;
                    case "down"  -> worldY += speed;
                    case "left"  -> worldX -= speed;
                    case "right" -> worldX += speed;
                }
            }

            spriteCounter++;
            if (spriteCounter > 10) {
                spriteNum = spriteNum % 8 + 1;  // 1-8 по кругу
                spriteCounter = 0;
            }
            if (moving && !gp.walkSound.isRunning()) {
                SoundManager.getInstance().play("walk");
            }
        }
    }
    public void draw(Graphics2D g2){
        int dir = switch (direction) {      // перевод строки→индекс
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

    }

    @Override
    public void defaultUpdate() {

    }
}
