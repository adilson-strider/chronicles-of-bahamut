package data;

import entity.Entity;
import main.GameManager;
import object.*;
import object.Object;
import object.consumables.OBJ_Health_Potion;
import object.interactables.OBJ_Chest;
import object.interactables.OBJ_Rock;
import object.swords.*;
import quest.Quest;
import quest.QuestObjective;
import tiledmap.TiledMap;

import java.io.*;
import java.util.*;

public class SaveLoad {

    public Object getObject(String itemName){

        return switch (itemName) {
            case "Potion" -> new OBJ_Health_Potion();
            case "Steel Longsword" -> new SteelLongsword();
            case "Beginner's Blade" -> new BeginnerBlade();
            case "Curved Scimitar" -> new CurvedScimitar();
            case "Iron Katana" -> new IronKatana();
            case "Training Sword" -> new TrainingSword();
            case "Ore" -> new OBJ_Ore();
            case "Rock" -> new OBJ_Rock();
            case "Pickaxe" -> new Obj_Pickaxe();
            case "Turquoise Amulet" -> new OBJ_TurquoiseAmulet();
            case "Chest" -> new OBJ_Chest();

            default -> throw new IllegalStateException("Unexpected value: " + itemName);
        };
    }

    public void save(){

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("save.dat"));
            DataStorage ds = new DataStorage();

            ds.level = GameManager.getPlayer().level;
            ds.maxHp = GameManager.getPlayer().maxHp;
            ds.hp = GameManager.getPlayer().hp;
            ds.maxMp = GameManager.getPlayer().maxMana;
            ds.mp = GameManager.getPlayer().mana;
            ds.strength = GameManager.getPlayer().strength;
            ds.dexterity = GameManager.getPlayer().dexterity;
            ds.exp = GameManager.getPlayer().exp;
            ds.nextLevelExp = GameManager.getPlayer().nextLevelExp;
            ds.gold = GameManager.getPlayer().gold;

            ds.playerX = GameManager.getPlayer().position.x;
            ds.playerY = GameManager.getPlayer().position.y;

            ds.mapNum = GameManager.currentMap;

            if(GameManager.getPlayer().currentWeapon != null){
                ds.currentWeapon = GameManager.getPlayer().currentWeapon.getName();
            }

            if(GameManager.getPlayer().currentShield != null){
                ds.currentShield = GameManager.getPlayer().currentShield.getName();
            }


            // Player Inventory
            for(int i = 0; i < GameManager.getPlayer().inventory.size(); i++){

                if(GameManager.getPlayer().inventory.get(i) != null){
                    ds.itemNames.add(GameManager.getPlayer().inventory.get(i).name);
                    ds.itemAmounts.add(GameManager.getPlayer().inventory.get(i).amount);
                }
            }

            ds.watchedCutscenes = GameManager.cutsceneManager.getWatchedCutscenes();

            ds.doneEvent = GameManager.eventManager.getEventDone();

            // Salvando os objetivos das quests
            for (Quest quest : GameManager.questManager.getQuests()) {
                ds.questObjectives.put(quest.getName(), quest.getObjectives());
            }

            ds.mapObjectNames = new String[GameManager.maxMap][GameManager.obj[1].length];
            ds.mapObjectWorldX = new int[GameManager.maxMap][GameManager.obj[1].length];
            ds.mapObjectWorldY = new int[GameManager.maxMap][GameManager.obj[1].length];
            ds.mapObjectLootNames = new String[GameManager.maxMap][GameManager.obj[1].length];
            ds.mapObjectOpened = new boolean[GameManager.maxMap][GameManager.obj[1].length];

            for(int mapNum = 0; mapNum < GameManager.maxMap; mapNum++){
                for (int i = 0; i < GameManager.obj[1].length; i++) {
                    if(GameManager.obj[mapNum][i] == null){
                        ds.mapObjectNames[mapNum][i] = "NA";
                        System.out.println("Objeto vazio.");
                    }
                    else{

                        System.out.println("Objeto registrado.");

                        ds.mapObjectNames[mapNum][i] = GameManager.obj[mapNum][i].name;
                        ds.mapObjectWorldX[mapNum][i] = GameManager.obj[mapNum][i].position.x;
                        ds.mapObjectWorldY[mapNum][i] = GameManager.obj[mapNum][i].position.y;

                        if(GameManager.obj[mapNum][i].loot != null){
                            ds.mapObjectLootNames[mapNum][i] = GameManager.obj[mapNum][i].loot.name;
                        }

                        ds.mapObjectOpened[mapNum][i] = GameManager.obj[mapNum][i].opened;
                    }
                }
            }

