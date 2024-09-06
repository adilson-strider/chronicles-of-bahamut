package object.swords;

import entity.EntityType;
import object.Weapon;
import tools.ResourceLoader;

public class IronKatana extends Weapon {
    public IronKatana() {
        super();

        entityType = EntityType.SWORD;

        name = "Iron Katana";
        image = ResourceLoader.loadImage("/images/icons/sword/Iron Katana.png");
        setAttackPower(35);
        setDefensePower(5);
        setPrice(350);
        setDescription(STR."Uma katana de ferro com lâmina afiada, usada por samurais.");
        stackable = true;
    }
}
