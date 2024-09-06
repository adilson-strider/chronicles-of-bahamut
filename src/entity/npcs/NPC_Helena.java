package entity.npcs;

import gamestate.StateMachine;
import main.GameManager;
import quest.Quest;
import quest.QuestEnum;
import quest.QuestObjective;
import ui.UI;

public class NPC_Helena extends NPC{

    public Quest findTheCat;

    public NPC_Helena() {
        npcType = "Helena";
        speed = 1;

        // Criar uma quest
        findTheCat = new Quest(QuestEnum.FIND_KITTEN);
        findTheCat.addObjective(new QuestObjective("Falar com Helena."));
        findTheCat.addObjective(new QuestObjective("Encontrar o gato."));
        findTheCat.addObjective(new QuestObjective("Devolver o gato à Helena."));
        GameManager.questManager.addQuest(findTheCat);

        setDialogue();
    }

    public void setDialogue() {
        dialogues[0][0] = "Você viu meu gatinho?";
    }

    @Override
    public void speak() {
        StateMachine.getInstance().change("Dialogue");

        setDialogue();

        UI.getInstance().entity = this;
    }
}
