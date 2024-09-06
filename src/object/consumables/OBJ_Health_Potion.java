package object.consumables;

import entity.Entity;
import entity.EntityType;
import entity.Player;
import main.GameManager;
import object.Object;
import tools.ResourceLoader;

public class OBJ_Health_Potion extends Object {

    private int value = 50;

    public OBJ_Health_Potion() {
        super();

        entityType = EntityType.CONSUMABLE;

        name = "Potion";
        image = ResourceLoader.loadImage("/images/icons/potions/hPotion.png");
        setDescription(STR."Restaura \{value} de vida.");
        setPrice(25);
        stackable = true;
    }

    public boolean use(Entity entity) {
        // Apply the health potion effect to the player
        entity.hp += this.value;

        // Ensure the player's HP does not exceed the maximum HP
        if (entity.hp > entity.maxHp) {
            entity.hp = entity.maxHp;
        }
        return true;
    }
}