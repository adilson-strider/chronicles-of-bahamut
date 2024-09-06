package cutscene;

import cutscene.actions.ShowSceneAction;
import entity.npcs.NPC;
import main.GameManager;
import tiledmap.SpriteLayer;
import tiledmap.TiledMap;
import ui.UI;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class CutsceneManager {
    private Queue<CutsceneAction> events = new LinkedList<>();
    private CutsceneAction currentEvent;
    private boolean cutsceneActive;
    private final CutsceneCamera cutsceneCamera;
    private int currentIndex = 0;

    // Novo campo para registrar cutscenes assistidas
    ArrayList<String> watchedCutscenes = new ArrayList<>();

    private static volatile CutsceneManager instance;

    public static CutsceneManager getInstance() {

        CutsceneManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized(CutsceneManager.class) {
            if (instance == null) {
                instance = new CutsceneManager();
            }
            return instance;
        }
    }

    public CutsceneManager() {
        events = new LinkedList<>();
        cutsceneCamera = new CutsceneCamera(GameManager.getPlayer().position.x, GameManager.getPlayer().position.y);
        cutsceneActive = false;
    }

    public void addEvent(CutsceneAction event) {
        events.add(event);
    }

    public void eraseList(){
        events.remove();
    }

    public void update() {
        if (currentEvent == null || currentEvent.isFinished()) {
            if (currentEvent != null) {
                System.out.println(STR."Evento terminado: \{currentEvent.getClass().getSimpleName()}");
            }

            currentEvent = events.poll();


            if (currentEvent != null) {
                System.out.println(STR."Iniciando evento: \{currentEvent.getClass().getSimpleName()}");
                currentIndex++;
                currentEvent.start();
            } else {
                cutsceneActive = false; // Cutscene terminou
            }
        }
        if (currentEvent != null) {
            currentEvent.update();
        }

        cutsceneCamera.update();
    }

    public void draw(Graphics2D g2) {
        if (currentEvent != null) {
            currentEvent.draw(g2);
        }
    }

    public void drawCutscene(Graphics2D g2, TiledMap map, boolean isDrawPlayer) {
        CutsceneCamera cutsceneCamera = getCutsceneCamera();

        // Desenhar camadas do mapa usando a câmera da cutscene
        for (int i = 0; i < 3; i++) {
            drawLayer(g2, map, i, cutsceneCamera.getX(), cutsceneCamera.getY());
        }

        // cutsceneCamera.centerOnPlayer(GameManager.getPlayer(), GameManager.camera);

        if(isDrawPlayer){

            // Desenhar jogador e NPCs
            GameManager.getPlayer().draw(g2, cutsceneCamera);

            for (NPC npc : GameManager.npcs.get(GameManager.currentMap)) {
                if (npc != null) {
                    npc.draw(g2, cutsceneCamera);
                }
            }
        }
    }

    private void drawLayer(Graphics2D g2, TiledMap map, int layerIndex, int cameraX, int cameraY) {
        if (layerIndex >= 0 && layerIndex < map.spriteLayers.size()) {
            SpriteLayer spriteLayer = map.spriteLayers.get(layerIndex);
            int[] layerSprites = spriteLayer.getSpritesArray();

            for (int y = 0; y < map.mapHeightInTiles; y++) {
                for (int x = 0; x < map.mapWidthInTiles; x++) {
                    int currentSpriteId = layerSprites[x + y * map.mapWidthInTiles];

                    int worldX = x * GameManager.tileSize;
                    int worldY = y * GameManager.tileSize;
                    int screenX = worldX  + GameManager.screenWidth / 2 - cameraX;
                    int screenY = worldY  + GameManager.screenHeight / 2 - cameraY;

                    // Verifica se o sprite está dentro da área visível da tela
                    if (isVisibleOnScreen(screenX, screenY)) {
                        if (currentSpriteId != -1) {
                            if (map.tiles[currentSpriteId] != null) {
                                g2.drawImage(map.tiles[currentSpriteId].getSprite().getImage(), screenX, screenY, 40, 40, null);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isVisibleOnScreen(int screenX, int screenY) {
        return screenX + 32 >= 0 && screenX < GameManager.screenWidth && screenY + 32 >= 0 && screenY < GameManager.screenHeight;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public CutsceneCamera getCutsceneCamera() {
        return cutsceneCamera;
    }

    public CutsceneAction getCurrentEvent() {
        return currentEvent;
    }

    public boolean isCutsceneActive() {
        return cutsceneActive;
    }

    public void startCutscene() {
        cutsceneActive = true;
        currentIndex = 0;
    }

    public boolean isCutsceneRunning() {
        return currentEvent != null || !events.isEmpty();
    }

    public void markCutsceneAsWatched(String cutsceneId) {
        if (!watchedCutscenes.contains(cutsceneId)) {
            watchedCutscenes.add(cutsceneId);
        }
    }

    public ArrayList<String> getWatchedCutscenes() {
        return watchedCutscenes;
    }

    public void setWatchedCutscenes(ArrayList<String> watchedCutscenes){
        this.watchedCutscenes = watchedCutscenes;
    }
}
