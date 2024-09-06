package object.consumables;

import entity.Entity;
import entity.EntityType;
import main.GameManager;
import tools.ResourceLoader;

public class OBJ_Mana_Potion extends Entity {

    private int value = 5;

    public OBJ_Mana_Potion() {
        super();
        name = "Mana Potion";
        entityType = EntityType.CONSUMABLE;
        image = ResourceLoader.loadImage("/images/icons/potions/mPotion.png");
        atk = 2;
        description = STR."\{name}:\nRestaura \{value} de mana.";
    }

    public boolean use(Entity entity){
        entity.mana += value;
        if(GameManager.getPlayer().mana > GameManager.getPlayer().maxMana){
            GameManager.getPlayer().mana = GameManager.getPlayer().maxMana;
            GameManager.playSFX(15);
        }

        return true;
    }
}
