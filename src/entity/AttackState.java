package entity;

import java.awt.Rectangle;

public class AttackState implements CharacterState {
    private int  frameTicker;
    private int  spriteFrame;
    private boolean appliedHit;
    private static final int FRAME_DELAY = 8;
    private static final int ANIM_FRAMES = Player.ATTACK_FRAMES;

    @Override
    public void enter(Entity e) {
        frameTicker  = 0;
        spriteFrame  = 0;
        appliedHit   = false;

        if (e instanceof Player p) {
            p.attackSpriteNum    = 0;
            p.attackFrameCounter = 0;
        }
    }

    @Override
    public void update(Entity e) {
        if (!appliedHit && spriteFrame == 1

        ) {
            appliedHit = true;
            applyHit(e);
        }

        frameTicker++;
        if (frameTicker >= FRAME_DELAY) {
            frameTicker = 0;
            spriteFrame++;
            if (e instanceof Player p) {
                p.attackSpriteNum = spriteFrame;
            }
        }

        if (spriteFrame >= ANIM_FRAMES) {
            e.changeState(new IdleState());
        }
    }

    private void applyHit(Entity e) {
        int tx = e.worldX, ty = e.worldY;
        switch (e.direction) {
            case "up"    -> ty -= e.gp.tileSize;
            case "down"  -> ty += e.gp.tileSize;
            case "left"  -> tx -= e.gp.tileSize;
            default      -> tx += e.gp.tileSize;
        }
        Rectangle hitBox = new Rectangle(tx, ty, e.gp.tileSize, e.gp.tileSize);

        if (e instanceof Player p) {
            for (Enemy en : p.gp.enemies) {
                Rectangle er = new Rectangle(en.worldX, en.worldY, p.gp.tileSize, p.gp.tileSize);
                if (hitBox.intersects(er)) {
                    en.health -= p.attackDamage;

                    int strength = p.gp.tileSize / 4;
                    int duration = 8;
                    int dx = 0, dy = 0;
                    switch (p.direction) {
                        case "up"    -> dy =  strength;
                        case "down"  -> dy = -strength;
                        case "left"  -> dx =  strength;
                        default      -> dx = -strength;
                    }
                    en.changeState(new KnockbackState(dx, dy, duration));

                    break;
                }
            }
            p.gp.enemies.removeIf(enemy -> enemy.health <= 0);
        }
        else if (e instanceof Enemy en) {
            Player pl = en.gp.player;
            Rectangle pr = new Rectangle(pl.worldX, pl.worldY, en.gp.tileSize, en.gp.tileSize);
            if (hitBox.intersects(pr)) {
                pl.health -= en.attackDamage;
                if (pl.health <= 0) pl.setDefaulValues();
            }
        }
    }

    @Override
    public void exit(Entity e) { }
}
