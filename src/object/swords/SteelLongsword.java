package object.swords;

import entity.EntityType;
import object.Weapon;
import tools.ResourceLoader;

public class SteelLongsword extends Weapon {
    public SteelLongsword() {
        super();

        entityType = EntityType.SWORD;

        name = "Steel Longsword";
        image = ResourceLoader.loadImage("/images/icons/sword/Steel Longsword.png");
        setAttackPower(50);
        setDefensePower(10);
        setPrice(500);
        setDescription(STR."Espada longa e de aço, usada por guerreiros experientes.");
        stackable = true;
    }
}
