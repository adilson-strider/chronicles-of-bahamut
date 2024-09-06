package object;

import entity.Entity;
import gamestate.StateMachine;
import main.GameManager;
import tools.ResourceLoader;
import ui.UI;

import java.util.ArrayList;

public class Obj_Pickaxe extends Object {
    public Obj_Pickaxe() {
        name = "Pickaxe";
        stackable = true;
        image = ResourceLoader.loadImage("/images/objects/pickaxe.png");
    }

    @Override
    public boolean use(Entity entity) {
        UI.getInstance().entity = this;
        StateMachine.getInstance().change("Dialogue");

        int objIndex = getDetected(entity, GameManager.obj, "Rock");

        if (objIndex != 999) {
            StateMachine.getInstance().change("Dialogue");
            dialogues[0][0] = "Você removeu a pedra!";
            GameManager.playSFX(3);

            // Remover o objeto da lista
            Object[] currentMapObjects = GameManager.obj[GameManager.currentMap];
            if (currentMapObjects != null && objIndex < currentMapObjects.length) {
                currentMapObjects[objIndex] = null;
            }

            return true;
        } else {
            StateMachine.getInstance().change("Dialogue");
            dialogues[0][0] = "O que você está fazendo?";

            return false;
        }
    }

}
