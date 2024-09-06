package cutscene.actions;

import cutscene.CutsceneAction;

import java.awt.*;

public class EndCutsceneAction extends CutsceneAction {
    private boolean isFinished;

    public EndCutsceneAction() {
        this.isFinished = false;
    }

    @Override
    public void start() {
        isFinished = true;
    }

    @Override
    public void update() {
        // Nada a atualizar
    }

    @Override
    public void draw(Graphics2D g2) {
        // Nada a desenhar
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
