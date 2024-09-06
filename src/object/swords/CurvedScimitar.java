package object.swords;

import entity.EntityType;
import graphics.Animation;
import object.Weapon;
import tools.ResourceLoader;

import static main.AssetSetter.loadAnimation;

public class CurvedScimitar extends Weapon {
    public CurvedScimitar() {
        super();

        loadAnimationFrameSword("/images/fx/Sword3.png", 3, 5);

        loadAttrb(EntityType.SWORD,
                "Curved Scimitar",
                "/images/icons/sword/Curved Scimitar.png",
                30,
                4,
                300,
                STR."Uma cimitarra curva, perfeita para ataques rápidos e precisos.",
                true);
    }
}