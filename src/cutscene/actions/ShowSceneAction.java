package cutscene.actions;

import cutscene.CutsceneAction;
import main.GameManager;
import tiledmap.TiledMap;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ShowSceneAction extends CutsceneAction {

    private TiledMap scene;
    private int duration; // duração em frames
    private int currentFrame;

    public ShowSceneAction(TiledMap scene, int duration) {
        this.scene = scene;
        this.duration = duration;
        this.currentFrame = 0;
    }

    @Override
    public void start() {
        currentFrame = 0;
    }

    @Override
    public void update() {
        currentFrame++;
        if (currentFrame >= duration) {
            isFinished = true;
        }
    }

    @Override
    public void draw(Graphics2D g2) {

        scene.draw(g2, 0, GameManager.camera);
        scene.draw(g2, 1, GameManager.camera);
        scene.draw(g2, 2, GameManager.camera);
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
