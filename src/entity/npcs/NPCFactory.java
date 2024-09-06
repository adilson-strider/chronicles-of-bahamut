package entity.npcs;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class NPCFactory {
    private static final Map<String, Supplier<NPC>> npcMap = new HashMap<>();

    static {
        npcMap.put("Oldman", NPC_OldMan::new);
        npcMap.put("Blacksmith", NPC_Blacksmith::new);
        npcMap.put("Isabelle", NPC_Isabelle::new);
        npcMap.put("Helena", NPC_Helena::new);
    }

    public static NPC createNPC(String npcType) {
        Supplier<NPC> npc = npcMap.get(npcType);
        if (npc != null) {
            return npc.get();
        }
        throw new IllegalArgumentException("Invalid NPC type: " + npcType);
    }
}