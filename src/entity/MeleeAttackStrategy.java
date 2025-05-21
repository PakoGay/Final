package entity;

public class MeleeAttackStrategy implements AttackStrategy {
    @Override
    public void attack(Entity attacker, Entity target) {
        target.health -= attacker.attackDamage;
    }
}