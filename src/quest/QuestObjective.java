package quest;

import java.io.Serializable;

public class QuestObjective implements Serializable {
    private String description;
    private boolean completed;

    public QuestObjective(String description) {
        this.description = description;
        this.completed = false;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean isCompleted) {
        this.completed = isCompleted;
        System.out.println(STR."A quest \{description} foi completada.");
    }
}
