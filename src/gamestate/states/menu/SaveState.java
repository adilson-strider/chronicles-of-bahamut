package gamestate.states.menu;

import gamestate.IState;
import gamestate.StateMachine;
import main.GameManager;
import main.InputManager;
import ui.UI;

import java.awt.*;
import java.awt.event.KeyEvent;

public class SaveState implements IState {

    @Override
    public void update() {

        UI.getInstance().currentDialogue = "Jogo salvo";
        UI.getInstance().charIndex++;
        UI.getInstance().combinedText = "";

        if(InputManager.getInstance().isKeyDown(KeyEvent.VK_SPACE)){
            StateMachine.getInstance().change("InGame");
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        GameManager.draw(g2);
        UI.getInstance().drawDialogueSaveScreen(g2);
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onExit() {
        UI.getInstance().charIndex = 0;
    }

    @Override
    public String getName() {
        return "Save";
    }
}
