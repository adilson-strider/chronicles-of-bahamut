package gamestate.states.game;

import gamestate.IState;
import gamestate.StateMachine;
import main.GameManager;
import main.InputManager;
import tools.DebugMode;
import ui.UI;

import java.awt.*;
import java.awt.event.KeyEvent;

public class ShopState implements IState {

    boolean isBuying = false;
    boolean isSelling = false;

    public static boolean isEmptyInventory = false;
    public static boolean exitMenu = false;
    public static boolean isFewMoney = false;
    public static boolean isFullInventory = false;

    public ShopState(){

    }

    @Override
    public void update() {

        if (UI.getInstance().subState == 0) { // Somente processar eventos na tela principal
            // Lógica para navegar pelas opções na tela principal
            if (InputManager.getInstance().isKeyDown(KeyEvent.VK_UP)) {
                UI.getInstance().commandNumber--;
                GameManager.playSFX(14);

                if (UI.getInstance().commandNumber < 0) {
                    UI.getInstance().commandNumber = 2;
                }
            }

            if (InputManager.getInstance().isKeyDown(KeyEvent.VK_DOWN)) {
                UI.getInstance().commandNumber++;
                GameManager.playSFX(14);

                if (UI.getInstance().commandNumber > 2) {
                    UI.getInstance().commandNumber = 0;
                }
            }

            // Verificar se o usuário pressionou Enter para mudar para outro estado
            if (InputManager.getInstance().isKeyDown(KeyEvent.VK_ENTER)) {
                if (UI.getInstance().commandNumber == 0) {
                    // Mudar para o estado relacionado à primeira opção
                    UI.getInstance().subState = 1; // Mudar para o estado de compra de itens
                } else if (UI.getInstance().commandNumber == 1) {
                    // Mudar para o estado relacionado à segunda opção
                    UI.getInstance().subState = 2; // Mudar para outro estado (se necessário)
                } else if (UI.getInstance().commandNumber == 2) {

                    // Mudar para o estado relacionado à terceira opção
                    UI.getInstance().subState = 0; // Mudar para outro estado (se necessário)
                    UI.getInstance().commandNumber = 0;
                    exitMenu = true;
                    StateMachine.getInstance().change("Dialogue");
                }
            }

        } // BUYING
        else if (UI.getInstance().subState == 1) {

            // Lógica para o estado de compra de itens
            // Verificar eventos de teclado apenas se estiver no estado de compra de itens
            // Lógica para navegar pelos itens e efetuar a compra

            if (InputManager.getInstance().isKeyDown(KeyEvent.VK_ENTER)) {
                isBuying = true;
            } else if (InputManager.getInstance().isKeyDown(KeyEvent.VK_ESCAPE)) {
                UI.getInstance().subState = 0;
                resetScrollControls();
            }

            if (InputManager.getInstance().isKeyHeldWithDelay(KeyEvent.VK_DOWN)) {
                if (UI.getInstance().npcSlotRow < 3
                        && UI.getInstance().npcSlotRow + UI.getInstance().npcScrollPosition < UI.getInstance().totalItems - 1) {
                    UI.getInstance().npcSlotRow++;
                    GameManager.playSFX(14);
                } else if (UI.getInstance().npcSlotRow == 3
                        && UI.getInstance().npcScrollPosition < UI.getInstance().totalItems - UI.getInstance().visibleItems) {

                    if(UI.getInstance().entity.inventory.countItems() >= 4){
                        UI.getInstance().npcScrollDown();
                        GameManager.playSFX(14);
                    }
                }

                if(UI.getInstance().npcItemIndex < GameManager.getPlayer().inventory.size() - 1){
                    UI.getInstance().npcItemIndex++;
                }
            }

            // Tecla UP
            if (InputManager.getInstance().isKeyHeldWithDelay(KeyEvent.VK_UP)) {
                if (UI.getInstance().npcSlotRow > 0) {
                    UI.getInstance().npcSlotRow--;
                    GameManager.playSFX(14);
                } else if (UI.getInstance().npcSlotRow == 0 && UI.getInstance().npcScrollPosition > 0) {
                    UI.getInstance().npcScrollUp();
                    GameManager.playSFX(14);
                }

                if(UI.getInstance().npcItemIndex > 0){
                    UI.getInstance().npcItemIndex--;
                }
            }

            int itemIndex = UI.getInstance().getItemIndexFromSlot(UI.getInstance().npcItemIndex);

            // BUYING
            if (isBuying) {

                if(UI.getInstance().entity.inventory.get(itemIndex) != null){
                    if (UI.getInstance().entity.inventory.get(itemIndex).getPrice() > GameManager.getPlayer().gold) {
                        UI.getInstance().subState = 0;
                        StateMachine.getInstance().change("Dialogue");
                        isFewMoney = true;

                    } else {
                        if (GameManager.getPlayer().canObtainItem(UI.getInstance().entity.inventory.get(itemIndex))){
                            GameManager.getPlayer().gold -= UI.getInstance().entity.inventory.get(itemIndex).getPrice();
                        }
                        else{
                            UI.getInstance().subState = 0;
                            StateMachine.getInstance().change("Dialogue");
                            isFullInventory = true;
                        }
                    }
                }


                isBuying = false;
            }

            // SELLING
        }  else if (UI.getInstance().subState == 2) {

            UI.getInstance().isSellTrade = true;

            if (InputManager.getInstance().isKeyDown(KeyEvent.VK_ENTER)) {
                isSelling = true;
            } else if (InputManager.getInstance().isKeyDown(KeyEvent.VK_ESCAPE)) {
                UI.getInstance().subState = 0;
                resetScrollControls();
            }

            // Lógica para navegar pelos slots de itens
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

            if (isSelling) {

                UI.getInstance().isBuyTrade = false;

                if(GameManager.getPlayer().inventory != null){
                    if(GameManager.getPlayer().inventory.get(UI.getInstance().itemIndex) != null){

                        int price = GameManager.getPlayer().inventory.get(UI.getInstance().itemIndex).getPrice() / 2;

                        if(GameManager.getPlayer().inventory.get(UI.getInstance().itemIndex).amount > 1){
                            GameManager.getPlayer().inventory.get(UI.getInstance().itemIndex).amount--;
                        }
                        else{
                            GameManager.getPlayer().inventory.remove(UI.getInstance().itemIndex);
                            GameManager.getPlayer().gold += price;
                            UI.getInstance().totalItems--;
                        }
                    }
                }
                else{
                    StateMachine.getInstance().change("Dialogue");
                    isEmptyInventory = true;
                }

                isSelling = false;
            }


        }
    }

