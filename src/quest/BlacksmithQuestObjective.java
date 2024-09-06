package quest;

public enum BlacksmithQuestObjective {
    TALK_TO_BLACKSMITH(0),
    FIND_ORE(1),
    RETURN_ORE(2);

    private final int index;

    BlacksmithQuestObjective(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}