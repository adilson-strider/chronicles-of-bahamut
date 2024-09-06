package object;

import entity.Entity;
import gamestate.StateMachine;
import main.GameManager;
import tools.ResourceLoader;
import ui.UI;

public class OBJ_Special_Hammer extends Object {

    public OBJ_Special_Hammer() {
        name = "Hammer";
        stackable = true;
        image = ResourceLoader.loadImage("/images/objects/hammer.png");
    }

    @Override
    public boolean use(Entity entity) {
        return false;
    }
}
