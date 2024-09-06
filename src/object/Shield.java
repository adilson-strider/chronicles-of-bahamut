package object;

import entity.Entity;

public class Shield extends Object {
    private int defenseValue;

    public int getDefenseValue() {
        return defenseValue;
    }

    public void setDefenseValue(int defenseValue) {
        this.defenseValue = defenseValue;
    }

    @Override
    public boolean use(Entity entity) {
        return false;
    }
}
