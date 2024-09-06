package cutscene.actions;

import cutscene.CutsceneAction;
import entity.Entity;
import main.GameManager;
import tools.GameNotification;

import java.awt.*;

public class ItemGivenAction extends CutsceneAction {

    private Entity entity;
    private boolean finished = false;

    public ItemGivenAction(Entity entity){
        this.entity = entity;
    }

    @Override
    public void start() {
        GameManager.getPlayer().canObtainItem(this.entity);
        GameManager.notifications = new GameNotification(STR."Você recebeu \{this.entity.name}", 90);
    }

    @Override
    public void update() {
        GameManager.notifications.update();

        if(GameManager.notifications.isFlag()){
            finished = true;
            GameManager.notifications.counter = 0;
            GameManager.notifications.flag = false;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        GameManager.notifications.drawNotification(g2, 0, 0);
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
