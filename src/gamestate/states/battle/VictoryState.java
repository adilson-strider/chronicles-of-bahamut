package gamestate.states.battle;

import battle.BattleManager;
import entity.Enemy;
import entity.Player;
import gamestate.IState;
import gamestate.StateMachine;
import main.GameManager;
import tiledmap.TiledMap;
import tools.GameNotification;

import java.awt.*;

public class VictoryState implements IState {

    int coolDownVictory = 0;

    public VictoryState(){

        for (Enemy e : BattleManager.enemies){
            BattleManager.goldAmount += e.gold;
            BattleManager.expAmount += e.exp;

            GameManager.getPlayer().gold += BattleManager.goldAmount;
        }

        for(Player p : GameManager.players){
            p.exp += BattleManager.expAmount;
        }

        GameManager.notifications =
               new GameNotification(
                       STR."Vitória! Recebeu \{BattleManager.goldAmount} de ouro e \{BattleManager.expAmount} de exp.",
                       250);


    }
    @Override
    public void update() {

        GameManager.notifications.update();

        coolDownVictory++;

        if(coolDownVictory >= 540){
            coolDownVictory = 0;
            StateMachine.getInstance().change("Transition");
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        StateMachine.getInstance().getmStates().get("Battle").draw(g2);

        GameManager.notifications.drawNotification(g2, 0, 0);
    }

    @Override
    public void onEnter() {

        GameManager.playMusic(4);
    }

    @Override
    public void onExit() {
        resetBattleData();
    }

    @Override
    public String getName() {
        return "Victory";
    }

    public void resetBattleData(){

        GameManager.currentTiledMap.battleCoolDown = 200;
        GameManager.currentTiledMap.enemyFound =  null;
        GameManager.stopMusic();

        switch (TiledMap.mapName){
            case "overworld":
                GameManager.playMusic(5);
                break;
            case "dungeon4":
                GameManager.playMusic(22);
                break;
        }

        BattleManager.expAmount = 0;
        BattleManager.goldAmount = 0;
        BattleManager.isPlayerAttack = false;

        BattleManager.showBattleOptions = false;
        BattleManager.isEnemyAttack = false;
        BattleManager.playerTurn = false; // Começa com o turno do jogador
        BattleManager.isBattleOver = false;
        BattleManager.coolDownEndBattle = 100;

        for (Player player : GameManager.getPlayers()) {
            player.playerAttackAnimation.reset();
            player.isAttackAnim = false;
        }
    }
}
