package quest;

import ui.UI;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuestManager {
    private ArrayList<Quest> quests;

    public QuestManager() {
        quests = new ArrayList<>();
    }

    public void addQuest(Quest quest) {
        quests.add(quest);
        System.out.println("Quest added: " + quest.getName());
    }

    public Quest getQuest(QuestEnum questEnum) {
        for (Quest quest : quests) {
            if (quest.getQuestEnum() == questEnum) {
                return quest;
            }
        }
        return null;
    }

    public ArrayList<Quest> getQuests() {
        return quests;
    }

    public void setQuests(ArrayList<Quest> quests) {
        this.quests = quests;
    }

    public void completeObjective(Quest quest, int objectiveIndex) {
        QuestObjective objective = quest.getObjectives().get(objectiveIndex);
        objective.setCompleted(true);
        System.out.println(STR."Objective completed: \{objective.getDescription()}");
    }

    public void update() {
        for (Quest quest : quests) {
            if (quest.isCompleted()) {
                // Pode adicionar lógica para recompensar o jogador aqui
            }
        }
    }

    public void draw(Graphics2D g2) {

        UI.getInstance().setColorAndFont(g2, Color.WHITE, Font.PLAIN, 11);
        // Desenhar as quests na tela (opcional)
        int y = 10;
        for (Quest quest : quests) {
            g2.drawString("Quest: " + quest.getName(), 10, y);
            y += 15;
            for (QuestObjective objective : quest.getObjectives()) {
                g2.drawString("- " + objective.getDescription() + (objective.isCompleted() ? " (Completed)" : "(Not completed)"), 20, y);
                y += 15;
            }
        }
    }
}
