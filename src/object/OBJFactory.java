package object;

import entity.Entity;
import object.consumables.OBJ_Health_Potion;
import object.interactables.OBJ_Chest;
import object.interactables.OBJ_Rock;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class OBJFactory {
    private static final Map<String, Function<JSONObject, Entity>> objectMap = new HashMap<>();

    static {
        objectMap.put("chest", (_) -> new OBJ_Chest());
        objectMap.put("potion", (_) -> new OBJ_Health_Potion());
        objectMap.put("rock", (_) -> new OBJ_Rock());
        objectMap.put("hammer", (_) -> new OBJ_Special_Hammer());
        objectMap.put("pickaxe", (_) -> new Obj_Pickaxe());
    }

    public static Entity createObject(String objType, JSONObject properties) {
        Function<JSONObject, Entity> constructor = objectMap.get(objType.toLowerCase());
        if (constructor != null) {
            return constructor.apply(properties);
        }
        return new OBJ_Empty(); // Retorna um objeto vazio caso o tipo não seja encontrado
    }
}