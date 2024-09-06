package main;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import cutscene.CutsceneManager;
import cutscene_scripts.*;
import data.SaveLoad;
import entity.Entity;
import entity.npcs.NPC;
import entity.Player;
import gamestate.GameStates;
import gamestate.StateMachine;
import gamestate.states.battle.PlayerSelectBattleMenuState;
import gamestate.states.battle.BattleItemState;
import gamestate.states.battle.BattleState;
import gamestate.states.battle.EnemySelectState;
import gamestate.states.cutscene.KaiInitialCutscene;
import gamestate.states.game.*;
import gamestate.states.menu.*;
import object.Object;
import quest.QuestManager;
import tiledmap.GameEvent;
import tiledmap.TiledMap;
import tools.DebugMode;
import tools.GameNotification;
import ui.UI;

public class GameManager extends JPanel implements Runnable {

	// SCREEN SETTINGS
	public static final int originalTileSize = 16; // 16x16 tile
	public static final int scale = 2;

	public static final int tileSize = originalTileSize * scale; // 32x32 tile

	public static final int screenWidth = 512;
	public static final int screenHeight = 384;

	public static final int maxScreenCol = screenWidth/tileSize;   // 16
	public static final int maxScreenRow = screenHeight/tileSize;  // 12

	public static int MARGIN_X = screenWidth / 2 - tileSize / 2;
	public static int MARGIN_Y = screenHeight / 2 - tileSize / 2;

	public static boolean canSave = false;

	// FULL SCREEN

	// GENERAL FIELDS
	public static GameStates gameStates;
	public static final int maxMap = 100;
	public static int currentMap = 0;
	public static CutsceneManager cutsceneManager;
	int FPS = 60;
	public static EventManager eventManager;
	static Sound music = new Sound();
	static Sound sfx = new Sound();
	public static Map<String, TiledMap> maps = new HashMap<>();
	public AssetSetter aSetter = new AssetSetter();
	Thread gameThread;
	public static GameNotification notifications;
	public static QuestManager questManager = new QuestManager();


	// ENTITY AND OBJECT
	public static ArrayList<Player> players = new ArrayList<>();

	//public static NPC[][] npcs = new NPC[maxMap][100];
	public static ArrayList<ArrayList<NPC>> npcs = new ArrayList<>();

	static {
		for (int i = 0; i < maxMap; i++) {
			npcs.add(new ArrayList<>());
		}
	}

	public static Object[][] obj = new Object[maxMap][100];

	private static DebugMode debugMode;
	public static TiledMap currentTiledMap;
	public static boolean isDialogue = false;
	public static SaveLoad gameData = new SaveLoad();
	public static boolean canPass = true;

	// GAME TIME
	private long startTime;
	private static long elapsedTime;

	// Camera
    public static Camera camera;

	public GameManager() {

		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		InputManager.getInstance().registerComponent(this);
		this.setFocusable(true);

		startTime = System.currentTimeMillis();
		elapsedTime = System.currentTimeMillis() - startTime;

		setupGame();

		camera = new Camera();
	}

	public void setupGame() {
		createPlayers();
		cutsceneManager = new CutsceneManager();
		createMap();
		createEventManager();
		setStateMachine();
	}

	public void setStateMachine(){

		StateMachine.getInstance().add("Title",   new TitleState());
		StateMachine.getInstance().add("InGame",   new InGameState());
		StateMachine.getInstance().add("Dialogue",   new DialogueState());
		StateMachine.getInstance().add("Pause",   new PauseState());
		StateMachine.getInstance().add("Transition",   new TransitionState());
		StateMachine.getInstance().add("GameOver", new GameOverState());
		StateMachine.getInstance().add("InGameMenu", new InGameMenuState());
		StateMachine.getInstance().add("EnemySelect", new EnemySelectState());
		StateMachine.getInstance().add("Shop", new ShopState());
		StateMachine.getInstance().add("Inventory", new InventoryState());
		StateMachine.getInstance().add("Save", new SaveState());
		StateMachine.getInstance().add("BattleItem", new BattleItemState());
		StateMachine.getInstance().add("PlayerSelect", new PlayerSelectBattleMenuState()); // Battle
		StateMachine.getInstance().add("PlayerMenu", new PlayerSelectMenuState());
		StateMachine.getInstance().add("StatusScreen", new StatusScreenState());

		// CUTSCENE STATES
		//StateMachine.getInstance().add("test", new TestCutscene());
		StateMachine.getInstance().add("Blacksmith", new BlacksmithCutscene());
		StateMachine.getInstance().add("intro", new CrabBossCutscene());
		StateMachine.getInstance().add("Opening", new OpeningCutscene());

		StateMachine.getInstance().change("Title");

		gameStates = GameStates.TITLE;
	}

