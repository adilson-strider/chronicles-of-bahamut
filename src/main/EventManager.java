package main;

import cutscene_scripts.NotPassCutscene;
import entity.npcs.NPC;
import gamestate.StateMachine;
import gamestate.states.battle.BattleState;
import tiledmap.GameEvent;
import tiledmap.Tile;
import tiledmap.TiledMap;
import tools.ResourceLoader;
import ui.UI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventManager {

    public int previousEventX;
    public int previousEventY;
    private boolean canTouchEvent = true;

    ArrayList<String> eventDone = new ArrayList<>();

    public EventManager() {
    }

    public void checkEvent() {
        int xDistance = Math.abs(GameManager.getPlayer().position.x - previousEventX);
        int yDistance = Math.abs(GameManager.getPlayer().position.y - previousEventY);
        int distance = Math.max(xDistance, yDistance);

        if (distance > GameManager.tileSize) {
            canTouchEvent = true;
        }

        if (canTouchEvent) {
            for (GameEvent event : GameManager.currentTiledMap.events) {
                if (hitEvent(event)) {
                    triggerEvent(event);
                }
            }
        }
    }

    public void draw(Graphics2D g2){

        for (GameEvent event : GameManager.currentTiledMap.events) {
            if (Objects.equals(event.getEventType(), "save")) {
                event.image = ResourceLoader.loadImage("/images/objects/save.png");
                event.draw(g2, GameManager.camera);
            }
        }
    }

    private boolean hitEvent(GameEvent event) {
        boolean hit = false;
        Rectangle eventArea = event.getArea();

        GameManager.getPlayer().solidArea.x = GameManager.getPlayer().position.x + GameManager.getPlayer().solidArea.x;
        GameManager.getPlayer().solidArea.y = GameManager.getPlayer().position.y + GameManager.getPlayer().solidArea.y;

        if (GameManager.getPlayer().solidArea.intersects(eventArea) && !event.isEventDone()) {
            hit = true;
            previousEventX = GameManager.getPlayer().position.x;
            previousEventY = GameManager.getPlayer().position.y;
        }

        GameManager.getPlayer().solidArea.x = GameManager.getPlayer().solidAreaDefaultX;
        GameManager.getPlayer().solidArea.y = GameManager.getPlayer().solidAreaDefaultY;

        return hit;
    }

    private void triggerEvent(GameEvent event) {

        String eventType = event.getEventType();

        for (String eventDone : eventDone){
            if(Objects.equals(eventType, eventDone)){
                return;
            }
        }

        switch (eventType) {
            case "bossCrab":
                bossCrab(event);
                eventDone.add(eventType);
                break;
            case "blackSmith":
                blackSmith(event);
                eventDone.add(eventType);
                break;
            case "notPass":
                if (!GameManager.canPass) {
                    notPassCutscene(event, new Point(event.getX(), event.getY()));
                }
                break;
            case "save":
                GameManager.canSave = true;
                break;
            // Adicione outros tipos de eventos aqui
        }

        //event.setEventDone(true); // Marcar o evento como concluído após sua execução
        //canTouchEvent = false;
    }


    private void bossCrab(GameEvent event) {
        StateMachine.getInstance().change("intro");
        event.setEventDone(true);
        canTouchEvent = false;
    }

    private void blackSmith(GameEvent event) {
        StateMachine.getInstance().change("Blacksmith");
        event.setEventDone(true);
        canTouchEvent = false;
    }

    private void notPassCutscene(GameEvent event, Point position) {
        StateMachine.getInstance().add("NotPass", new NotPassCutscene(position));
        StateMachine.getInstance().change("NotPass");
    }

    public ArrayList<String> getEventDone() {
        return eventDone;
    }

    public void setEventDone(ArrayList<String> eventDone){
        this.eventDone = eventDone;
    }

}

