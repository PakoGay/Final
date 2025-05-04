package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

public class GameCanvas extends Canvas implements Runnable {

    private final JFrame frame;
    private volatile boolean running;
    private Thread gameThread;
    private Screen currentScreen;
    private final int screenWidth;
    private final int screenHeight;
    private final int fps;

    public GameCanvas(GameplayScreen startScreen) {

        this.currentScreen = startScreen;

        this.screenWidth  = startScreen.screenWidth;
        this.screenHeight = startScreen.screenHeight;
        this.fps = startScreen.FPS;

        setPreferredSize(new Dimension(screenWidth, screenHeight));

        frame = new JFrame("Final game");
        frame.add(this);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { stop(); }
        });
        frame.setVisible(true);


    }

    public void setScreen(Screen newScreen) {
        this.currentScreen = newScreen;
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        gameThread = new Thread(this, "GameLoop");
        gameThread.start();
    }


    public synchronized void stop() {
        running = false;
        try { gameThread.join(); } catch (InterruptedException ignored) {}
        frame.dispose();
    }

    @Override
    public void run() {
        createBufferStrategy(2);
        BufferStrategy bs = getBufferStrategy();

        long last = System.nanoTime();
        final double nsPerFrame = 1_000_000_000.0 / fps;

        while (running) {
            long now = System.nanoTime();
            if (now - last < nsPerFrame) continue;

            float dt = (float)((now - last) / 1_000_000_000.0);
            last = now;

            currentScreen.update(dt);

            do {
                do {
                    Graphics2D g = (Graphics2D) bs.getDrawGraphics();
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, screenWidth, screenHeight);
                    currentScreen.render(g);

                    g.dispose();
                } while (bs.contentsRestored());
                bs.show();
            } while (bs.contentsLost());
        }
    }
}