	public static String getFormattedElapsedTime() {
		long seconds = elapsedTime / 1000;
		long hours = seconds / 3600;
		long minutes = (seconds % 3600) / 60;
		seconds = seconds % 60;

		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	public void updateElapsedTime() {
		elapsedTime = System.currentTimeMillis() - startTime;
	}

	public void createEventManager(){
		eventManager = new EventManager();
	}

	public void createMap() {

		// CITIES
		maps.put("overworld", new TiledMap("/maps/overworld.json"));
		maps.put("briarwood", new TiledMap("/maps/briarwood.json"));
		maps.put("valenor", new TiledMap("/maps/valenor.json"));
		maps.put("valenor-as", new TiledMap("/maps/valenor-as.json"));
		maps.put("village1", new TiledMap("/maps/village-int1.json"));
		maps.put("thaloria", new TiledMap("/maps/thaloria.json"));

		maps.put("briarwood-church", new TiledMap("/maps/briarwood-church.json"));

		// DUNGEON
		maps.put("dungeon1", new TiledMap("/maps/dungeon1.json"));
		maps.put("dungeon2", new TiledMap("/maps/dungeon2.json"));
		maps.put("dungeon3", new TiledMap("/maps/dungeon3.json"));
		maps.put("dungeon4", new TiledMap("/maps/dungeon4.json"));

		setMapAndPosition("overworld", 33, 32);
	}

	public void createPlayers() {

		int baseX = (GameManager.screenWidth / 2) + (GameManager.tileSize * 3);
		int baseY = (GameManager.screenHeight / 2) - (GameManager.tileSize * 2);
		int[] xOffsets = {-120, -60, 40};

		players.add(new Player(new Point(baseX + (-120), baseY)));

		aSetter.setPlayers(players);
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {
		double drawInterval = (double) 1_000_000_000 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;

		while (gameThread != null) {

			currentTime = System.nanoTime();

			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);

			lastTime = currentTime;

			if (delta >= 1) {

				update();
				InputManager.getInstance().update();
				repaint();
				delta--;
			}

			if (timer >= 1_000_000_000) {
				timer = 0;
			}
		}
	}

	public void update() {
		currentTiledMap.update();
		StateMachine.getInstance().update();
		DebugMode.getInstance().update();
		updateElapsedTime();

		eventManager.checkEvent();
		camera.centerOnPlayer(getPlayer());
	}

	public static Player getPlayer(){
		return players.getFirst();
	}

	public static Player getPlayerAtIndex(int index){
		return players.get(index);
	}

	public static ArrayList<Player> getPlayers(){
		return players;
	}

	public static int getTileSize(){
		return tileSize;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		StateMachine.getInstance().draw(g2);
		g2.dispose();
	}

	public static void draw(Graphics2D g2){

		long drawStart;
		drawStart = System.nanoTime();
		// TILE
		currentTiledMap.draw(g2, 0, camera);

		currentTiledMap.draw(g2, 1, camera);

		UI.getInstance().draw(g2);

		for (NPC npc : npcs.get(currentMap)) {
			if (npc != null) {
				npc.draw(g2, camera);
			}
		}

		currentTiledMap.draw(g2, 2, camera);

		eventManager.draw(g2);

		// OBJECT
		for(int i = 0; i < obj[1].length; i++) {
			if(obj[currentMap][i] != null) {
				obj[currentMap][i].draw(g2, camera);
			}
		}

		//PLAYER
		getPlayer().draw(g2, camera);

		long drawEnd = System.nanoTime();
		long passed = drawEnd - drawStart;

		DebugMode.getInstance().showDebugMode(g2, passed);

		// questManager.draw(g2);
	}

	public static void startBattle(boolean isBoss, int index){
		GameManager.gameStates = GameStates.BATTLE;
		StateMachine.getInstance().change("Transition");
		StateMachine.getInstance().add("Battle", new BattleState(isBoss, index));
	}

	public static void playMusic(int i) {

		music.setFile(i);
		music.play();
		music.loop();
	}

	public static void stopMusic() {
		music.stop();
	}

	public static void playSFX(int i) {
		sfx.setFile(i);
		sfx.play();
	}

	public static void resumeMusic(){
		music.play();
		music.loop();
	}

	public static void quitGame(){
		System.exit(0);
	}

	public static void resetGame(){
		getPlayer().hp = 50;
		getPlayer().atk = 5;
		getPlayer().def = 10;
		getPlayer().maxHp = getPlayer().hp;
		currentTiledMap.battleCoolDown = 200;
		currentTiledMap.enemyFound = null;
	}

	public static void setMapAndPosition(String map, int x, int y) {
		currentTiledMap = GameManager.maps.get(map);
		currentMap = currentTiledMap.mapNum;

		Map<Integer, String> mapNames = new HashMap<>();
		mapNames.put(0, "overworld");
		mapNames.put(1, "briarwood");
		mapNames.put(2, "valenor");
		mapNames.put(3, "valenor-as");
		mapNames.put(4, "village1");
		mapNames.put(5, "thaloria");

		String mapName = mapNames.get(currentMap);
		if (mapName != null) {
			TiledMap.mapName = mapName;
		} else {
			System.err.println("Mapa inválido: " + currentMap);
		}

		getPlayer().setPosition(new Point(x, y));
	}

	public static void checkLevelUp(ArrayList<Player> players) {

		for(Player player : players){
			while (player.exp >= player.nextLevelExp) {
				player.level++;
				player.maxHp += (int) ((player.level * 2) * 1.5);
				player.strength++;
				player.dexterity++;
				player.atk = player.getAttack();
				player.def = player.getDefense();
				player.nextLevelExp = (int) (150 * Math.pow(player.level + 3, 2.4 + (50.0 / 100)) / Math.pow(5, 2.4 + (50.0 / 100)));
			}
		}
	}
}
