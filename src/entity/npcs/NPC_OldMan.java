package entity.npcs;

import java.util.Random;

public class NPC_OldMan extends NPC {
    public NPC_OldMan() {
        speed = 1;
        dialogueSet = -1;
        setDialogue();
    }

    public void setDialogue(){
        dialogues[0][0] = "Olá, jovem!";
        dialogues[0][1] = "Conheces a lenda do tesouro perdido?";
        dialogues[0][2] = "Não precisa se preocupar...";
        dialogues[0][3] = "É só uma lenda mesmo! Hahaha";

        dialogues[1][0] = "Pressione I";
        dialogues[1][1] = "Pressione Enter";

    }

    public void speak(){
        startDialogue(this, dialogueSet, "dialogue");

        // Normal case
        dialogueSet++;

        if(dialogues[dialogueSet][0] == null){
            dialogueSet--;
        }

        // Specifical case
        /*if(GameManager.player.hp < GameManager.player.maxHp/3){
            dialogueSet = 1;
        }*/
    }
}


