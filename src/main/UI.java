package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class UI {
    private final GameplayScreen gp;
    private int displayedHealth;
    private int targetHealth;
    private int healthAnimTimer = 0;
    private static final int HEARTS = 3;
    private static final int ANIM_FRAMES = 5;
    private static final int ANIM_DURATION = 20;

    private BufferedImage[] heartFrames = new BufferedImage[ANIM_FRAMES];
    private int[] heartAnimFrame = new int[HEARTS];

    public UI(GameplayScreen gp) {
        this.gp = gp;
        this.displayedHealth = gp.player.health;
        this.targetHealth = gp.player.health;
        try {
            heartFrames[0] = ImageIO.read(getClass().getResource("/hp/hp.png"));
            heartFrames[1] = ImageIO.read(getClass().getResource("/hp/hp1.png"));
            heartFrames[2] = ImageIO.read(getClass().getResource("/hp/hp2.png"));
            heartFrames[3] = ImageIO.read(getClass().getResource("/hp/hp3.png"));
            heartFrames[4] = ImageIO.read(getClass().getResource("/hp/hp4.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startHealthAnimation(int newHealth) {
        // Ограничиваем newHealth и displayedHealth допустимыми значениями
        int safeNewHealth = Math.max(0, Math.min(HEARTS, newHealth));
        int safeDisplayedHealth = Math.max(0, Math.min(HEARTS, displayedHealth));
        if (safeNewHealth < safeDisplayedHealth) {
            for (int i = safeNewHealth; i < safeDisplayedHealth; i++) {
                heartAnimFrame[i] = 1;
            }
            healthAnimTimer = ANIM_DURATION;
        }
        targetHealth = safeNewHealth;
    }

    public void update() {
        if (displayedHealth > targetHealth && healthAnimTimer > 0) {
            for (int i = targetHealth; i < displayedHealth; i++) {
                if (heartAnimFrame[i] < ANIM_FRAMES - 1) {
                    heartAnimFrame[i]++;
                }
            }
            healthAnimTimer--;
            if (healthAnimTimer == 0) {
                displayedHealth = targetHealth;
                // Сбросить анимацию
                for (int i = 0; i < HEARTS; i++) heartAnimFrame[i] = 0;
            }
        } else {
            displayedHealth = targetHealth;
        }
    }

    public void draw(Graphics2D g2) {
        int x = 20, y = 20;
        for (int i = 0; i < HEARTS; i++) {
            BufferedImage img;
            if (i < displayedHealth) {
                img = heartFrames[0];
            } else if (heartAnimFrame[i] > 0) {
                img = heartFrames[heartAnimFrame[i]];
            } else {
                img = heartFrames[ANIM_FRAMES - 1];
            }
            g2.drawImage(img, x + i * 40, y, 32, 32, null);
        }
    }
}