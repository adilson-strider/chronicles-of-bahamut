package entity.npcs;

import gamestate.StateMachine;
import main.GameManager;
import object.consumables.OBJ_Health_Potion;
import object.swords.*;
import quest.BlacksmithQuestObjective;
import quest.Quest;
import quest.QuestEnum;
import quest.QuestObjective;
import ui.UI;

public class NPC_Blacksmith extends NPC {

    public Quest blackSmith;

    public NPC_Blacksmith(){
        npcType = "Blacksmith";
        name = npcType;
        setItems();

        // Criar uma quest
        blackSmith = new Quest(QuestEnum.HELP_BLACKSMITH);
        blackSmith.addObjective(new QuestObjective("Fale com o Ferreiro."));
        blackSmith.addObjective(new QuestObjective("Encontre o martelo no bosque."));
        blackSmith.addObjective(new QuestObjective("Devolva o martelo ao Ferreiro."));
        GameManager.questManager.addQuest(blackSmith);


        setDialogue();
    }

    public void setDialogue(){
        // Se a segunda quest da primeira missão não estiver completa...
        if(!blackSmith.getObjectives().get(BlacksmithQuestObjective.FIND_ORE.getIndex()).isCompleted()){
            dialogues[0][0] = "Por favor, encontre o minério...";
        }
        else {
            dialogues[0][0] = "Você conseguiu achar o minério?";
        }

        if(blackSmith.getObjectives().get(BlacksmithQuestObjective.RETURN_ORE.getIndex()).isCompleted()){

            dialogues[0][0] = "Bom dia! O que você quer negociar?";
            dialogues[0][1] = "Volte sempre, hehe!";
            dialogues[0][2] = "Você precisa de mais dinheiro...";
            dialogues[0][3] = "O inventário está cheio!";
            dialogues[0][4] = "Seu inventário está vazio!";
        }
    }

    public void setItems(){

        inventory.add(new SteelLongsword(), 0);
        inventory.add(new TrainingSword(), 1);
        inventory.add(new OBJ_Health_Potion(), 2);
        inventory.add(new BeginnerBlade(), 3);
        inventory.add(new CurvedScimitar(), 4);
        inventory.add(new IronKatana(), 5);
    }

    public void speak(){

        if (blackSmith.getObjectives().get(BlacksmithQuestObjective.RETURN_ORE.getIndex()).isCompleted()) {

            StateMachine.getInstance().change("Shop");
        } else {
            StateMachine.getInstance().change("Dialogue");
        }

        setDialogue();

        UI.getInstance().entity = this;
    }
}