    @Override
    public void draw(Graphics2D g2) {

        GameManager.draw(g2);
        UI.getInstance().drawTradeScreen(g2);

        // DEBUG
        DebugMode.getInstance().clearDebugValues(); // Limpa as variáveis de debug antes de adicionar novas
        DebugMode.getInstance().addDebugValue("Is Buying: ", String.valueOf(isBuying));
        DebugMode.getInstance().addDebugValue("Is Selling: ", String.valueOf(isSelling));

        DebugMode.getInstance().showDebugMode(g2, 0);
    }

    @Override
    public void onEnter() {
        UI.getInstance().npcItemIndex = 0;
        UI.getInstance().npcSlotRow = 0;
        UI.getInstance().npcScrollPosition = 0;

        UI.getInstance().itemIndex = 0;
        UI.getInstance().playerSlotRow = 0;
        UI.getInstance().scrollPosition = 0;


    }

    @Override
    public void onExit() {
        UI.getInstance().isBuyTrade = false;
        UI.getInstance().isSellTrade = false;

        resetScrollControls();
    }

    @Override
    public String getName() {
        return "Shop";
    }

    public void resetScrollControls(){
        UI.getInstance().npcItemIndex = 0;
        UI.getInstance().npcSlotRow = 0;
        UI.getInstance().npcScrollPosition = 0;

        UI.getInstance().itemIndex = 0;
        UI.getInstance().playerSlotRow = 0;
        UI.getInstance().scrollPosition = 0;
    }
}
