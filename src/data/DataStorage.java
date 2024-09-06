package data;

import object.Shield;
import object.Weapon;
import quest.Quest;
import quest.QuestObjective;
import tiledmap.GameEvent;
import tools.Asset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStorage implements Serializable {

    int level;
    int maxHp;
    int hp;
    int maxMp;
    int mp;
    int strength;
    int dexterity;
    int exp;
    int nextLevelExp;
    int gold;

    int playerX;
    int playerY;

    int mapNum;

    String currentWeapon;
    String currentShield;

    ArrayList<String> itemNames = new ArrayList<>();
    ArrayList<Integer> itemAmounts = new ArrayList<>();

    // Novo campo para registrar cutscenes assistidas
    ArrayList<String> watchedCutscenes = new ArrayList<>();
    ArrayList<String> doneEvent = new ArrayList<>();

    String mapObjectNames[][];
    int mapObjectWorldX[][];
    int mapObjectWorldY[][];
    String mapObjectLootNames[][];
    boolean mapObjectOpened[][];

    ArrayList<Quest> quests = new ArrayList<>();

    // Adicionando uma estrutura para armazenar objetivos
    public Map<String, ArrayList<QuestObjective>> questObjectives = new HashMap<>();
}

