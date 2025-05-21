package entity;

import main.GameplayScreen;
import main.KeyHandler;
import main.SoundManager;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{
    KeyHandler keyH;
    private java.util.List<HealthObserver> healthObservers = new java.util.ArrayList<>();
    private Command moveUpCommand;
    private Command moveDownCommand;
    private Command moveLeftCommand;
    private Command moveRightCommand;
    private AttackStrategy attackStrategy = new MeleeAttackStrategy();
    public final int screenX;
    public final int screenY;

    public Player(GameplayScreen gp, KeyHandler keyH){
        super(gp);
        this.keyH=keyH;
        moveUpCommand    = new MoveUpCommand(this);
        moveDownCommand  = new MoveDownCommand(this);
        moveLeftCommand  = new MoveLeftCommand(this);
        moveRightCommand = new MoveRightCommand(this);

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
        health = 3;
        notifyHealthChanged();
        attackCooldown = 1200;
        attackDamage   = 10;
    }
    public void moveUp() {
        direction = "up";
        collisionOn = false;
        gp.cChecker.checkTile(this);
        if (!collisionOn) worldY -= speed;
        animateWalk();
    }
    public void moveDown() {
        direction = "down";
        collisionOn = false;
        gp.cChecker.checkTile(this);
        if (!collisionOn) worldY += speed;
        animateWalk();
    }
    public void moveLeft() {
        direction = "left";
        collisionOn = false;
        gp.cChecker.checkTile(this);
        if (!collisionOn) worldX -= speed;
        animateWalk();
    }
    public void moveRight() {
        direction = "right";
        collisionOn = false;
        gp.cChecker.checkTile(this);
        if (!collisionOn) worldX += speed;
        animateWalk();
    }
    private void animateWalk() {
        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum = spriteNum % 8 + 1;
            spriteCounter = 0;
        }
        if (!gp.walkSound.isRunning()) {
            SoundManager.getInstance().play("walk");
        }
    }
    public void addHealthObserver(HealthObserver observer) {
        healthObservers.add(observer);
    }
    public void removeHealthObserver(HealthObserver observer) {
        healthObservers.remove(observer);
    }
    public void notifyHealthChanged() {
        for (HealthObserver observer : healthObservers) {
            observer.onHealthChanged(this.health);
        }
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
        if (keyH.upPressed)    moveUpCommand.execute();
        if (keyH.downPressed)  moveDownCommand.execute();
        if (keyH.leftPressed)  moveLeftCommand.execute();
        if (keyH.rightPressed) moveRightCommand.execute();
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
