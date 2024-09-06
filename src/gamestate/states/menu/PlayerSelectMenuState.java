package gamestate.states.menu;

import battle.BattleManager;
import entity.Enemy;
import entity.Entity;
import entity.Player;
import gamestate.IState;
import gamestate.StateMachine;
import gamestate.states.battle.Action;
import main.GameManager;
import main.InputManager;
import object.Obj_Pickaxe;
import object.Shield;
import object.Weapon;
import object.consumables.OBJ_Health_Potion;
import object.consumables.OBJ_Key;
import tools.FixedSizeList;
import ui.UI;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PlayerSelectMenuState implements IState {

    public static int playerMenuIndex = 0;
    public static boolean isPlayerSelected = false;

    @Override
    public void update() {

        if (InputManager.getInstance().isKeyDown(KeyEvent.VK_UP)) {
            playerMenuIndex--;
            GameManager.playSFX(14);

            if (playerMenuIndex < 0) {
                playerMenuIndex = GameManager.getPlayers().size() - 1;
            }
        }

        if (InputManager.getInstance().isKeyDown(KeyEvent.VK_DOWN)) {
            playerMenuIndex++;
            GameManager.playSFX(14);

            if (playerMenuIndex >= GameManager.getPlayers().size()) {
                playerMenuIndex = 0;
            }
        }

        if(InputManager.getInstance().isKeyDown(KeyEvent.VK_ESCAPE)){
            StateMachine.getInstance().change("InGameMenu");
            playerMenuIndex = 0;
            InventoryState.isUseItem = false;
        }
        if(InputManager.getInstance().isKeyDown(KeyEvent.VK_ENTER)){

            if(UI.getInstance().commandNumber == 0){
                GameManager.getPlayer().useItem(UI.getInstance().itemIndex, UI.getInstance().playerIndex);
            }
            else if(UI.getInstance().commandNumber == 2){
                StateMachine.getInstance().change("StatusScreen");
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {

        if(InventoryState.isUseItem){
            StateMachine.getInstance().getmStates().get("InGame").draw(g2);
            UI.getInstance().drawPlayerPortrait(g2);
            UI.getInstance().drawInventoryScreen(g2, GameManager.getPlayer(), true);
        }
        else{
            StateMachine.getInstance().getmStates().get("InGame").draw(g2);
            StateMachine.getInstance().getmStates().get("InGameMenu").draw(g2);
        }

        int portraitY = UI.getInstance().playerPanel.getFrameY() + 20;
        int portraitHeight = GameManager.getPlayer().portrait.getHeight()/2;


        if (playerMenuIndex == 0){
            g2.drawImage(UI.getInstance().titleCursor,
                    UI.getInstance().playerPanel.getFrameX(),
                    portraitY, 32, 32, null);
        }
        else if(playerMenuIndex == 1){
            g2.drawImage(UI.getInstance().titleCursor,
                    UI.getInstance().playerPanel.getFrameX(),
                    portraitY + portraitHeight, 32, 32, null);
        }
        else if(playerMenuIndex == 2){
            g2.drawImage((Image) UI.getInstance().titleCursor,
                    UI.getInstance().playerPanel.getFrameX(),
                    (int) (portraitY + (portraitHeight * 2.2)), 32, 32, null);
        }
    }



    @Override
    public void onEnter() {
        isPlayerSelected = true;
    }

    @Override
    public void onExit() {
        isPlayerSelected = false;
    }

    @Override
    public String getName() {
        return "PlayerSelect";
    }
}
