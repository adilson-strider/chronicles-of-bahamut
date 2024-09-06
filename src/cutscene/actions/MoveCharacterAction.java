package cutscene.actions;

import cutscene.CutsceneAction;
import entity.Entity;

import java.awt.*;

public class MoveCharacterAction extends CutsceneAction {
    private Entity entity;
    private Point destination;

    private int speed;
    private boolean movingHorizontally;

    public MoveCharacterAction(Entity entity, Point destination, int speed) {
        this.entity = entity;
        this.destination = destination;
        this.isFinished = false;
        this.speed = speed;
        this.movingHorizontally = true;
    }

    @Override
    public void start() {
        entity.moving = true;
        isFinished = false;
        System.out.println("Movendo para: " + destination);
    }

    @Override
    public void update() {
        if (!isFinished) {
            entity.moveToNextPosition(entity, movingHorizontally, destination);
            if (entity.getX() == destination.x && entity.getY() == destination.y) {
                isFinished = true;
                entity.moving = false;
                System.out.println("Alcançado destino: " + destination);
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {

    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
