package object.swords;

import entity.EntityType;
import graphics.Animation;
import object.Weapon;
import tools.ResourceLoader;

import static main.AssetSetter.loadAnimation;

public class TrainingSword extends Weapon {
    public TrainingSword() {
        super();

        entityType = EntityType.SWORD;
        weaponFrame = loadAnimation("/images/fx/Sword1.png", 2, 5, 192, 192);
        weaponAnimation = new Animation(weaponFrame, 7);
        name = "Training Sword";
        image = ResourceLoader.loadImage("/images/icons/sword/Training Sword.png");
        setAttackPower(10);
        setDefensePower(2);
        setPrice(50);
        setDescription(STR."Uma espada básica usada por novos recrutas para treinamento.");
        stackable = true;
    }
}
