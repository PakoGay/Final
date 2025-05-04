package entity;

import java.awt.Rectangle;

public class IdleState implements CharacterState {
    @Override public void enter(Entity e) { }

    @Override
    public void update(Entity e) {
        long now = System.currentTimeMillis();

        if (e instanceof Player p) {
            if (p.keyH.kPressed
                    && now - p.lastAttackTime >= p.attackCooldown) {
                p.lastAttackTime = now;
                p.changeState(new AttackState());
                return;
            }
            p.defaultUpdate();
            return;
        }
        if (e instanceof Enemy en) {
            if (now - en.lastAttackTime >= en.attackCooldown) {
                int tx = en.worldX;
                int ty = en.worldY;
                int ts = en.gp.tileSize;
                switch (en.direction) {
                    case "up"    -> ty -= ts;
                    case "down"  -> ty += ts;
                    case "left"  -> tx -= ts;
                    default      -> tx += ts;
                }
                Rectangle attackRect = new Rectangle(tx, ty, ts, ts);

                Rectangle playerRect = new Rectangle(
                        en.gp.player.worldX,
                        en.gp.player.worldY,
                        ts, ts
                );

                if (attackRect.intersects(playerRect)) {
                    en.lastAttackTime = now;
                    en.changeState(new AttackState());
                    return;
                }
            }

            en.defaultUpdate();
        }
    }

    @Override public void exit(Entity e) { }
}
