package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class MainMenuScreen implements Screen, MouseListener {
    private final GameCanvas canvas;
    private final GameplayScreen gameplay;
    private BufferedImage  bg, startImg, exitImg, aboutImg;
    private final Rectangle startBtn, exitBtn, aboutBtn;

    public MainMenuScreen(GameplayScreen gameplay, GameCanvas canvas) {
        this.gameplay = gameplay;
        this.canvas   = canvas;

        try {
            bg       = ImageIO.read(getClass().getResource("/menu/background.png"));
            startImg = ImageIO.read(getClass().getResource("/menu/start.png"));
            exitImg  = ImageIO.read(getClass().getResource("/menu/exit.png"));
            aboutImg = ImageIO.read(getClass().getResource("/menu/about.png"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        int w = gameplay.screenWidth;
        int h = gameplay.screenHeight;
        int btnW = 500, btnH = 150;
        int x = (w - btnW) / 2 + 350;
        int y = 350, spacing = 150;

        startBtn = new Rectangle(x,y,btnW, btnH);
        exitBtn  = new Rectangle(x,y + spacing,btnW, btnH);
        aboutBtn = new Rectangle(x,y + spacing*2, btnW, btnH);

        canvas.setFocusable(true);
        canvas.requestFocusInWindow();
    }

    @Override public void update(float dt) {
    }

    @Override public void render(Graphics2D g) {
        g.drawImage(bg,0,0,gameplay.screenWidth, gameplay.screenHeight,null);
        g.drawImage(startImg,startBtn.x, startBtn.y,startBtn.width,startBtn.height,null);
        g.drawImage(exitImg, exitBtn.x, exitBtn.y, exitBtn.width, exitBtn.height, null);
        g.drawImage(aboutImg,aboutBtn.x,aboutBtn.y,aboutBtn.width,aboutBtn.height,null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
        if (startBtn.contains(p)) {
            canvas.removeMouseListener(this);
            canvas.setScreen(gameplay);
        }
        else if (exitBtn.contains(p)) {
            System.exit(0);
        }
        else if (aboutBtn.contains(p)) {
            JOptionPane.showMessageDialog(canvas, "About this game");
        }
    }
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
