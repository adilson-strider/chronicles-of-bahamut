package object.interactables;

import cutscene_scripts.NotPassCutscene;
import entity.Entity;
import entity.EntityType;
import gamestate.StateMachine;
import main.GameManager;
import object.OBJ_Ore;
import object.OBJ_Special_Hammer;
import object.Object;
import object.consumables.OBJ_Health_Potion;
import object.consumables.OBJ_Key;
import quest.BlacksmithQuestObjective;
import quest.Quest;
import tools.ResourceLoader;
import ui.UI;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

public class OBJ_Chest extends Object {
	
	public OBJ_Chest() {
		name = "Chest";

		entityType = EntityType.OBSTACLE;

		collision = true;

		closedChest = ResourceLoader.loadImage("/images/objects/chest1.png");
		openedChest = ResourceLoader.loadImage("/images/objects/chest2.png");

		solidArea.x = 0;
		solidArea.y = 0;
		solidArea.width = 32;
		solidArea.height = 32;

		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;

		image = closedChest;
	}

	public void setLoot(Object loot){
		this.loot = loot;
	}

	public void interact(){
		UI.getInstance().entity = this;
		StateMachine.getInstance().change("Dialogue");

		if(!opened){

			GameManager.playSFX(3);
			// dialogues[0][0] = STR."Você encontrou um \{loot}!";

			if(GameManager.getPlayer().inventory.countItems() >= 20){
				dialogues[0][0] = "...mas não há mais espaço!";
			}
			else{
				dialogues[0][0] = STR."Você obteve um \{loot.name}.";

				if(Objects.equals(loot.name, "Ore")){
					// A quest do Ferreiro é a primeira
					Quest blackSmith = GameManager.questManager.getQuests().getFirst();

					// Marca o primeiro objetivo como completo
					blackSmith.getObjectives().get(BlacksmithQuestObjective.FIND_ORE.getIndex()).setCompleted(true);
					GameManager.canPass = false;
				}

				GameManager.getPlayer().canObtainItem(loot);
				image = openedChest;
				opened = true;
			}
		}
		else{
			dialogues[0][0] = "Está vazio...";
		}
	}

	@Override
	public boolean use(Entity entity) {
		return false;
	}
}
