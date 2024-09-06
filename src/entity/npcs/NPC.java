package entity.npcs;

import entity.Entity;
import entity.Player;
import quest.QuestManager;

import java.util.Random;

public abstract class NPC extends Entity {

    public abstract void speak();

    public void setAction(){

        // Corrige atualização de direção do npc
        actionLockCounter++;

        if(actionLockCounter == 120){

            Random rand = new Random();
            int i = rand.nextInt(100)+1;

            if (i <= 25) {
                direction = "up";
                animation = walkUp;
                animation.start();
            }
            if(i > 25 && i <= 50){
                direction = "down";
                animation = walkDown;
                animation.start();
            }
            if(i > 50 && i <= 75){
                direction = "left";
                animation = walkLeft;
                animation.start();
            }
            if(i > 75){
                direction = "right";
                animation = walkRight;
                animation.start();
            }

            actionLockCounter = 0;
        }

        moveNpc();
    }
}
