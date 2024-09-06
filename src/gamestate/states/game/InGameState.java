package gamestate.states.game;

import gamestate.GameStates;
import gamestate.IState;
import gamestate.StateMachine;
import main.AssetSetter;
import main.GameManager;
import main.InputManager;
import tiledmap.TiledMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class InGameState implements IState {

    private boolean isInSubLevel = false;
    private boolean isStatsScreen = false;
    private boolean isPauseScreen = false;

    @Override
    public void update() {
        GameManager.getPlayer().update();
        handleInput();
    }

    private void handleInput() {
        InputManager inputManager = InputManager.getInstance();

        if (inputManager.isKeyDown(KeyEvent.VK_I)) {
            isStatsScreen = true;
            StateMachine.getInstance().change("InGameMenu");
        }

        if (inputManager.isKeyDown(KeyEvent.VK_P)) {
            isPauseScreen = true;
            StateMachine.getInstance().change("Pause");
        }

        // Teste
        /*if (inputManager.isKeyDown(KeyEvent.VK_A)) {
            AssetSetter.addHelena(GameManager.getPlayers());
            AssetSetter.addZetsu(GameManager.getPlayers());
        }*/
    }

    @Override
    public void draw(Graphics2D g2) {
        GameManager.draw(g2);
    }

    private void configureSoundSystem() {
        String mapName = GameManager.currentTiledMap.getMapName();

        System.out.println(mapName);

        if ("overworld".equals(mapName)) {
            configureOverworldSoundSystem();
        } else if (isVillageOrDungeon(mapName)) {
            configureVillageSoundSystem(mapName);
        }
    }

    private void configureOverworldSoundSystem() {
        isInSubLevel = false;
        setMusicLevel(5);
    }

    private boolean isVillageOrDungeon(String mapName) {
        return mapName.startsWith("briarwood") || mapName.startsWith("dungeon") ||
                mapName.equals("valenor") || mapName.equals("valenor-as") ||
                mapName.equals("church") || mapName.equals("village1");
    }

    private void configureVillageSoundSystem(String mapName) {
        switch (mapName) {
            case "briarwood", "village1":
                setSubLevelMusic(10);
                break;
            case "valenor":
                setSubLevelMusic(19);
                break;
            case "dungeon2":
                setSubLevelMusic(22);
                break;
            case "dungeon1":
                setSubLevelMusic(23);
                break;
            case "armor-shop":
            case "dungeon3":
            case "dungeon4":
                resumeMusicForSubLevel();
                break;
            case "briarwood-church":
                setMusicLevel(9);
                break;
        }
    }

    private void setMusicLevel(int index) {
        if (!isInSubLevel) {
            GameManager.stopMusic();
            GameManager.playMusic(index);
        }
    }

    private void setSubLevelMusic(int index) {
        if (!isInSubLevel) {
            GameManager.stopMusic();
            GameManager.playMusic(index);
        } else {
            GameManager.resumeMusic();
        }
    }

    private void resumeMusicForSubLevel() {
        GameManager.resumeMusic();
        isInSubLevel = true;
    }

    private void toggleMusicControls() {
        if (isInSubLevel) {
            GameManager.resumeMusic();
        } else {
            GameManager.stopMusic();
        }
    }

    @Override
    public void onEnter() {
        StateMachine stateMachine = StateMachine.getInstance();
        stateMachine.remove("victory");
        stateMachine.remove("battle");

        GameManager.gameStates = GameStates.IN_GAME;

        if (GameManager.isDialogue) {
            GameManager.resumeMusic();
        }

        if (!isStatsScreen && !isPauseScreen && !GameManager.isDialogue) {
            toggleMusicControls();
            configureSoundSystem();
        }

        resetFlags();
        clearNotifications();
    }

    private void resetFlags() {
        isStatsScreen = false;
        isPauseScreen = false;
        GameManager.isDialogue = false;
    }

    private void clearNotifications() {
        GameManager.notifications = null;
    }

    @Override
    public void onExit() {
        GameManager.resumeMusic();
    }

    @Override
    public String getName() {
        return "InGame";
    }
}
