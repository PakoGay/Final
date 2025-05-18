package entity;

public class ConsoleHealthObserver implements HealthObserver {
    @Override
    public void onHealthChanged(int newHealth) {
        System.out.println("Здоровье игрока: " + newHealth);
    }
}