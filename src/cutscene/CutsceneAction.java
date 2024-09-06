package cutscene;

import java.awt.*;

public abstract class CutsceneAction {
    private boolean completed;
    public boolean isFinished;

    public CutsceneAction() {
        this.completed = false;
    }

    public abstract void start();
    public abstract void update();
    public abstract void draw(Graphics2D g2);

    public boolean isCompleted() {
        return completed;
    }

    protected void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public abstract boolean isFinished();
}
