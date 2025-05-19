package main;

import javax.sound.sampled.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class SoundManager {
    public Clip getClip(String name) {
        return cache.computeIfAbsent(name, n -> {
            try {
                URL url = SoundManager.class.getResource("/sound/" + n + ".wav");
                AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                Clip c = AudioSystem.getClip();
                c.open(ais);
                return c;
            } catch (Exception e) {
                throw new RuntimeException("Не удалось загрузить звук: " + n, e);
            }
        });
    }

    private static SoundManager instance;
    private final Map<String, Clip> cache = new HashMap<>();

    private SoundManager() {}

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void play(String name) {
        Clip clip = cache.computeIfAbsent(name, n -> {
            try {
                URL url = SoundManager.class.getResource("/sound/" + n + ".wav");
                AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                Clip c = AudioSystem.getClip();
                c.open(ais);
                return c;
            } catch (Exception e) {
                throw new RuntimeException("Не удалось загрузить звук: " + n, e);
            }
        });
        if (clip.isRunning()) clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }
}