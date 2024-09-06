package object.interactables;


import entity.Entity;
import entity.EntityType;
import gamestate.StateMachine;
import object.Object;
import tools.ResourceLoader;
import ui.UI;

public class OBJ_Door extends Object {
	
	public OBJ_Door() {
		name = "Door";
		solidArea.x = 0;
		solidArea.y = 16;
		solidArea.width = 48;
		solidArea.height = 32;
		entityType = EntityType.OBSTACLE;
		image = ResourceLoader.loadImage("/images/objects/door1.png");
		collision = true;
	}

	public void interact(){
		UI.getInstance().entity = this;
		StateMachine.getInstance().change("Dialogue");
		dialogues[0][0] = "Você precisa de uma chave!";
	}

	@Override
	public boolean use(Entity entity) {
		return false;
	}
}
