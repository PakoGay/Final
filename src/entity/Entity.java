package entity;

import main.GameplayScreen;

import java.awt.*;

public abstract class Entity implements Cloneable {
    public int maxHealth;
    public int health;
    public CharacterState state;
    public long lastAttackTime = 0;
    public long attackCooldown  = 500;  // мс между ударами
    public int  attackDamage    = 5;


    GameplayScreen gp;
    public int worldX,worldY;
    public int speed;
    public String direction;
    public int spriteCounter=0;
    public int spriteNum=4;
    public Rectangle solidArea = new Rectangle(0,0,48,48);
    public boolean collisionOn = false;
    public Entity(GameplayScreen gp){
        this.gp = gp;
    }
    public void changeState(CharacterState newState) {
        if (state != null) state.exit(this);
        state = newState;
        state.enter(this);
    }
    @Override
    public Entity clone() {
        try {
            Entity copy = (Entity) super.clone();
            copy.solidArea = new Rectangle(this.solidArea);
            return copy;
        } catch (CloneNotSupportedException e) {

            throw new AssertionError(e);
        }
    }
    public abstract void defaultUpdate();

}
