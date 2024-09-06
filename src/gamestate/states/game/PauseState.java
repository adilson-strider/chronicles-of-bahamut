package gamestate.states.game;

import gamestate.IState;
import gamestate.StateMachine;
import main.GameManager;
import main.InputManager;
import ui.UI;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PauseState implements IState {

    @Override
    public void update() {
        if(InputManager.getInstance().isKeyDown(KeyEvent.VK_P)){
            StateMachine.getInstance().change("InGame");
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        GameManager.draw(g2);
        UI.getInstance().drawPauseScreen(g2);
    }

    @Override
    public void onEnter() {
        GameManager.stopMusic();
    }

    @Override
    public void onExit() {
        GameManager.resumeMusic();
    }

    @Override
    public String getName() {
        return "Pause";
    }
}
