package gamestate.states.game;

import gamestate.IState;
import gamestate.StateMachine;
import main.GameManager;
import main.InputManager;
import ui.UI;

import java.awt.*;
import java.awt.event.KeyEvent;

public class TitleState implements IState {

    public TitleState(){

    }

    @Override
    public void update() {

        if (InputManager.getInstance().isKeyDown(KeyEvent.VK_DOWN)){
            UI.getInstance().titleIndex++;
            GameManager.playSFX(0);

            if(UI.getInstance().titleIndex > 2){
                UI.getInstance().titleIndex = 0;
            }
        }
        if (InputManager.getInstance().isKeyDown(KeyEvent.VK_UP)){
            UI.getInstance().titleIndex--;
            GameManager.playSFX(0);

            if(UI.getInstance().titleIndex < 0){
                UI.getInstance().titleIndex = 2;
            }
        }

        if (InputManager.getInstance().isKeyDown(KeyEvent.VK_ENTER) && UI.getInstance().titleIndex == 0){
            StateMachine.getInstance().change("Transition");
            GameManager.playSFX(7);

        }

        if (InputManager.getInstance().isKeyDown(KeyEvent.VK_ENTER) && UI.getInstance().titleIndex == 1){
            StateMachine.getInstance().change("Transition");
            GameManager.playSFX(7);
            GameManager.gameData.load();
        }

        if (InputManager.getInstance().isKeyDown(KeyEvent.VK_ENTER) && UI.getInstance().titleIndex == 2){
            GameManager.quitGame();
        }

    }

    @Override
    public void draw(Graphics2D g2) {
        UI.getInstance().drawTitleScreen(g2);
    }

    @Override
    public void onEnter() {
        GameManager.playMusic(6);
    }

    @Override
    public void onExit() {
        GameManager.stopMusic();
    }

    @Override
    public String getName() {
        return "Title"; // Retorna o nome do estado
    }
}
