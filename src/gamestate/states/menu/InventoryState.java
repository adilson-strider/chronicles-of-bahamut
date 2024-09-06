package gamestate.states.menu;

import entity.EntityType;
import gamestate.IState;
import gamestate.StateMachine;
import main.GameManager;
import main.InputManager;
import tools.DebugMode;
import ui.UI;

import java.awt.*;
import java.awt.event.KeyEvent;

public class InventoryState implements IState {

    public static boolean isUseItem = false;
    public static boolean isEquipItem = false;

    public InventoryState(){
        GameManager.getPlayer().inventory.sortItems();
    }

    public static void updateInventory(){

        if (InputManager.getInstance().isKeyHeldWithDelay(KeyEvent.VK_DOWN)) {
            if (UI.getInstance().playerSlotRow < 3
                    && UI.getInstance().playerSlotRow + UI.getInstance().scrollPosition < UI.getInstance().totalItems - 1) {
                UI.getInstance().playerSlotRow++;
                UI.getInstance().itemIndex++;
                GameManager.playSFX(14);
            } else if (UI.getInstance().playerSlotRow == 3
                    && UI.getInstance().scrollPosition < UI.getInstance().totalItems - UI.getInstance().visibleItems) {

                if(GameManager.getPlayer().inventory.countItems() >= 4){
                    UI.getInstance().itemIndex++;
                    UI.getInstance().scrollDown();
                }

                GameManager.playSFX(14);
            }


            if(UI.getInstance().itemIndex < GameManager.getPlayer().inventory.size() - 1){
                //UI.getInstance().itemIndex++;
            }
        }

        // Tecla UP
        if (InputManager.getInstance().isKeyHeldWithDelay(KeyEvent.VK_UP)) {
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

            StateMachine.getInstance().change("PlayerMenu");
            isUseItem = true;
        }

        if(InputManager.getInstance().isKeyDown(KeyEvent.VK_ESCAPE)){
            StateMachine.getInstance().change("InGameMenu");
            UI.getInstance().itemIndex = 0;
        }
    }

    @Override
    public void update() {
        updateInventory();
    }

    @Override
    public void draw(Graphics2D g2) {
        GameManager.draw(g2);
        UI.getInstance().drawHeaderPanel(g2, "Items");
        UI.getInstance().drawPlayerPortrait(g2);
        UI.getInstance().drawInventoryScreen(g2, GameManager.getPlayer(), true);
    }

    @Override
    public void onEnter() {
        UI.getInstance().isInventoryScreen = true;
        UI.getInstance().itemIndex = 0;
        UI.getInstance().playerSlotRow = 0;
        UI.getInstance().scrollPosition = 0;
    }

    @Override
    public void onExit() {
        UI.getInstance().isInventoryScreen = false;
        //UI.getInstance().itemIndex = 0;
        UI.getInstance().scrollPosition = 0;
    }

    @Override
    public String getName() {
        return "Inventory";
    }
}
