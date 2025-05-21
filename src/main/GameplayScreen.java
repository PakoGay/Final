package main;

import entity.*;
import tile.Assets;
import tile.TileManager;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import entity.EntityFactory;
import entity.Enemy;

public class GameplayScreen implements Screen {
    public final int originalTileSize = 16;
    public final int scale = 3;

    public final int tileSize = originalTileSize * scale;       // 48
    public final int maxScreenCol = 32;
    public final int maxScreenRow = 21;
    public final int screenWidth  = maxScreenCol * tileSize;    // 1635
    public final int screenHeight = maxScreenRow * tileSize;    // 1024

    public final int maxWorldCol = 150;
    public final int maxWorldRow = 150;

    public final int FPS = 60;// целевой FPS

    public final Clip walkSound  = SoundManager.getInstance().getClip("walk");
    public final Clip musicLoop  = SoundManager.getInstance().getClip("music");

    public final TileManager tileM;
    public final CollisionChecker cChecker;
    public final Player player;
    public UI ui;
    public final Enemy enemy;
    public final KeyHandler keyH = new KeyHandler();
    public final List<Enemy> enemies = new ArrayList<>();
    public final List<NPC>   npcs   = new ArrayList<>();
    public final List<WheatNPC> walkers = new ArrayList<>();

    private boolean dialogueActive = false;
    private NPC     dialogueNpc;
    private int     dialogueIndex = 0;
    private boolean iConsumed      = false;
    private boolean isGameOver = false;

    public GameplayScreen() {
        tileM    = new TileManager(this);
        cChecker = new CollisionChecker(this);
        player   = new Player(this, keyH);
        ui = new UI(this);
        player.addHealthObserver(new entity.UIHealthObserver(this));
        musicLoop.loop(Clip.LOOP_CONTINUOUSLY);
        enemy = EntityFactory.createEnemy(this, tileSize * 44, tileSize * 46);
        enemies.add(enemy);
        enemies.add(EntityFactory.createEnemy(this, tileSize * 46, tileSize * 46));
        enemies.add(EntityFactory.createEnemy(this, tileSize * 45, tileSize * 50));
        enemies.add(EntityFactory.createEnemy(this, tileSize * 47, tileSize * 39));
        enemies.add(EntityFactory.createEnemy(this, tileSize * 45, tileSize * 40));
        enemies.add(EntityFactory.createEnemy(this, tileSize * 56, tileSize * 114));
        enemies.add(EntityFactory.createEnemy(this, tileSize * 57, tileSize * 115));
        enemies.add(EntityFactory.createEnemy(this, tileSize * 58, tileSize * 119));
        enemies.add(EntityFactory.createEnemy(this, tileSize * 128, tileSize * 79));
        enemies.add(EntityFactory.createEnemy(this, tileSize * 125, tileSize * 82));
        enemies.add(EntityFactory.createEnemy(this, tileSize * 129, tileSize * 83));
        BufferedImage npcSprite = Assets.get("/npc/villager.png");
        npcs.add(new NPC(
                this,
                tileSize * 77, tileSize * 77,
                npcSprite,
                new String[]{
                        "Здравствуй, ученик!",
                        "В этом лесу полно орков.",
                        "Они планируют нападение на наш город.",
                        "Ступай и одолей их прежде чем они что-то предпримут."
                }
        ));
        Rectangle field0 = new Rectangle(tileSize*57,  tileSize*78,  tileSize*2, tileSize*1);
        walkers.add(EntityFactory.createWheatWalker(
                this, tileSize*57,  tileSize*78, "/npc/farmer", field0));
        Rectangle field1 = new Rectangle(tileSize * 82, tileSize * 51, tileSize * 1, tileSize * 1);
        walkers.add(EntityFactory.createWheatWalker(
                this, tileSize * 82, tileSize * 51, "/npc/farmer", field1
        ));
        Rectangle field2 = new Rectangle(tileSize * 84, tileSize * 53, tileSize * 1, tileSize * 1);
        walkers.add(EntityFactory.createWheatWalker(
                this, tileSize * 84, tileSize * 53, "/npc/farmer", field2
        ));
        Rectangle field3 = new Rectangle(tileSize * 57, tileSize * 60, tileSize * 1, tileSize * 2);
        walkers.add(EntityFactory.createWheatWalker(
                this, tileSize * 57, tileSize * 60, "/npc/farmer",field3
        ));
        Rectangle field4 = new Rectangle(tileSize * 42, tileSize * 65, tileSize * 2, tileSize * 1);
        walkers.add(EntityFactory.createWheatWalker(
                this, tileSize * 43, tileSize * 66, "/npc/farmer", field4
        ));
        Rectangle field5 = new Rectangle(tileSize * 58, tileSize * 65, tileSize * 3, tileSize * 7);
        walkers.add(EntityFactory.createWheatWalker(
                this, tileSize * 58, tileSize * 65, "/npc/farmer", field5
        ));
        Rectangle field6 = new Rectangle(tileSize * 49, tileSize * 68, tileSize * 3, tileSize * 2);
        walkers.add(EntityFactory.createWheatWalker(
                this, tileSize * 49, tileSize * 68, "/npc/farmer", field6
        ));
        Rectangle field7 = new Rectangle(tileSize * 41, tileSize * 71, tileSize * 1, tileSize * 2);
        walkers.add(EntityFactory.createWheatWalker(
                this, tileSize * 41, tileSize * 71, "/npc/farmer", field7
        ));
        Rectangle field8 = new Rectangle(tileSize * 53, tileSize * 73, tileSize * 1, tileSize * 2);
        walkers.add(EntityFactory.createWheatWalker(
                this, tileSize * 53, tileSize * 74, "/npc/farmer", field8
        ));
        Rectangle field9 = new Rectangle(tileSize * 92, tileSize * 72, tileSize * 2, tileSize * 2);
        walkers.add(EntityFactory.createWheatWalker(
                this, tileSize * 92, tileSize * 72, "/npc/farmer", field9
        ));
        Rectangle field10 = new Rectangle(tileSize * 96, tileSize * 72, tileSize * 2, tileSize * 2);
        walkers.add(EntityFactory.createWheatWalker(
                this, tileSize * 96, tileSize * 72, "/npc/farmer", field10
        ));


    }

