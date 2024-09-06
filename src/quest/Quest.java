package quest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Quest implements Serializable {
    private QuestEnum questEnum;
    private String name;
    private String description;
    public ArrayList<QuestObjective> objectives;

    public Quest(QuestEnum questEnum) {
        this.questEnum = questEnum;
        this.name = questEnum.getName();
        this.description = questEnum.getDescription();
        this.objectives = new ArrayList<>();
    }

    public QuestEnum getQuestEnum() {
        return questEnum;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<QuestObjective> getObjectives() {
        return objectives;
    }

    public void setObjectives(ArrayList<QuestObjective> objectives) {
        this.objectives = objectives;
    }

    public void addObjective(QuestObjective objective) {
        objectives.add(objective);
    }

    public boolean isCompleted() {
        for (QuestObjective objective : objectives) {
            if (!objective.isCompleted()) {
                return false;
            }
        }
        return true;
    }
}
