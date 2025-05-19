package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MainMenuScreen implements Screen, MouseListener, MouseMotionListener {
    private final GameCanvas canvas;
    private final GameplayScreen gameplay;
    private BufferedImage bg, startImg, exitImg, aboutImg;
    private BufferedImage startImgHover, exitImgHover, aboutImgHover;
    private final Rectangle startBtn, exitBtn, aboutBtn;
    private boolean isStartHovered = false;
    private boolean isExitHovered = false;
    private boolean isAboutHovered = false;

    public MainMenuScreen(GameplayScreen gameplay, GameCanvas canvas) {
        this.gameplay = gameplay;
        this.canvas = canvas;

        try {
            bg = ImageIO.read(getClass().getResource("/menu/background.png"));
            startImg = ImageIO.read(getClass().getResource("/menu/start.png"));
            exitImg = ImageIO.read(getClass().getResource("/menu/exit.png"));
            aboutImg = ImageIO.read(getClass().getResource("/menu/about.png"));
            startImgHover = ImageIO.read(getClass().getResource("/menu/start1.png"));
            exitImgHover = ImageIO.read(getClass().getResource("/menu/exit1.png"));
            aboutImgHover = ImageIO.read(getClass().getResource("/menu/about1.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int w = gameplay.screenWidth;
        int h = gameplay.screenHeight;
        int btnW = 500, btnH = 150;
        int x = (w - btnW) / 2 + 350;
        int y = 350, spacing = 150;

        startBtn = new Rectangle(x, y, btnW, btnH);
        exitBtn = new Rectangle(x, y + spacing, btnW, btnH);
        aboutBtn = new Rectangle(x, y + spacing * 2, btnW, btnH);

        canvas.setFocusable(true);
        canvas.requestFocusInWindow();
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(bg, 0, 0, gameplay.screenWidth, gameplay.screenHeight, null);
        // Используем изображения для hover в зависимости от состояния
        g.drawImage(isStartHovered ? startImgHover : startImg, startBtn.x, startBtn.y, startBtn.width, startBtn.height, null);
        g.drawImage(isExitHovered ? exitImgHover : exitImg, exitBtn.x, exitBtn.y, exitBtn.width, exitBtn.height, null);
        g.drawImage(isAboutHovered ? aboutImgHover : aboutImg, aboutBtn.x, aboutBtn.y, aboutBtn.width, aboutBtn.height, null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
        if (startBtn.contains(p)) {
            canvas.removeMouseListener(this);
            canvas.removeMouseMotionListener(this);
            canvas.setScreen(gameplay);
        } else if (exitBtn.contains(p)) {
            System.exit(0);
        } else if (aboutBtn.contains(p)) {
            JOptionPane.showMessageDialog(canvas, "About this game");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point p = e.getPoint();
        isStartHovered = startBtn.contains(p);
        isExitHovered = exitBtn.contains(p);
        isAboutHovered = aboutBtn.contains(p);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }
}

