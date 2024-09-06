package gamestate.states.battle;

import battle.BattleManager;
import entity.Enemy;
import entity.Player;
import gamestate.GameStates;
import gamestate.IState;
import gamestate.StateMachine;
import main.GameManager;
import main.InputManager;
import ui.UI;

import java.awt.*;
import java.awt.event.KeyEvent;

public class EnemySelectState implements IState {

    @Override
    public void update() {

        StateMachine.getInstance().getmStates().get("Battle").update();
        BattleManager.showBattleOptions = false;

        if(InputManager.getInstance().isKeyDown(KeyEvent.VK_DOWN)){
            UI.getInstance().monsterIndex++;

            if(BattleManager.enemies.size() == 1){
                UI.getInstance().monsterIndex = 0;
            }
            if(BattleManager.enemies.size() == 2){

                if(UI.getInstance().monsterIndex > 1){
                    UI.getInstance().monsterIndex = 0;
                }
            }
            if(BattleManager.enemies.size() == 3){

                if(UI.getInstance().monsterIndex > 2){
                    UI.getInstance().monsterIndex = 0;
                }
            }
        }

        if(InputManager.getInstance().isKeyDown(KeyEvent.VK_UP)){
            UI.getInstance().monsterIndex--;

            if(BattleManager.enemies.size() == 1){
                UI.getInstance().monsterIndex = 0;
            }
            if(BattleManager.enemies.size() == 2){

                if(UI.getInstance().monsterIndex < 0){
                    UI.getInstance().monsterIndex = 1;
                }
            }
            if(BattleManager.enemies.size() == 3){

                if(UI.getInstance().monsterIndex < 0){
                    UI.getInstance().monsterIndex = 2;
                }
            }
        }

        if(InputManager.getInstance().isKeyDown(KeyEvent.VK_ENTER)){
            Enemy targetEnemy = BattleManager.enemies.get(UI.getInstance().monsterIndex);
            Player currentPlayer = GameManager.getPlayerAtIndex(BattleManager.currentPlayerTurn);
            BattleManager.playerActions.add(new Action(Action.ActionType.ATTACK, currentPlayer, targetEnemy));
            StateMachine.getInstance().change("Battle");
            GameManager.getPlayerAtIndex(BattleManager.currentPlayerTurn).attackCooldown = 0;
            BattleManager.isPlayerAttack = true;
        }

    }

    @Override
    public void draw(Graphics2D g2) {

        StateMachine.getInstance().getmStates().get("Battle").draw(g2);

        UI.getInstance().drawEnemySelection(g2,
                BattleManager.enemies.get(UI.getInstance().monsterIndex).battlePos.x,
                BattleManager.enemies.get(UI.getInstance().monsterIndex).battlePos.y);
    }

    @Override
    public void onEnter() {
        GameManager.gameStates = GameStates.SELECT_ENEMY;
        GameManager.resumeMusic();
    }

    @Override
    public void onExit() {
        GameManager.gameStates = GameStates.BATTLE;
        GameManager.resumeMusic();
    }

    @Override
    public String getName() {
        return "EnemySelect";
    }
}
