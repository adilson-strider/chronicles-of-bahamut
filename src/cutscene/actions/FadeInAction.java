package cutscene.actions;

import cutscene.CutsceneAction;
import main.GameManager;

import java.awt.*;

public class FadeInAction extends CutsceneAction {
    private int alphaCounter;
    private boolean isFinished;

    public FadeInAction() {
        this.alphaCounter = 50;
        this.isFinished = false;
    }

    @Override
    public void start() {
        isFinished = false;
    }

    @Override
    public void update() {
        if (alphaCounter > 0) {
            alphaCounter--;
        } else {
            isFinished = true;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, alphaCounter * 5));
        g2.fillRect(0, 0, GameManager.screenWidth, GameManager.screenHeight);
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
