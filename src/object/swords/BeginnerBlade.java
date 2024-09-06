package object.swords;

import entity.EntityType;
import object.Weapon;
import tools.ResourceLoader;

public class BeginnerBlade extends Weapon {
    public BeginnerBlade() {
        super();

        loadAnimationFrameSword("/images/fx/Sword1.png", 2, 5);

        loadAttrb(EntityType.SWORD,
                "Beginner's Blade",
                "/images/icons/sword/Beginner's Blade.png",
                15,
                3,
                100,
                STR."Uma espada simples e leve, ideal para iniciantes em combate.",
                true);
    }
}