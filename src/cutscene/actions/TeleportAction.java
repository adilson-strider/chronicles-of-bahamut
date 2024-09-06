package cutscene.actions;

import cutscene.CutsceneAction;
import entity.Player;
import main.GameManager;

import java.awt.*;

public class TeleportAction extends CutsceneAction {
    private Player player;
    private int targetX;
    private int targetY;
    private boolean finished;

    public TeleportAction(Player player, int targetX, int targetY) {
        this.player = player;
        this.targetX = targetX;
        this.targetY = targetY;
        this.finished = false;
    }

    @Override
    public void start() {
        player.setX(targetX);
        player.setY(targetY);
        finished = true;
    }

    @Override
    public void update() {
        // No update needed since the teleport is instant
    }

    @Override
    public void draw(Graphics2D g2) {

    }


    @Override
    public boolean isFinished() {
        return finished;
    }
}
