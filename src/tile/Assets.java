package tile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public final class Assets {

    private static final Map<String, BufferedImage> CACHE = new HashMap<>();

    private Assets() {}

    public static BufferedImage get(String path) {
        return CACHE.computeIfAbsent(path, p -> {
            try {
                return ImageIO.read(Assets.class.getResource(p));
            } catch (IOException | NullPointerException e) {
                throw new RuntimeException("Не удалось загрузить: " + p, e);
            }
        });
    }
}
