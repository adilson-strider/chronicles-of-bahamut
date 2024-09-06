package entity.npcs;

public class NPC_Isabelle extends NPC {
    public NPC_Isabelle(){
        setDialogue();
        npcType = "Isabelle";
    }

    public void setAction(){
        animation.update();
        direction = "left";
        animation = walkLeft;
    }

    public void setDialogue(){

        dialogues[0][0] = "Para acessar o menu, pressione I.";
        dialogues[0][1] = "Para sair, pressione ESC.";
    }

    @Override
    public void speak(){
        startDialogue(this, dialogueSet, "dialogue");

        // Normal case
        dialogueSet++;

        if(dialogues[dialogueSet][0] == null){
            dialogueSet--;
        }
    }
}
