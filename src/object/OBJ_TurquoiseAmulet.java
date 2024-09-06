package object;

import entity.Entity;
import entity.EntityType;
import tools.ResourceLoader;

public class OBJ_TurquoiseAmulet extends Object {
    public OBJ_TurquoiseAmulet() {
        super();

        entityType = EntityType.SWORD;

        name = "Turquoise Amulet";
        image = ResourceLoader.loadImage("/images/objects/turquoise.png");
        setDescription(STR."Misteriosa herança familiar.");
        stackable = false;
    }

    @Override
    public boolean use(Entity entity) {
        return false;
    }
}
