package gamestate.states.battle;

public class Action {
    public enum ActionType {
        ATTACK, ITEM, ESCAPE
    }

    public ActionType type;
    public Object source;
    public Object target;

    public Action(ActionType type, Object source, Object target) {
        this.type = type;
        this.source = source;
        this.target = target;
    }
}