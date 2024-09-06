package object.interactables;

import entity.Entity;
import entity.EntityType;
import gamestate.StateMachine;
import object.Object;
import tools.ResourceLoader;
import ui.UI;

public class OBJ_Rock extends Object {
    public OBJ_Rock() {
        name = "Rock";
        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        entityType = EntityType.OBSTACLE;
        image = ResourceLoader.loadImage("/images/objects/rock.png");
        collision = true;
    }

    public void interact(){
        UI.getInstance().entity = this;
        StateMachine.getInstance().change("Dialogue");
        dialogues[0][0] = "Não dá pra remover...";
        dialogues[0][1] = "Podemos quebrar com um martelo...";
    }

    @Override
    public boolean use(Entity entity) {
        return false;
    }
}
