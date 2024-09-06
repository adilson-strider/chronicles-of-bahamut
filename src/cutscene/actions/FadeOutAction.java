package cutscene.actions;

import cutscene.CutsceneAction;
import main.GameManager;

import java.awt.*;

public class FadeOutAction extends CutsceneAction {
    private int alphaCounter;
    private int fadeSpeed;
    private boolean fadingIn;

    public FadeOutAction(int fadeSpeed, boolean fadingIn) {
        this.fadeSpeed = fadeSpeed;
        this.fadingIn = fadingIn;
        this.alphaCounter = fadingIn ? 255 : 0;
    }

    @Override
    public void start() {
        alphaCounter = fadingIn ? 255 : 0;
    }

    @Override
    public void update() {
        if (fadingIn) {
            alphaCounter -= fadeSpeed;
            if (alphaCounter <= 0) {
                alphaCounter = 0;
                isFinished = true;
            }
        } else {
            alphaCounter += fadeSpeed;
            if (alphaCounter >= 255) {
                alphaCounter = 255;
                isFinished = true;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, alphaCounter));
        g2.fillRect(0, 0, GameManager.screenWidth, GameManager.screenHeight);
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
