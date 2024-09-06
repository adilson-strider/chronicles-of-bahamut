package object.consumables;

import entity.Entity;
import gamestate.StateMachine;
import main.GameManager;
import object.Object;
import tools.ResourceLoader;
import ui.UI;

import java.io.IOException;

import javax.imageio.ImageIO;


public class OBJ_Key extends Object {
	public OBJ_Key() {
		name = "Key";
        stackable = true;
        image = ResourceLoader.loadImage("/images/objects/key1.png");
    }

    public boolean use(Entity entity) {
        System.out.println("Método use foi chamado.");
        UI.getInstance().entity = this;
        StateMachine.getInstance().change("Dialogue");

        int objIndex = getDetected(entity, GameManager.obj, "Door");
        System.out.println("Resultado de getDetected: " + objIndex);

        if (objIndex != 999) {
            StateMachine.getInstance().change("Dialogue");
            dialogues[0][0] = "Você abriu a porta!";
            amount--;
            GameManager.playSFX(3);
            GameManager.obj[GameManager.currentMap][objIndex] = null;

            return true;
        } else {
            StateMachine.getInstance().change("Dialogue");
            dialogues[0][0] = "O que você está fazendo?";

            return false;
        }
    }

}