            oos.writeObject(ds);
            oos.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void load(){

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("save.dat"));

            DataStorage ds = (DataStorage) ois.readObject();

            GameManager.getPlayer().level = ds.level;
            GameManager.getPlayer().maxHp = ds.maxHp;
            GameManager.getPlayer().hp = ds.hp;
            GameManager.getPlayer().maxMana = ds.maxMp;
            GameManager.getPlayer().mana = ds.mp;
            GameManager.getPlayer().strength = ds.strength;
            GameManager.getPlayer().dexterity = ds.dexterity;
            GameManager.getPlayer().exp = ds.exp;
            GameManager.getPlayer().nextLevelExp = ds.nextLevelExp;
            GameManager.getPlayer().gold = ds.gold;

            GameManager.getPlayer().position.x = ds.playerX;
            GameManager.getPlayer().position.y = ds.playerY;

            if(ds.currentWeapon != null){
                GameManager.getPlayer().currentWeapon = (Weapon) getObject(ds.currentWeapon);
            }

            if(ds.currentShield != null){
                GameManager.getPlayer().currentShield = (Shield) getObject(ds.currentShield);
            }

            // Load player inventory
            GameManager.getPlayer().inventory.clear();
            int itemCount = Math.min(ds.itemNames.size(), ds.itemAmounts.size()); // Para garantir que você não acesse mais elementos do que existem

            for (int i = 0; i < itemCount; i++) {
                Entity item = getObject(ds.itemNames.get(i));
                if (item != null) {
                    item.amount = ds.itemAmounts.get(i);  // Set the amount before adding to inventory
                    GameManager.getPlayer().inventory.readd(item);  // Use add instead of readd
                } else {
                    System.out.println("Failed to create item: " + ds.itemNames.get(i));
                }
            }

            GameManager.cutsceneManager.setWatchedCutscenes(ds.watchedCutscenes);

            GameManager.eventManager.setEventDone(ds.doneEvent);

            // Restaurando os objetivos das quests
            for (Quest quest : GameManager.questManager.getQuests()) {
                ArrayList<QuestObjective> objectives = ds.questObjectives.get(quest.getName());
                if (objectives != null) {
                    quest.setObjectives(objectives);
                }
            }

            System.out.println("Carregando mapas...");
            loadMap(ds);

            for (int mapNum = 0; mapNum < GameManager.maxMap; mapNum++) {
                for (int i = 0; i < ds.mapObjectNames[mapNum].length; i++) {
                    if (Objects.equals(ds.mapObjectNames[mapNum][i], "NA")) {
                        GameManager.obj[mapNum][i] = null;
                        System.out.println("teste 1");
                    } else {

                        System.out.println("teste 2");
                        GameManager.obj[mapNum][i] = getObject(ds.mapObjectNames[mapNum][i]);
                        GameManager.obj[mapNum][i].position.x = ds.mapObjectWorldX[mapNum][i];
                        GameManager.obj[mapNum][i].position.y = ds.mapObjectWorldY[mapNum][i];

                        if (ds.mapObjectLootNames[mapNum][i] != null) {
                            GameManager.obj[mapNum][i].loot = getObject(ds.mapObjectLootNames[mapNum][i]);
                        }
                        GameManager.obj[mapNum][i].opened = ds.mapObjectOpened[mapNum][i];

                        if( GameManager.obj[mapNum][i].opened){
                            GameManager.obj[mapNum][i].image = GameManager.obj[mapNum][i].openedChest;
                        }
                    }
                }
            }



            ois.close();

        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadMap(DataStorage ds) {
        // Mapeamento dos números dos mapas para seus nomes
        Map<Integer, String> mapNumbers = new HashMap<>();
        mapNumbers.put(0, "overworld");
        mapNumbers.put(1, "briarwood");
        mapNumbers.put(2, "valenor");
        mapNumbers.put(3, "valenor-as");
        mapNumbers.put(4, "village1");
        mapNumbers.put(5, "thaloria");

        // DUNGEONS
        mapNumbers.put(6, "dungeon1");
        mapNumbers.put(7, "dungeon2");
        mapNumbers.put(8, "dungeon3");
        mapNumbers.put(9, "dungeon4");

        // Obter o nome do mapa correspondente ao número fornecido
        // ds.mapNum
        String mapName = mapNumbers.get(ds.mapNum);

        if (mapName != null) {
            GameManager.currentTiledMap = GameManager.maps.get(mapName);
            TiledMap.mapName = mapName;
        } else {
            // Tratar caso de número de mapa inválido
            System.err.println("Mapa inválido: " + ds.mapNum);
        }
    }
}
