package entity;

import main.GameplayScreen;

public class UIHealthObserver implements HealthObserver {
    private final GameplayScreen gp;

    public UIHealthObserver(GameplayScreen gp) {
        this.gp = gp;
    }

    @Override
    public void onHealthChanged(int newHealth) {
        gp.ui.startHealthAnimation(newHealth);
    }
}