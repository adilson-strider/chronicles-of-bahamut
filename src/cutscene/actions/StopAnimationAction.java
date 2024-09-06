package cutscene.actions;

import cutscene.CutsceneAction;
import entity.npcs.NPC;

import java.awt.*;

public class StopAnimationAction extends CutsceneAction {

    NPC npc;
    private boolean finished = false;

    public StopAnimationAction(NPC npc) {
        this.npc = npc;
    }
    @Override
    public void start() {
        npc.animation.stop();
        npc.speed = 0;
        finished = true;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g2) {

    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