    @Override public void update(float dt) {
        player.update();

        if (keyH.iPressed) {
            if (!iConsumed) {
                iConsumed = true; // блокируем до отпускания

                if (dialogueActive) {
                    // в диалоге — продвигаем или закрываем
                    dialogueIndex++;
                    if (dialogueIndex >= dialogueNpc.dialogues.length) {
                        dialogueActive = false;
                        dialogueNpc    = null;
                    }
                } else {
                    // не в диалоге — пытаемся начать
                    for (NPC npc : npcs) {
                        if (npc.isPlayerNear()) {
                            dialogueActive  = true;
                            dialogueNpc     = npc;
                            dialogueIndex   = 0;
                            break;
                        }
                    }
                }
            }
        } else {
            iConsumed = false;
        }

        if (dialogueActive) return;
        ui.update();
        for (Enemy e : enemies) e.update();
        for (NPC   n : npcs)    n.update();
        for (WheatNPC wf : walkers) wf.update();
    }

    @Override public void render(Graphics2D g) {
        tileM.draw(g);
        for (Enemy e : enemies) e.draw(g);
        for (NPC npc : npcs) npc.draw(g);
        for (WheatNPC wf : walkers) wf.draw(g);
        player.draw(g);
        ui.draw(g);
        Font font = new Font("Arial", Font.BOLD, 26);  // имя шрифта, стиль, размер
        g.setFont(font);
        g.setColor(Color.RED);
        g.drawString("Health: " + player.health, screenWidth - 800, 25);
        if (dialogueActive && dialogueNpc != null) {
            String text = dialogueNpc.dialogues[dialogueIndex];

            int boxH  = screenHeight / 3;               // 1/3 экрана
            int boxY  = screenHeight - boxH;
            int pad   = 20;
            int textW = screenWidth - pad * 2;

            g.setColor(new Color(0, 0, 0, 220));        // чуть более непрозрачный
            g.fillRect(0, boxY, screenWidth, boxH);

            g.setColor(Color.WHITE);
            g.drawRect(0, boxY, screenWidth-1, boxH-1);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            FontMetrics fm = g.getFontMetrics();
            List<String> lines = new ArrayList<>();
            String[] words = text.split(" ");
            StringBuilder line = new StringBuilder();
            for (String w : words) {
                String trial = line.length() == 0 ? w : line + " " + w;
                if (fm.stringWidth(trial) > textW) {
                    lines.add(line.toString());
                    line = new StringBuilder(w);
                } else {
                    line = new StringBuilder(trial);
                }
            }
            if (line.length() > 0) lines.add(line.toString());

            int lineH = fm.getHeight();
            int startY = boxY + pad + fm.getAscent();
            for (int i = 0; i < lines.size(); i++) {
                g.drawString(lines.get(i), pad, startY + i * lineH);
            }
        }
    }
}