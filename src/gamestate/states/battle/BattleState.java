package gamestate.states.battle;

import battle.BattleManager;
import entity.Enemy;
import gamestate.GameStates;
import gamestate.IState;
import main.GameManager;

import java.awt.*;

public class BattleState implements IState {

    public BattleManager bm;

    public BattleState(boolean isBoss, int index){
        bm = new BattleManager(isBoss, index);
    }

    public BattleState(){
        bm = new BattleManager(false, 0);
    }

    @Override
    public void update() {
        bm.update();
    }

    @Override
    public void draw(Graphics2D g2) {
        bm.draw(g2);
    }

    @Override
    public void onEnter() {
        GameManager.getPlayer().isWalking = false;
    }

    @Override
    public void onExit() {

        if(GameManager.gameStates != GameStates.SELECT_ENEMY){
            GameManager.stopMusic();
        }

        GameManager.currentTiledMap.enemies.clear();
    }

    @Override
    public String getName() {
        return "Battle";
    }
}
