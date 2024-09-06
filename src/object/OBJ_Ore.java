package object;

import entity.Entity;
import entity.npcs.NPC;
import entity.npcs.NPC_Blacksmith;
import gamestate.StateMachine;
import main.GameManager;
import quest.Quest;
import quest.QuestEnum;
import tools.ResourceLoader;

import java.util.Objects;

public class OBJ_Ore extends Object {
    public OBJ_Ore() {
        name = "Ore";
        stackable = true;
        image = ResourceLoader.loadImage("/images/objects/ore.png");
    }

    @Override
    public boolean use(Entity entity) {

        Quest blackSmithQuest = GameManager.questManager.getQuest(QuestEnum.HELP_BLACKSMITH);

        if (!blackSmithQuest.getObjectives().get(2).isCompleted()) {

            for (NPC npc : GameManager.npcs.get(GameManager.currentMap)) {
                if (npc != null && Objects.equals(npc.npcType, "Blacksmith")) {
                    npc.dialogues[0][0] = "Nossa, você é muito competente!";
                    npc.animation.start();
                    npc.speed = 1;
                }
            }

            StateMachine.getInstance().change("Dialogue");
            GameManager.questManager.completeObjective(blackSmithQuest, 2);

            return true;
        }

        return false;
    }
}
