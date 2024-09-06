package cutscene.actions;

import cutscene.CutsceneAction;
import cutscene.CutsceneCamera;

import java.awt.*;

public class MoveCameraAction extends CutsceneAction {
    private CutsceneCamera camera;
    private Point targetPosition;
    private int speed;

    public MoveCameraAction(CutsceneCamera camera, Point targetPosition, int speed) {
        this.camera = camera;
        this.targetPosition = targetPosition;
        this.speed = speed;
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        if (!camera.moveTo(targetPosition, speed)) {
            setCompleted(true);
            isFinished = true;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        // Aqui você pode desenhar elementos específicos da ação, se necessário
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
