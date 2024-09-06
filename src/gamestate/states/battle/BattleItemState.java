package gamestate.states.battle;

import battle.BattleManager;
import gamestate.IState;
import gamestate.StateMachine;
import main.GameManager;
import main.InputManager;
import ui.UI;

import java.awt.*;
import java.awt.event.KeyEvent;

public class BattleItemState implements IState {
    @Override
    public void update() {

        StateMachine.getInstance().getmStates().get("Battle").update();
        BattleManager.showBattleOptions = false;


        if (InputManager.getInstance().isKeyDown(KeyEvent.VK_DOWN)) {
            if (UI.getInstance().playerSlotRow < 3
                    && UI.getInstance().playerSlotRow + UI.getInstance().scrollPosition < UI.getInstance().totalItems - 1) {
                UI.getInstance().playerSlotRow++;
                GameManager.playSFX(14);
            } else if (UI.getInstance().playerSlotRow == 3
                    && UI.getInstance().scrollPosition < UI.getInstance().totalItems - UI.getInstance().visibleItems) {

                UI.getInstance().scrollDown();
                GameManager.playSFX(14);
            }

            if(UI.getInstance().itemIndex < GameManager.getPlayer().inventory.size() - 1){
                UI.getInstance().itemIndex++;
            }
        }

        // Tecla UP
        if (InputManager.getInstance().isKeyDown(KeyEvent.VK_UP)) {
            if (UI.getInstance().playerSlotRow > 0) {
                UI.getInstance().playerSlotRow--;
                GameManager.playSFX(14);
            } else if (UI.getInstance().playerSlotRow == 0 && UI.getInstance().scrollPosition > 0) {
                UI.getInstance().scrollUp();
                GameManager.playSFX(14);
            }

            if(UI.getInstance().itemIndex > 0){
                UI.getInstance().itemIndex--;
            }
        }

        // Verificar se o usuário pressionou Enter para mudar para outro estado
        if (InputManager.getInstance().isKeyDown(KeyEvent.VK_ENTER)) {
            StateMachine.getInstance().change("PlayerSelect");
        }

        if(InputManager.getInstance().isKeyDown(KeyEvent.VK_ESCAPE)){
            StateMachine.getInstance().change("Battle");
            UI.getInstance().itemIndex = 0;
        }
    }

    @Override
    public void draw(Graphics2D g2) {

        StateMachine.getInstance().getmStates().get("Battle").draw(g2);
        UI.getInstance().drawInventoryInBattleScreen(g2, GameManager.getPlayer());
    }

    @Override
    public void onEnter() {
        UI.getInstance().isBattleItem = true;
        BattleManager.showBattleOptions = false;
    }

    @Override
    public void onExit() {
        UI.getInstance().isBattleItem = false;
        BattleManager.showBattleOptions = true;
    }

    @Override
    public String getName() {
        return "BattleItem";
    }
}
