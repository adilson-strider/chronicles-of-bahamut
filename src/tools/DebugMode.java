package tools;

import main.GameManager;
import main.InputManager;
import tiledmap.BattleLayer;
import tiledmap.GameEvent;
import tiledmap.Portal;
import tiledmap.TiledMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DebugMode {

    private static DebugMode instance;
    public boolean debug;

    // Mapa para armazenar pares chave-valor de variáveis de debug
    private Map<String, String> debugValues;

    private DebugMode(){
        debug = false;
        debugValues = new HashMap<>();
    }

    public static DebugMode getInstance() {
        if (instance == null) {
            instance = new DebugMode();
        }
        return instance;
    }

    public void update(){
        if(InputManager.getInstance().isKeyDown(KeyEvent.VK_D) && !debug) {
            debug = true;
        } else if(InputManager.getInstance().isKeyDown(KeyEvent.VK_D) && debug){
            debug = false;
        }
    }

    // Método para adicionar variáveis de debug
    public void addDebugValue(String key, String value) {
        debugValues.put(key, value);
    }

    // Método para limpar as variáveis de debug
    public void clearDebugValues() {
        debugValues.clear();
    }

    public void showDebugMode(Graphics2D g2, long passed){

            int textSpacing = GameManager.tileSize / 2;
            int x = GameManager.getPlayer().screenX + (GameManager.tileSize * 2);
            int y = GameManager.getPlayer().screenY + (GameManager.tileSize * -5);

            /*// Exibir retângulos de colisão
            for (Rectangle rectangle : GameManager.currentTiledMap.collisionAreasForUpdate) {
                g2.setColor(Color.RED);
                g2.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            }

            // Exibir portais
            for (Portal portal : GameManager.currentTiledMap.portals) {
                int portalScreenX = portal.getArea().x - GameManager.getPlayer().position.x + GameManager.getPlayer().screenX;
                int portalScreenY = portal.getArea().y - GameManager.getPlayer().position.y + GameManager.getPlayer().screenY;
                g2.setColor(Color.GRAY);
                //portal.draw(g2, portalScreenX, portalScreenY);
            }

            // Exibir áreas de batalha
            for (BattleLayer areas : GameManager.currentTiledMap.battleAreas) {
                int portalScreenX = areas.getArea().x - GameManager.getPlayer().position.x + GameManager.getPlayer().screenX;
                int portalScreenY = areas.getArea().y - GameManager.getPlayer().position.y + GameManager.getPlayer().screenY;
                g2.setColor(Color.GRAY);
                areas.draw(g2, portalScreenX, portalScreenY);
            }

            // Exibir áreas de eventos
            for (GameEvent event : GameManager.currentTiledMap.events) {
                int portalScreenX = event.getArea().x - GameManager.getPlayer().position.x + GameManager.getPlayer().screenX;
                int portalScreenY = event.getArea().y - GameManager.getPlayer().position.y + GameManager.getPlayer().screenY;
                g2.setColor(Color.GRAY);
                event.draw(g2, portalScreenX, portalScreenY);
            }

            // Exibir limites do jogador
            g2.setColor(Color.GREEN);
            g2.drawRect(GameManager.getPlayer().TOP_LIMIT.x, GameManager.getPlayer().TOP_LIMIT.y, GameManager.getPlayer().TOP_LIMIT.width, GameManager.getPlayer().TOP_LIMIT.height);
            g2.drawRect(GameManager.getPlayer().RIGHT_LIMIT.x, GameManager.getPlayer().RIGHT_LIMIT.y, GameManager.getPlayer().RIGHT_LIMIT.width, GameManager.getPlayer().RIGHT_LIMIT.height);
            g2.drawRect(GameManager.getPlayer().LEFT_LIMIT.x, GameManager.getPlayer().LEFT_LIMIT.y, GameManager.getPlayer().LEFT_LIMIT.width, GameManager.getPlayer().LEFT_LIMIT.height);
            g2.drawRect(GameManager.getPlayer().BOTTOM_LIMIT.x, GameManager.getPlayer().BOTTOM_LIMIT.y, GameManager.getPlayer().BOTTOM_LIMIT.width, GameManager.getPlayer().BOTTOM_LIMIT.height);

            // Exibir área sólida do jogador
            g2.setColor(Color.BLUE);
            g2.drawRect(GameManager.getPlayer().solidArea.x + GameManager.getPlayer().screenX, GameManager.getPlayer().solidArea.y + GameManager.getPlayer().screenY, GameManager.getPlayer().solidArea.width, GameManager.getPlayer().solidArea.height);*/

        if (debug){
            // Mostrar variáveis de debug
            drawDebugValues(g2, x, y); // Ajuste a posição conforme necessário
        }
    }

    private void drawDebugValues(Graphics2D g2, int x, int y) {
        int textSpacing = GameManager.tileSize / 2;
        g2.setColor(Color.WHITE);
        g2.drawString("Debug Values:", x, y);
        y += textSpacing;

        for (Map.Entry<String, String> entry : debugValues.entrySet()) {
            g2.drawString(entry.getKey() + ": " + entry.getValue(), x, y);
            y += textSpacing;
        }
    }
}
