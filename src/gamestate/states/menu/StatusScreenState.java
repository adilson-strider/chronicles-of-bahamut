package gamestate.states.menu;

import entity.Entity;
import gamestate.IState;
import gamestate.StateMachine;
import main.GameManager;
import main.InputManager;
import object.Shield;
import object.Weapon;
import ui.UI;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class StatusScreenState implements IState {

    @Override
    public void update() {

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

        if(InputManager.getInstance().isKeyDown(KeyEvent.VK_ESCAPE)){
            StateMachine.getInstance().change("PlayerMenu");
        }

        if(InputManager.getInstance().isKeyDown(KeyEvent.VK_ENTER)){
            equipItem(UI.getInstance().itemIndex);
        }


    }

    public void equipItem(int itemIndex) {

        if (itemIndex < GameManager.getPlayer().inventory.size() &&
                GameManager.getPlayer().inventory.get(itemIndex) != null) {

            Entity selectedItem = GameManager.getPlayer().inventory.get(itemIndex);

            if (selectedItem instanceof Weapon) {
                unequipWeapon(GameManager.getPlayerAtIndex(PlayerSelectMenuState.playerMenuIndex).currentWeapon);
                GameManager.getPlayerAtIndex(PlayerSelectMenuState.playerMenuIndex).currentWeapon = (Weapon) selectedItem;
                GameManager.playSFX(17);
                GameManager.getPlayerAtIndex(PlayerSelectMenuState.playerMenuIndex).atk = GameManager.getPlayerAtIndex(PlayerSelectMenuState.playerMenuIndex).getAttack();
                selectedItem.amount--;
                if (selectedItem.getAmount() <= 0) {
                    GameManager.getPlayer().inventory.remove(itemIndex);
                }

            } else if (selectedItem instanceof Shield) {
                unequipShield(GameManager.getPlayerAtIndex(PlayerSelectMenuState.playerMenuIndex).currentShield);
                GameManager.getPlayerAtIndex(PlayerSelectMenuState.playerMenuIndex).currentShield = (Shield) selectedItem;
                GameManager.playSFX(17);
                GameManager.getPlayerAtIndex(PlayerSelectMenuState.playerMenuIndex).def = GameManager.getPlayerAtIndex(PlayerSelectMenuState.playerMenuIndex).getDefense();
                selectedItem.amount--;
                if (selectedItem.getAmount() <= 0) {
                    GameManager.getPlayer().inventory.remove(itemIndex);
                }
            }
        }
    }

    public void unequipShield(Entity currentShield) {
        if (currentShield != null) {
            GameManager.getPlayerAtIndex(PlayerSelectMenuState.playerMenuIndex).currentShield = null;

            for (int i = 0; i < GameManager.getPlayer().inventory.size(); i++) {
                if (GameManager.getPlayer().inventory.get(i) != null && Objects.equals(currentShield.getName(),
                        GameManager.getPlayer().inventory.get(i).getName())) {
                    GameManager.getPlayer().inventory.get(i).amount++;
                    return;
                }
            }

            currentShield.setAmount(1);
            GameManager.getPlayer().inventory.readd(currentShield);
        }
    }

    public void unequipWeapon(Entity currentWeapon) {
        if (currentWeapon != null) {
            GameManager.getPlayerAtIndex(PlayerSelectMenuState.playerMenuIndex).currentWeapon = null;

            for (int i = 0; i < GameManager.getPlayer().inventory.size(); i++) {
                if (GameManager.getPlayer().inventory.get(i) != null && Objects.equals(currentWeapon.getName(),
                        GameManager.getPlayer().inventory.get(i).getName())) {
                    GameManager.getPlayer().inventory.get(i).amount++;
                    return;
                }
            }
            // Se o item não está no inventário, re-add ele com quantidade 1
            currentWeapon.setAmount(1);
            GameManager.getPlayer().inventory.readd(currentWeapon);
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        GameManager.draw(g2);
        UI.getInstance().drawHeaderPanel(g2, "Equip/Stats");
        UI.getInstance().drawStatsScreen(g2);
        UI.getInstance().drawInventoryScreen(g2, GameManager.getPlayer(), true);
    }

    @Override
    public void onEnter() {
        UI.getInstance().isStatusItem = true;

        InventoryState.isEquipItem = true;
    }

    @Override
    public void onExit() {
        UI.getInstance().isStatusItem = false;

        InventoryState.isEquipItem = false;
    }

    @Override
    public String getName() {
        return "StatusScreen";
    }
}
