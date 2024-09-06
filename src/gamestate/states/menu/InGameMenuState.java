package gamestate.states.menu;

import gamestate.IState;
import gamestate.StateMachine;
import main.GameManager;
import main.InputManager;
import ui.UI;

import java.awt.*;
import java.awt.event.KeyEvent;

public class InGameMenuState implements IState {

    @Override
    public void update() {
        if (UI.getInstance().subState == 0) { // Somente processar eventos na tela principal
            // Lógica para navegar pelas opções na tela principal
            if (InputManager.getInstance().isKeyDown(KeyEvent.VK_UP)) {
                UI.getInstance().commandNumber--;
                GameManager.playSFX(14);

                if (UI.getInstance().commandNumber < 0) {
                    UI.getInstance().commandNumber = 3;
                }
            }

            if (InputManager.getInstance().isKeyDown(KeyEvent.VK_DOWN)) {
                UI.getInstance().commandNumber++;
                GameManager.playSFX(14);

                if (UI.getInstance().commandNumber > 3) {
                    UI.getInstance().commandNumber = 0;
                }
            }

            // Verificar se o usuário pressionou Enter para mudar para outro estado
            if (InputManager.getInstance().isKeyDown(KeyEvent.VK_ENTER)) {
                if (UI.getInstance().commandNumber == 0){
                    StateMachine.getInstance().change("Inventory");
                }
                else if (UI.getInstance().commandNumber == 1){
                    // Magic
                }
                else if (UI.getInstance().commandNumber == 2){
                    // Status
                    StateMachine.getInstance().change("PlayerMenu");
                }
                else if (UI.getInstance().commandNumber == 3){

                    if(GameManager.canSave || GameManager.currentMap == 0){
                        GameManager.gameData.save();
                        StateMachine.getInstance().change("Save");
                    }
                }

            }
            else if(InputManager.getInstance().isKeyDown(KeyEvent.VK_ESCAPE)){
                StateMachine.getInstance().change("InGame");
                UI.getInstance().commandNumber = 0;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        StateMachine.getInstance().getmStates().get("InGame").draw(g2);

        UI.getInstance().drawInGameMenu(g2);
    }

    @Override
    public void onEnter() {
    }

    @Override
    public void onExit() {

    }

    @Override
    public String getName() {
        return "InGameMenu";
    }
}
