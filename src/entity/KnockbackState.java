package entity;
public class KnockbackState implements CharacterState {
    private final int dx, dy;
    private int ticksRemaining;

    public KnockbackState(int dx, int dy, int ticks) {
        this.dx = dx;
        this.dy = dy;
        this.ticksRemaining = ticks;
    }

    @Override
    public void enter(Entity e) {
    }

    @Override
    public void update(Entity e) {
        if (ticksRemaining > 0) {
            e.worldX += dx;
            e.worldY += dy;
            ticksRemaining--;
        } else {
            e.changeState(new IdleState());
        }
    }

    @Override
    public void exit(Entity e) {
    }
}