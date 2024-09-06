package gamestate.states.game;

import gamestate.GameStates;
import gamestate.IState;
import gamestate.StateMachine;
import main.GameManager;
import main.InputManager;
import ui.UI;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOverState implements IState {

    public GameOverState(){

    }

    @Override
    public void update() {

        if (InputManager.getInstance().isKeyDown(KeyEvent.VK_ENTER)){
            GameManager.gameStates = GameStates.OVER_TO_TITLE;
            StateMachine.getInstance().change("Transition");
            GameManager.resetGame();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        UI.getInstance().drawGameOverScreen(g2);
    }

    @Override
    public void onEnter() {
        GameManager.playMusic(13);
    }

    @Override
    public void onExit() {
        GameManager.stopMusic();
    }

    @Override
    public String getName() {
        return "GameOver";
    }
}
