package object.shields;

import entity.Entity;
import entity.EntityType;
import tools.ResourceLoader;

public class OBJ_Wood_Shield extends Entity {
    public OBJ_Wood_Shield() {
        super();

        name = "Wood Shield";
        entityType = EntityType.SHIELD;
        image = ResourceLoader.loadImage("/images/icons/shield/wood-shield.png");
        def = 2;
        description = STR."\{name}:\nUm escudo leve e barato.";
    }
}
