package gamestate.states.game;

import battle.BattleManager;
import gamestate.GameStates;
import gamestate.IState;
import gamestate.StateMachine;
import main.GameManager;
import ui.UI;
import tiledmap.TiledMap;

import java.awt.*;
import java.util.Objects;

public class TransitionState implements IState {

    GameManager gp;
    int alphaCounter = 0;

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g2) {

        switch (GameManager.gameStates) {
            case TITLE:

                if(isFadeOutEffect(g2, "")){
                    //StateMachine.getInstance().change("InGame");
                }

                UI.getInstance().drawTitleScreen(g2);
                UI.getInstance().drawTitleTransition(g2);


                break;
            case TELEPORT:
                if(isFadeOutEffect(g2, "")){
                    teleportPlayer();
                }
                break;
            case BATTLE:
                if(isFadeOutEffect(g2, "")){
                    StateMachine.getInstance().change("Battle");
                    GameManager.stopMusic();

                    if(!BattleManager.isBoss){
                        GameManager.playMusic(8);
                    }
                    else{
                        GameManager.playMusic(24);
                    }

                }
                break;
            case VICTORY:
                if(isFadeOutEffect(g2, "victory")){
                    StateMachine.getInstance().change("InGame");
                }
                break;
            case IN_GAME:
                if(isFadeOutEffect(g2, "InGame")){
                    StateMachine.getInstance().change("InGame");
                }
                break;
            case GAME_OVER:
                if(isFadeOutEffect(g2, "Battle")){
                    StateMachine.getInstance().change("GameOver");
                }
                break;
            case OVER_TO_TITLE:
                if(isFadeOutEffect(g2, "GameOver")){
                    StateMachine.getInstance().change("Title");
                }
                break;
            case RUN:
                if(isFadeOutEffect(g2, "Battle")){
                    StateMachine.getInstance().change("InGame");
                }

                GameManager.currentTiledMap.enemyFound =  null;
        }
    }


    @Override
    public void onEnter() {
        if(GameManager.gameStates == GameStates.BATTLE){
            GameManager.playSFX(18);
        }
    }

    @Override
    public void onExit() {

    }

    @Override
    public String getName() {
        return "Transition";
    }

    public boolean isFadeOutEffect(Graphics2D g2, String state){
        alphaCounter++;

        if(Objects.equals(state, "")){
            GameManager.draw(g2);
        }
        else{
            StateMachine.getInstance().getmStates().get(state).draw(g2);
        }

        g2.setColor(new Color(0, 0, 1, alphaCounter * 5));
        g2.fillRect(0, 0, GameManager.screenWidth, GameManager.screenHeight);

        if (alphaCounter == 50) {
            alphaCounter = 0;
            return true;
        }

        return false;
    }

    public void teleportPlayer(){
        GameManager.currentTiledMap = GameManager.maps.get(TiledMap.mapName);
        StateMachine.getInstance().change("InGame");

        GameManager.currentMap = GameManager.currentTiledMap.mapNum;
        GameManager.getPlayer().setPosition(TiledMap.playerPos);
    }
}
