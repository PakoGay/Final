package main;

import javax.sound.sampled.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class SoundManager {

    private static final Map<String, Clip> CACHE = new HashMap<>();

    private SoundManager() {}

    public static Clip get(String path) {
        return CACHE.computeIfAbsent(path, p -> {
            try {
                URL url  = SoundManager.class.getResource(p);
                AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                Clip c = AudioSystem.getClip();
                c.open(ais);
                return c;
            } catch (Exception e) {
                throw new RuntimeException("Не удалось загрузить звук: " + p, e);
            }
        });
    }
}
