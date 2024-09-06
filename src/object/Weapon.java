package object;

import entity.Entity;
import entity.EntityType;
import graphics.Animation;
import tools.ResourceLoader;

import static main.AssetSetter.loadAnimation;

public class Weapon extends Object {

    public int getAttackValue() {
        return getAttackPower();
    }

    public int getDefenseValue() { return getDefensePower(); }

    public void setAttackPower(int attack) {
        this.attack = attack;
    }

    public void setDefensePower(int defense) {
        this.defense = defense;
    }

    public void loadAttrb(EntityType type, String name, String imagePath, int atk, int def, int price, String descr, boolean stackable){
        entityType = type;
        this.name = name;
        image = ResourceLoader.loadImage(imagePath);
        setAttackPower(atk);
        setDefensePower(def);
        setPrice(price);
        setDescription(descr);
        this.stackable = stackable;
    }

    public void loadAnimationFrameSword(String imagePath, int rows, int cols){
        weaponFrame = loadAnimation(imagePath, rows, cols, 192, 192);
        weaponAnimation = new Animation(weaponFrame, 7);
    }

    @Override
    public boolean use(Entity entity) {
        return false;
    }
}