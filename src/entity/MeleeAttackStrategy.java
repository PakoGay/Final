package entity;

public class MeleeAttackStrategy implements AttackStrategy {
    @Override
    public void attack(Entity attacker, Entity target) {
        // Пример простой атаки: уменьшение здоровья цели на урон атакующего
        target.health -= attacker.attackDamage;
    }
}