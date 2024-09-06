package gamestate.states.battle;

import battle.BattleManager;
import gamestate.GameStates;
import gamestate.IState;
import gamestate.StateMachine;
import main.GameManager;
import main.InputManager;
import ui.UI;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PlayerSelectBattleMenuState implements IState {
    @Override
    public void update() {
        StateMachine.getInstance().getmStates().get("Battle").update();
        BattleManager.showBattleOptions = false;

        if(InputManager.getInstance().isKeyDown(KeyEvent.VK_DOWN)){
            UI.getInstance().playerIndex++;

            if(GameManager.players.size() == 1){
                UI.getInstance().playerIndex = 0;
            }
            if(GameManager.players.size() == 2){

                if(UI.getInstance().playerIndex > 1){
                    UI.getInstance().playerIndex = 0;
                }
            }
            if(GameManager.players.size() == 3){

                if(UI.getInstance().playerIndex > 2){
                    UI.getInstance().playerIndex = 0;
                }
            }
        }

        if(InputManager.getInstance().isKeyDown(KeyEvent.VK_UP)){
            UI.getInstance().playerIndex--;

            if(GameManager.players.size() == 1){
                UI.getInstance().playerIndex = 0;
            }
            if(GameManager.players.size() == 2){

                if(UI.getInstance().playerIndex < 0){
                    UI.getInstance().playerIndex = 1;
                }
            }
            if(GameManager.players.size() == 3){

                if(UI.getInstance().playerIndex < 0){
                    UI.getInstance().playerIndex = 2;
                }
            }
        }

        if(InputManager.getInstance().isKeyDown(KeyEvent.VK_ENTER)){

            GameManager.getPlayer().useItem(UI.getInstance().itemIndex, UI.getInstance().playerIndex);

            //BattleManager.showBattleOptions = false;
            BattleManager.playerTurnCoolDown = 100;
            BattleManager.isPlayerAttack = false;


            UI.getInstance().battleIndex = 0;

            System.out.println("teste");

            StateMachine.getInstance().change("Battle");
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        StateMachine.getInstance().getmStates().get("Battle").draw(g2);

        UI.getInstance().drawPlayerSelection(g2,
                GameManager.getPlayers().get(UI.getInstance().playerIndex).battlePos.x,
                GameManager.getPlayers().get(UI.getInstance().playerIndex).battlePos.y);
    }

    @Override
    public void onEnter() {
        GameManager.gameStates = GameStates.SELECT_PLAYER;
        GameManager.resumeMusic();
    }

    @Override
    public void onExit() {
        GameManager.gameStates = GameStates.BATTLE;
        GameManager.resumeMusic();
    }

    @Override
    public String getName() {
        return "";
    }
}
