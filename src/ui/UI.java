package ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import cutscene.CutsceneManager;
import entity.Enemy;
import entity.Entity;
import entity.EntityType;
import entity.Player;
import gamestate.StateMachine;
import gamestate.states.menu.PlayerSelectMenuState;
import main.GameManager;
import tiledmap.TiledMap;
import tools.Asset;
import tools.FixedSizeList;
import tools.ResourceLoader;

import static battle.BattleManager.currentPlayerTurn;
import static battle.BattleManager.enemies;
import static gamestate.states.menu.PlayerSelectMenuState.playerMenuIndex;

public class UI {

	// Variáveis auxiliares
    public int scrollPosition = 0; // Posição atual de rolagem
    public int npcScrollPosition = 0; // Posição atual de rolagem
	public int visibleItems = 4; // Número de itens visíveis ao mesmo tempo
	public int totalItems = GameManager.getPlayer().inventory.size(); // Número total de itens no inventário
	int itemHeight = GameManager.getTileSize(); // Altura de cada item

	public boolean ready = false;

	public boolean dialogueActive = false;
	private boolean dialogueFinished = false;

	public String currentDialogue = "";
	public static Font maruMonica;

	public int npcItemIndex = 0;
	public int itemIndex = 0;

	public int alphaCounter = 0;

	public int count = 0;

	public int titleIndex = 0;
	public int battleIndex = 0;
	public int monsterIndex = 0;
	public int playerIndex = 0;

	public static ArrayList<Player> readyPlayers = new ArrayList<>();

	BufferedImage titleImage;
    public BufferedImage titleCursor;
    BufferedImage gameOverImage;
    BufferedImage nullCursor;
	public Point cursorPos;
	public int subState;
	public int commandNumber;
	public int npcSlotCol = 0;
	public int npcSlotRow = 0;
	public int playerSlotCol = 0;
	public int playerSlotRow = 0;

	public boolean isInventoryScreen = false;

	int cursorY;
	public boolean isBuyTrade = false;
	public boolean isSellTrade = false;
	public boolean isBattleItem = false;
	public boolean isStatusItem = false;

	// Menu Panel
    public Panel statsCharPanel;
	Panel headerMenuPanel;
	Panel menuPanel;
	public Panel playerPanel;
	Panel goldAndTimePanel;

	// Battle Panel
	Panel enemiesBattlePanel;
	Panel playerBattlePanel;
	Panel battleOptionsPanel;

	// Dialogue
	Panel dialoguePanel;

	public static boolean isBuying = false;

	public Entity entity;

	private static final UI Instance = new UI();

	public static UI getInstance() {
		return Instance;
	}

	int textSize = 11;

	public int charIndex = 0;
	public String combinedText = "";

	public static boolean turnOn = false;
	public Random rand = new Random();

    private UI() {
		titleImage = ResourceLoader.loadImage("/images/title/bahamut.png");
		gameOverImage = ResourceLoader.loadImage("/images/game over/over.png");
		titleCursor = ResourceLoader.loadImage("/images/ui/cursor.png");
		nullCursor = ResourceLoader.loadImage("/images/ui/nullCursor.png");
		maruMonica = ResourceLoader.loadFont("/fonts/MenuCard2.ttf");
		subState = 0;
		cursorPos = new Point();

		setMenuPanel();
	}

	public void draw(Graphics2D g2) {

	}

	// ================== TRADE ==================
	public void drawTradeScreen(Graphics2D g2){

		switch (subState){
			case 0 -> tradeSelect(g2);
			case 1 -> tradeBuy(g2);
			case 2 -> tradeSell(g2);
		}
	}

	public void tradeSelect(Graphics2D g2){

		entity.dialogueSet = 0;
		drawDialogueScreen(g2);

		// DRAW WINDOW
		int x = 0;
		int y = 0;
		int width = GameManager.tileSize * 3;
		int height = (int) (GameManager.tileSize * 3.5);
		drawSubWindow(g2, x, y, width, height);

		// DRAW TEXT
		x += GameManager.tileSize;
		y += GameManager.tileSize;
		g2.drawString("Buy", x, y);
		if (commandNumber == 0) {
			g2.drawImage(titleCursor, 0, y-20, 32, 32, null);
		}
		y += GameManager.tileSize;
		g2.drawString("Sell", x, y);
		if (commandNumber == 1) {
			g2.drawImage(titleCursor, 0, y-20, 32, 32, null);
		}
		y += GameManager.tileSize;
		g2.drawString("Leave", x, y);
		if (commandNumber == 2) {
			g2.drawImage(titleCursor, 0, y-24, 32, 32, null);
		}
	}

	public void tradeBuy(Graphics2D g2){

		isBuyTrade = true;
		// DRAW PLAYER INVENTORY
		drawInventoryScreen(g2, GameManager.getPlayer(), false);

		// DRAW NPC INVENTORY
		drawInventoryScreen(g2, entity, true);

		// DRAW HINT WINDOW
		int x = (int) (GameManager.tileSize * 2);
		int y = GameManager.tileSize * 9;
		int width = (int) (GameManager.tileSize * 6.5);
		int height = GameManager.tileSize * 2;
		drawSubWindow(g2, x, y, width, height);
		g2.drawString("[ESC] Back", x + 24, y + 60);

		// DRAW PLAYER COIN WINDOW
		x = GameManager.tileSize * 9;
		drawSubWindow(g2, x, y, width, height);
		g2.drawString(STR."Your Coin: \{GameManager.getPlayer().gold}", x + 24, y + 60);

		// DRAW PRICE WINDOW
		int i = getItemIndexFromSlot(itemIndex);

		if (i < entity.inventory.size()) {
			x = (int) (GameManager.tileSize * 4.5);
			y = (int) (GameManager.tileSize * 8.5);
			width = (int) (GameManager.tileSize * 3.5);
			height = GameManager.tileSize;
			drawSubWindow(g2, x, y, width, height);
			// g2.drawImage(coin, x + 10, y + 8, 32, 32, null);

			if(entity.inventory.get(i) != null){

				if(i < entity.inventory.size()){

					int price = entity.inventory.get(i).getPrice();
					String text = String.valueOf(price);
					x = ResourceLoader.getXForAlightToRightOfText(text, GameManager.tileSize * 8 - 70, g2);
					g2.drawString(STR."Preço: \{text}", x, y + 24);
				}

			}
		}
	}

	public void tradeSell(Graphics2D g2){

		isBuyTrade = false;
		// DRAW PLAYER INVENTORY
		drawInventoryScreen(g2, GameManager.getPlayer(), true);

		// DRAW HINT WINDOW
		int x = (int) (GameManager.tileSize * 2);
		int y = GameManager.tileSize * 9;
		int width = (int) (GameManager.tileSize * 6.5);
		int height = GameManager.tileSize * 2;
		drawSubWindow(g2, x, y, width, height);

		g2.drawString("[ESC] Back", x + 24, y + 60);

		// DRAW PLAYER COIN WINDOW
		x = GameManager.tileSize * 9;
		drawSubWindow(g2, x, y, width, height);
		g2.drawString(STR."Your Coin: \{GameManager.getPlayer().gold}", x + 24, y + 60);

		// DRAW PRICE WINDOW
		int i = getItemIndexFromSlot(itemIndex);

		if(GameManager.getPlayer().inventory != null){
			if (i >= 0 && i < GameManager.getPlayer().inventory.size()) {
				x = (int) (GameManager.tileSize * 12.5);
				y = (int) (GameManager.tileSize * 8.5);
				width = (int) (GameManager.tileSize * 2.5);
				height = GameManager.tileSize;
				//drawSubWindow(g2, x, y, width, height);
				//g2.drawImage(coin, x + 10, y + 8, 32, 32, null);

				if(GameManager.getPlayer().inventory.get(i) != null){
					int price = GameManager.getPlayer().inventory.get(i).getPrice() / 2;
					String text = String.valueOf(price);
					x = ResourceLoader.getXForAlightToRightOfText(text, GameManager.tileSize * 18 - 20, g2);
					g2.drawString(text, x, y + 34);
				}

			}
		}

	}
	// ================== TRADE ==================


	// ================== DIALOGUE ==================
	public void drawDialogueScreen(Graphics2D g2) {

		int margin = 20;
		int lineSpacing = 5;

		// Coordenadas e dimensões do painel de diálogo
		int panelX = dialoguePanel.getFrameX();
		int panelY = dialoguePanel.getFrameY();
		int panelWidth = dialoguePanel.getFrameWidth();
		int panelHeight = dialoguePanel.getFrameHeight();

		// Margem interna e área de texto
		int textX = panelX + margin;
		int textY = panelY + margin;
		int textWidth = panelWidth - 2 * margin;
		int textHeight = panelHeight - 2 * margin;

		// Desenhar o painel de diálogo
		dialoguePanel.drawPanel(g2);

		// Definir a fonte e cor do diálogo
		setColorAndFont(g2, Color.WHITE, Font.PLAIN, 12);

		// Debug
		// g2.drawRect(textX, textY, textWidth, textHeight);

		if(entity != null){
			if (entity.dialogues[entity.dialogueSet][entity.dialogueIndex] != null) {
				char[] chars = entity.dialogues[entity.dialogueSet][entity.dialogueIndex].toCharArray();

				if (charIndex < chars.length) {
					String s = String.valueOf(chars[charIndex]);
					combinedText = combinedText + s;
					currentDialogue = combinedText;
					GameManager.playSFX(21);
					charIndex++;
				}
			}
		}

		List<String> lines = wrapText(g2, currentDialogue, textWidth);

		int y = textY + 20;
		for (String line : lines) {
			g2.drawString(line, textX, y);
			y += g2.getFontMetrics().getHeight() + lineSpacing; // Adicionar espaçamento entre as linhas
			if (y > textY + textHeight) {
				break;
			}
		}
	}

	private ArrayList<String> wrapText(Graphics2D g2, String text, int maxWidth) {
		ArrayList<String> lines = new ArrayList<>();
		String[] words = text.split(" ");
		StringBuilder line = new StringBuilder();

		for (String word : words) {
			if (line.isEmpty()) {
				line.append(word);
			} else {
				String testLine = STR."\{line.toString()} \{word}";
				int lineWidth = g2.getFontMetrics().stringWidth(testLine);
				if (lineWidth < maxWidth) {
					line.append(" ").append(word);
				} else {
					lines.add(line.toString());
					line = new StringBuilder(word);
				}
			}
		}

		if (!line.isEmpty()) {
			lines.add(line.toString());
		}

		return lines;
	}

	public void drawDialogueSaveScreen(Graphics2D g2){

		int x = 0;
		int y = 0;

		dialoguePanel.drawPanel(g2);

		char chars[] = currentDialogue.toCharArray();

		// Verifique se ainda há caracteres para adicionar
		if (charIndex < chars.length) {
			// Adicione o próximo caractere à string acumulada
			String s = String.valueOf(chars[charIndex]);
			combinedText = combinedText + s;
			currentDialogue = combinedText;

			// Reproduza o efeito sonoro
			GameManager.playSFX(21);

			// Incremente o índice do caractere
			charIndex++;
		}

		// FONT DIALOGUE
		setColorAndFont(g2, Color.WHITE, Font.PLAIN, 12);
		x += (dialoguePanel.getFrameX() + GameManager.tileSize);
		y += (dialoguePanel.getFrameY() + GameManager.tileSize);

		for(String line : currentDialogue.split("\n")){
			g2.drawString(line, x, y);
			y += 40;
		}
	}
	// ================== DIALOGUE ==================


	// ================== BATTLE ==================
	public void drawBattlePanel(Graphics2D g2){

		String text = "";

		setColorAndFont(g2, Color.white, Font.PLAIN, 12);
		enemiesBattlePanel.drawPanel(g2);

		int yTextNameEnemy = GameManager.tileSize * 9;

		for(Enemy e : enemies){
			if(e != null){
				text = STR."\{e.name}";
				g2.drawString(text, enemiesBattlePanel.getFrameX() + 32, yTextNameEnemy + 32);
				yTextNameEnemy += 25;
			}
		}

		int x = 0;
		int yTextPlayerName = playerBattlePanel.getFrameY();
		int yBar = playerBattlePanel.getFrameY();
		x += enemiesBattlePanel.getFrameWidth();

		// Player Panel
		playerBattlePanel.setFrameX(x);

		playerBattlePanel.drawPanel(g2);

		for (int i = 0; i < GameManager.players.size(); i++){
			if(GameManager.players.get(i) != null){
				text = STR."\{GameManager.players.get(i).name}\{GameManager.players.get(i).hp}/\{GameManager.players.get(i).maxHp}";

				if(i == currentPlayerTurn){
					setColorAndFont(g2, Color.gray, Font.PLAIN, 12);
				}
				else{
					setColorAndFont(g2, Color.white, Font.PLAIN, 12);
				}

				g2.drawString(text, x + 16, yTextPlayerName + 32);
				yTextPlayerName += 25;

				/*int cooldownBarWidth = 75;

				// Calcule o progresso do cooldown
				int currentCooldown = GameManager.getPlayerAtIndex(i).attackCooldown;
				int minCooldown = GameManager.getPlayerAtIndex(i).minCooldown;
				double cooldownProgress = 1.0 - (double) currentCooldown / minCooldown;
				int currentWidth = (int) (cooldownProgress);

				if(currentWidth > cooldownBarWidth){
					currentWidth = cooldownBarWidth;
					currentPlayerTurn = i;
					GameManager.getPlayerAtIndex(i).turnOn = true;
				}



				g2.setColor(Color.WHITE);
                g2.drawRect(x + GameManager.tileSize * 5, yBar + 24, cooldownBarWidth, 10); // Barra de fundo
				g2.setColor(Color.WHITE);
				g2.fillRect(x + GameManager.tileSize * 5, yBar + 24, currentWidth, 10); // Barra de progresso

				yBar += 25;*/

			}

		}
	}

	public void drawEnemySelection(Graphics2D g2, int x, int y){
		if (monsterIndex == 0){
			g2.drawImage(titleCursor, x, y+20, 32, 32, null);
		}
		else if(monsterIndex == 1){
			g2.drawImage(titleCursor, x, y+40, 32, 32, null);
		}
		else if(monsterIndex == 2){
			g2.drawImage(titleCursor, x, y+60, 32, 32, null);
		}
	}

	public void drawPlayerSelection(Graphics2D g2, int x, int y){
		if (playerIndex == 0){
			g2.drawImage(titleCursor, x, y+20, 32, 32, null);
		}
		else if(playerIndex == 1){
			g2.drawImage(titleCursor, x, y+40, 32, 32, null);
		}
		else if(playerIndex == 2){
			g2.drawImage(titleCursor, x, y+60, 32, 32, null);
		}
	}

	public void drawBattlePanelOptions(Graphics2D g2){

		int x = 0;
		int y = 0;
		String text = "";

		battleOptionsPanel.drawPanel(g2);

		// ATTACK
		setColorAndFont(g2, Color.DARK_GRAY, Font.PLAIN, 12);

		text = "Attack";
		x += (battleOptionsPanel.getFrameX() + 32);
		y += (battleOptionsPanel.getFrameY() + 26);

		if (battleIndex == 0){
			g2.setColor(Color.white);
			g2.drawImage(titleCursor, x-GameManager.tileSize, y-20, 32, 32, null);
		}

		g2.drawString(text, x, y);

		// MAGIC
		setColorAndFont(g2, Color.DARK_GRAY, Font.PLAIN, 12);
		text = "Magic";

		y += GameManager.tileSize;
		if (battleIndex == 1){
			g2.setColor(Color.white);
			g2.drawImage(titleCursor, x - GameManager.tileSize, y-20, 32, 32, null);
		}

		g2.drawString(text, x, y);

		// ITEMS
		setColorAndFont(g2, Color.DARK_GRAY, Font.PLAIN, 12);
		text = "Items";
;
		y += GameManager.tileSize;

		if (battleIndex == 2){
			g2.setColor(Color.white);
			g2.drawImage(titleCursor, x- GameManager.tileSize, y-20, 32, 32, null);
		}

		g2.drawString(text, x, y);

		// RUN
		setColorAndFont(g2, Color.DARK_GRAY, Font.PLAIN, 12);
		text = "Run";

		y += GameManager.tileSize;

		if (battleIndex == 3){
			g2.setColor(Color.white);
			g2.drawImage(titleCursor, x- GameManager.tileSize, y-20, 32, 32, null);
		}

		g2.drawString(text, x, y);
	}
	// ================== BATTLE ==================


	// ================== PAUSE ==================
	public void drawPauseScreen(Graphics2D g2){
		String text = "PAUSE";

		g2.setFont(maruMonica);
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 36F));
		int x = getXCenterText(g2, text);
		int y = GameManager.screenHeight /2;

		g2.drawString(text, x, y);
	}
	// ================== PAUSE ==================


	// ==================== TITLE ==========================
	public void drawTitleTransition(Graphics2D g2){
		alphaCounter++;
		g2.setColor(new Color(0, 0, 0, alphaCounter*5));
		g2.fillRect(0, 0, GameManager.screenWidth, GameManager.screenHeight);

		if(alphaCounter == 50){
			alphaCounter = 0;

			if(GameManager.cutsceneManager.getWatchedCutscenes().isEmpty()){
				StateMachine.getInstance().change("Opening");
			}
			else{
				StateMachine.getInstance().change("InGame");
			}

		}
	}

	public void drawTitleScreen(Graphics2D g2) {
		// Desenhar a imagem do título preenchendo a tela
		g2.drawImage(titleImage, 0, 0, GameManager.screenWidth, GameManager.screenHeight, null);

		// Configurar a fonte para o título
		g2.setFont(maruMonica);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, GameManager.screenWidth / 20)); // Ajuste proporcional da fonte

		g2.setColor(Color.blue);
		String text = "The Chronicles of Bahamut";
		int x = UI.getInstance().getXCenterText(g2, text);
		int y = (int) (GameManager.screenHeight / 3.5); // Ajustar a posição do título para mais abaixo

		g2.drawString(text, x, y);
		y += GameManager.tileSize * 2;

		// Configurar a fonte para as opções de menu
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, GameManager.screenWidth / 40)); // Ajuste proporcional da fonte
		int menuSpacing = GameManager.screenHeight / 10; // Aumentar o espaçamento entre os itens do menu

		// NEW GAME
		text = "New Game";
		x = UI.getInstance().getXCenterText(g2, text);
		y += menuSpacing;

		if (titleIndex == 0) {
			g2.drawImage(titleCursor, x - GameManager.tileSize, y - 20, 32, 32, null);
		}

		g2.drawString(text, x, y);

		// LOAD GAME
		text = "Load Game";
		x = UI.getInstance().getXCenterText(g2, text);
		y += menuSpacing;

		if (titleIndex == 1) {
			g2.drawImage(titleCursor, x - GameManager.tileSize, y - 20, 32, 32, null);
		}

		g2.drawString(text, x, y);

		// QUIT
		text = "Quit";
		x = UI.getInstance().getXCenterText(g2, text);
		y += menuSpacing;

		if (titleIndex == 2) {
			g2.drawImage(titleCursor, x - GameManager.tileSize, y - 20, 32, 32, null);
		}

		g2.drawString(text, x, y);
	}
	// ==================== TITLE ==========================


	// ==================== GAME OVER ==========================
	public void drawGameOverScreen(Graphics2D g2){

		g2.drawImage(gameOverImage, 0, 0, GameManager.screenWidth, GameManager.screenHeight, null);

		g2.setFont(UI.getInstance().maruMonica);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));

		g2.setColor(Color.WHITE);
		String text = "Game Over";
		int x = UI.getInstance().getXCenterText(g2, text);
		int y = GameManager.tileSize * 4;

		g2.drawString(text, x, y);
	}
	// ==================== GAME OVER ==========================


	//  =============== CHAR SCREEN ==========================
	public void drawStatsScreen(Graphics2D g2) {
		statsCharPanel.drawPanel(g2);
		drawStatsText(g2);
		drawValues(g2);
	}

	private void drawStatsText(Graphics2D g2) {

		int textX = statsCharPanel.getFrameX() + 20;
		int textY = statsCharPanel.getFrameY() + GameManager.tileSize;
		int lineHeight = 21;

		setColorAndFont(g2, Color.WHITE, Font.PLAIN, 12);

		g2.drawString("Lvl", textX, textY);
		textY += lineHeight;
		g2.drawString("Life", textX, textY);
		textY += lineHeight;
		g2.drawString("Mana", textX, textY);
		textY += lineHeight;
		g2.drawString("Strength", textX, textY);
		textY += lineHeight;
		g2.drawString("Dexterity", textX, textY);
		textY += lineHeight;
		g2.drawString("Attack", textX, textY);
		textY += lineHeight;
		g2.drawString("Defense", textX, textY);
		textY += lineHeight;
		g2.drawString("Exp", textX, textY);
		textY += lineHeight;
		g2.drawString("Nxt Lvl", textX, textY);
		textY += lineHeight;
		g2.drawString("Weapon", textX, textY);
		textY += lineHeight;
		g2.drawString("Shield", textX, textY);
	}

	private void drawValues(Graphics2D g2) {
		int textX;
		String value;
		int tailX = (statsCharPanel.getFrameX() + statsCharPanel.getFrameWidth()) - 10;
		int textY = statsCharPanel.getFrameY() + GameManager.tileSize;
		int lineHeight = 21;

		value = String.valueOf(GameManager.getPlayerAtIndex(playerMenuIndex).level);
		textX = ResourceLoader.getXForAlightToRightOfText(value, tailX, g2);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = STR."\{GameManager.getPlayerAtIndex(playerMenuIndex).hp}/\{GameManager.getPlayerAtIndex(playerMenuIndex).maxHp}";
		textX = ResourceLoader.getXForAlightToRightOfText(value, tailX, g2);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = GameManager.getPlayerAtIndex(playerMenuIndex).mana + "/" + GameManager.getPlayer().maxMana;
		textX = ResourceLoader.getXForAlightToRightOfText(value, tailX, g2);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(GameManager.getPlayerAtIndex(playerMenuIndex).strength);
		textX = ResourceLoader.getXForAlightToRightOfText(value, tailX, g2);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(GameManager.getPlayerAtIndex(playerMenuIndex).dexterity);
		textX = ResourceLoader.getXForAlightToRightOfText(value, tailX, g2);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(GameManager.getPlayerAtIndex(playerMenuIndex).getAttack());
		textX = ResourceLoader.getXForAlightToRightOfText(value, tailX, g2);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(GameManager.getPlayerAtIndex(playerMenuIndex).getDefense());
		textX = ResourceLoader.getXForAlightToRightOfText(value, tailX, g2);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(GameManager.getPlayerAtIndex(playerMenuIndex).exp);
		textX = ResourceLoader.getXForAlightToRightOfText(value, tailX, g2);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(GameManager.getPlayerAtIndex(playerMenuIndex).nextLevelExp);
		textX = ResourceLoader.getXForAlightToRightOfText(value, tailX, g2);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		if(GameManager.getPlayerAtIndex(playerMenuIndex).currentWeapon != null){
			g2.drawImage(GameManager.getPlayerAtIndex(playerMenuIndex).currentWeapon.getImage(), tailX - 20, textY-15, null);
			textY += GameManager.tileSize;
		}
		else{
			textY += GameManager.tileSize;
		}

		if(GameManager.getPlayerAtIndex(playerMenuIndex).currentShield != null){
			g2.drawImage(GameManager.getPlayerAtIndex(playerMenuIndex).currentShield.getImage(), tailX - 20, textY-15, null);
		}
	}
	//  =============== CHAR SCREEN ==========================


	// ==================== IN-GAME MENU ===============================
	public void drawInGameMenu(Graphics2D g2) {

		switch(subState){
			case 0 -> menuSelect(g2);
			case 1 -> drawInventoryScreen(g2, GameManager.getPlayer(), true);
		}
	}

	public void menuSelect(Graphics2D g2){
		drawMenuPanel(g2);
		drawHeaderPanel(g2, "Menu");
		drawGoldAndTimePanel(g2);
		drawGoldAndTimePanel(g2);
		drawPlayerPortrait(g2);
	}

	public void drawMenuPanel(Graphics2D g2){
		menuPanel.drawPanel(g2);
		drawMenuText(g2);
	}

	public void drawHeaderPanel(Graphics2D g2, String titleHeader){
		headerMenuPanel.drawPanel(g2);
		drawHeaderText(g2, titleHeader);
	}

	public void drawGoldAndTimePanel(Graphics2D g2){
		goldAndTimePanel.drawPanel(g2);
		drawGoldAndTimeText(g2);
		goldAndTimeValues(g2);
	}

	public void drawPlayerPortrait(Graphics2D g2) {
		String value = "";
		int margin = 10;
		int portraitX = playerPanel.getFrameX() + margin;
		int portraitY = playerPanel.getFrameY() + margin;
		int portraitWidth = GameManager.getPlayer().portrait.getWidth() / 2;
		int portraitHeight = GameManager.getPlayer().portrait.getHeight() / 2;
		int lineHeight = 15;

		int textPlayerNameX = portraitX + portraitWidth + margin;
		int textPlayerNameY = portraitY;

		setColorAndFont(g2, Color.WHITE, Font.PLAIN, 12);
		playerPanel.drawPanel(g2);

		for (int i = 0; i < GameManager.getPlayers().size(); i++) {
			// Desenha o retrato do jogador
			g2.drawImage(GameManager.getPlayerAtIndex(i).portrait, portraitX, portraitY, portraitWidth, portraitHeight, null);

			// Define a posição inicial do texto para este jogador
			textPlayerNameY = portraitY + 14;

			// Desenha o nome do jogador
			value = GameManager.getPlayerAtIndex(i).name;
			g2.drawString(value, textPlayerNameX, textPlayerNameY);
			textPlayerNameY += lineHeight;

			// Desenha o nível do jogador
			value = STR."Level: \{GameManager.getPlayerAtIndex(i).level}";
			g2.drawString(value, textPlayerNameX, textPlayerNameY);
			textPlayerNameY += lineHeight;

			// Desenha os pontos de vida do jogador
			value = STR."HP: \{GameManager.getPlayerAtIndex(i).hp}";
			g2.drawString(value, textPlayerNameX, textPlayerNameY);
			textPlayerNameY += lineHeight;

			// Desenha os pontos de mana do jogador
			value = STR."Mana: \{GameManager.getPlayerAtIndex(i).mana}";
			g2.drawString(value, textPlayerNameX, textPlayerNameY);

			// Incrementa a posição Y para o próximo jogador
			portraitY += portraitHeight + margin;
		}
	}

	public void drawHeaderText(Graphics2D g2, String titleHeader){
		String value = "";
		int textX = headerMenuPanel.getFrameWidth()/2 + 20;
		int textY = headerMenuPanel.getFrameY() + (GameManager.tileSize - 12);

		value = titleHeader;

		setColorAndFont(g2, Color.WHITE, Font.PLAIN, 12);
		g2.drawString(value, textX, textY);
	}

	public void drawMenuText(Graphics2D g2){

		String value = "";
		int textX = menuPanel.getFrameX() + 15;
		int textY = menuPanel.getFrameY() + GameManager.tileSize;
		int lineHeight = 32;

		setColorAndFont(g2, Color.WHITE, Font.PLAIN, 12);

		value = "Items";
		g2.drawString(value, textX, textY);

		if (commandNumber == 0 && !PlayerSelectMenuState.isPlayerSelected) {
			g2.drawImage(titleCursor, textX - GameManager.tileSize, textY-lineHeight + (GameManager.tileSize/2), 32, 32, null);
		}

		textY += lineHeight;

		value = "Magic";
		g2.drawString(value, textX, textY);

		if (commandNumber == 1 && !PlayerSelectMenuState.isPlayerSelected) {
			g2.drawImage(titleCursor,  textX - GameManager.tileSize, textY-lineHeight + (GameManager.tileSize/2), 32, 32, null);
		}
		textY += lineHeight;

		value = "Equip/Stats";
		g2.drawString(value, textX, textY);

		if (commandNumber == 2 && !PlayerSelectMenuState.isPlayerSelected) {
			g2.drawImage(titleCursor, textX - GameManager.tileSize, textY-lineHeight + (GameManager.tileSize/2), 32, 32, null);
		}

		textY += lineHeight;

		if(GameManager.canSave || GameManager.currentMap == 0){
			setColorAndFont(g2, Color.WHITE, Font.PLAIN, 12);
		}
		else{
			setColorAndFont(g2, Color.DARK_GRAY, Font.PLAIN, 12);
		}

		value = "Save";
		g2.drawString(value, textX, textY);

		if (commandNumber == 3 && !PlayerSelectMenuState.isPlayerSelected) {
			g2.drawImage(titleCursor, textX - GameManager.tileSize, textY-lineHeight + (GameManager.tileSize/2), 32, 32, null);
		}
	}

	public void drawGoldAndTimeText(Graphics2D g2){

		String value = "";
		int textX = goldAndTimePanel.getFrameX() + 20;
		int textY = goldAndTimePanel.getFrameY() + GameManager.tileSize;
		int lineHeight = 32;

		setColorAndFont(g2, Color.WHITE, Font.PLAIN, 12);

		value = "Gold: ";
		g2.drawString(value, textX, textY);

		textY += lineHeight;

		value = "Time: ";
		g2.drawString(value, textX, textY);
	}

	public void goldAndTimeValues(Graphics2D g2){
		int textX;
		String value;
		int tailX = (goldAndTimePanel.getFrameX() + goldAndTimePanel.getFrameWidth()) - 10;
		int textY = goldAndTimePanel.getFrameY() + GameManager.tileSize;
		int lineHeight = 32;

		setColorAndFont(g2, Color.WHITE, Font.PLAIN, 12);

		value = String.valueOf(GameManager.getPlayer().gold);
		textX = ResourceLoader.getXForAlightToRightOfText(value, tailX, g2);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = GameManager.getFormattedElapsedTime();
		textX = ResourceLoader.getXForAlightToRightOfText(value, tailX, g2);
		g2.drawString(value, textX, textY);
	}
	// ==================== IN-GAME MENU ===============================


	// ================== INVENTORY ====================================
	public void drawInventoryInBattleScreen(Graphics2D g2, Entity entity){
		int frameX = 0;
		int frameY = 0;
		int frameWidth = 0;
		int frameHeight = 0;
		int slotCol = 0;
		int slotRow = 0;

		slotCol = playerSlotCol;
		slotRow = playerSlotRow;

		frameX = playerBattlePanel.getFrameX();
		frameY = playerBattlePanel.getFrameY();
		frameWidth = playerBattlePanel.getFrameWidth();
		frameHeight = playerBattlePanel.getFrameHeight();

		drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);
		Rectangle rect = new Rectangle(frameX, frameY, frameWidth, frameHeight);
		g2.drawRect(rect.x, rect.y, rect.width, rect.height);

		// ITEM SLOTS
		final int slotXStart = frameX + 10;
		final int slotYStart = frameY + 10;
		int slotX = slotXStart;
		int slotY = slotYStart;
		int slotSize = GameManager.getTileSize() + 3;

		// CURSOR selection box
		int cursorX = slotXStart + (slotSize * slotCol);
		cursorY = slotYStart + (slotSize * slotRow) ;

		FixedSizeList<Entity> inventory = entity.getInventory();

		drawItemsInInventory(g2, entity, rect, slotX, inventory, cursorX, cursorY, true, scrollPosition);
	}

	public void drawInventoryScreen(Graphics2D g2, Entity entity, boolean cursor) {

		int frameX = 0;
		int frameY = 0;
		int frameWidth = 0;
		int frameHeight = 0;
		int slotCol = 0;
		int slotRow = 0;

		// ITEM FRAME BOX
		if (entity == GameManager.getPlayer()) {

			if(isBuyTrade || isSellTrade){
				frameX = GameManager.getTileSize() * 9;
				frameY = GameManager.getTileSize();
			}
			else{
				frameX = menuPanel.getFrameX();
				frameY = menuPanel.getFrameY();
			}

			slotCol = playerSlotCol;
			slotRow = playerSlotRow;

			frameWidth = (int) (GameManager.getTileSize() * 6.5);
			frameHeight = GameManager.getTileSize() * 5;

			drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);
			Rectangle rect = new Rectangle(frameX, frameY, frameWidth, frameHeight);
			g2.drawRect(rect.x, rect.y, rect.width, rect.height);

			// ITEM SLOTS
			final int slotXStart = frameX + 10;
			final int slotYStart = frameY + 10;
			int slotX = slotXStart;
			int slotY = slotYStart;
			int slotSize = GameManager.getTileSize() + 3;

			// CURSOR selection box
			int cursorX = slotXStart + (slotSize * slotCol);
			cursorY = slotYStart + (slotSize * slotRow) ;

			FixedSizeList<Entity> inventory = entity.getInventory();

			drawItemsInInventory(g2, entity, rect, slotX, inventory, cursorX, cursorY, cursor, scrollPosition);

			if (cursor) {
				// drawSelectionBox(g2, slotXStart, slotYStart, slotSize, slotCol, slotRow);

				// DESCRIPTION FRAME
				int descriptionFrameX = frameX;
				int descriptionFrameY = frameY + frameHeight;
				int descriptionFrameWidth = frameWidth;
				int descriptionFrameHeight = GameManager.getTileSize() * 3;

				drawItemDescriptionText(g2, inventory, descriptionFrameX, descriptionFrameY, descriptionFrameWidth, descriptionFrameHeight, itemIndex);
			}

		} else {
			frameX = GameManager.getTileSize() * 2;
			slotCol = npcSlotCol;
			slotRow = npcSlotRow;

			frameY = GameManager.getTileSize();
			frameWidth = (int) (GameManager.getTileSize() * 6.5);
			frameHeight = GameManager.getTileSize() * 5;

			drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);
			Rectangle rect = new Rectangle(frameX, frameY, frameWidth, frameHeight);
			g2.drawRect(rect.x, rect.y, rect.width, rect.height);

			// ITEM SLOTS
			final int slotXStart = frameX + 10;
			final int slotYStart = frameY + 10;
			int slotX = slotXStart;
			int slotY = slotYStart;
			int slotSize = GameManager.getTileSize() + 3;

			// CURSOR selection box
			int cursorX = slotXStart + (slotSize * slotCol);
			cursorY = slotYStart + (slotSize * slotRow) ;

			FixedSizeList<Entity> inventory = entity.getInventory();

			drawItemsInInventory(g2, entity, rect, slotX, inventory, cursorX, cursorY, cursor, npcScrollPosition);

			if (cursor) {
				// drawSelectionBox(g2, slotXStart, slotYStart, slotSize, slotCol, slotRow);

				// DESCRIPTION FRAME
				int descriptionFrameX = frameX;
				int descriptionFrameY = frameY + frameHeight;
				int descriptionFrameWidth = frameWidth;
				int descriptionFrameHeight = GameManager.getTileSize() * 3;

				drawItemDescriptionText(g2, inventory, descriptionFrameX, descriptionFrameY, descriptionFrameWidth, descriptionFrameHeight, npcItemIndex);
			}
		}
	}

	private void drawItemsInInventory(Graphics2D g2, Entity entity, Rectangle rect, int slotX, FixedSizeList<Entity> inventory, int cursorX, int cursorY, boolean cursor, int scrollPosition) {
		// Definir os limites do retângulo visível
		int rectangleX = rect.x;
		int rectangleY = rect.y;
		int rectangleWidth = rect.width; // Ajuste conforme necessário
		int rectangleHeight = rect.height; // Ajuste conforme necessário
		Rectangle visibleRect = new Rectangle(rectangleX, rectangleY, rectangleWidth, rectangleHeight);

		// Desenhar o retângulo visível (para depuração)
		//g2.setColor(Color.RED);

		// g2.drawRect(visibleRect.x, visibleRect.y, visibleRect.width, visibleRect.height);

		// Desenhar os itens de inventário dentro do retângulo visível
		for (int i = scrollPosition; i < Math.min(scrollPosition + visibleItems, totalItems); i++) {
			int itemPosY = rectangleY + (i - scrollPosition) * itemHeight;

			// Verificar se o item está dentro dos limites verticais do retângulo visível
			if (itemPosY >= visibleRect.y && itemPosY < visibleRect.y + visibleRect.height) {

				if(i < inventory.size()){

					if(inventory.get(i) != null){

						Asset object = inventory.get(i);
						g2.drawImage(object.getImage(), slotX, itemPosY + 16, null);

						drawItemName(g2, inventory, i, slotX, itemPosY);
						drawItemAmount(g2, entity, inventory, i, slotX, itemPosY);
					}

				}

			}
		}

		if(isBuyTrade && entity != GameManager.getPlayer()){

			g2.drawImage(titleCursor, cursorX - 10, cursorY, 22, 22, null);
			drawShowBar(g2, rect, npcScrollPosition);
		}

		if(isStatusItem || isBattleItem || isInventoryScreen || isSellTrade && !isBuyTrade && entity == GameManager.getPlayer()){

			g2.drawImage(titleCursor, cursorX - 10, cursorY, 22, 22, null);
			drawShowBar(g2, rect, scrollPosition);
		}
	}

	private void drawItemAmount(Graphics2D g2, Entity entity, FixedSizeList<Entity> inventory, int index, int itemPosX, int itemPosY){
		if(entity == GameManager.getPlayer()){

			String s = STR." x\{inventory.get(index).getAmount()}";
			setColorAndFont(g2, Color.WHITE, Font.PLAIN, 9);
			int amountX = ResourceLoader.getXForAlightToRightOfText(s, itemPosX, g2) + 28;
			int amountY = itemPosY + GameManager.getTileSize() + 12;

			g2.drawString(s, amountX, amountY);
		}
	}

	private void drawItemName(Graphics2D g2, FixedSizeList<Entity> inventory, int index, int itemPosX, int itemPosY){
		String s = STR." \{inventory.get(index).getName()}";

		String nameState = StateMachine.getInstance().getCurrentState().getName();

		if(Objects.equals(nameState, "Inventory") || Objects.equals(nameState, "PlayerSelect")){
			if(inventory.get(index).entityType == EntityType.CONSUMABLE){
				setColorAndFont(g2, Color.WHITE, Font.PLAIN, 12);
			}
			else{
				setColorAndFont(g2, Color.DARK_GRAY, Font.PLAIN, 12);
			}
		}
		else if(Objects.equals(nameState, "StatusScreen") || Objects.equals(nameState, "PlayerSelect")){
			if(inventory.get(index).entityType == EntityType.CONSUMABLE){
				setColorAndFont(g2, Color.DARK_GRAY, Font.PLAIN, 12);
			}
			else{
				setColorAndFont(g2, Color.WHITE, Font.PLAIN, 12);
			}
		}

		int amountX = itemPosX + 26;
		int amountY = itemPosY + GameManager.getTileSize();

		setColorAndFont(g2, Color.WHITE, Font.PLAIN, 12);
		g2.drawString(s, amountX, amountY);
	}

	private void drawShowBar(Graphics2D g2, Rectangle rectangle, int scrollPosition){

		if(scrollPosition >= 1){
			// Desenhar a barra de rolagem
			int scrollbarWidth = 20; // Largura da barra de rolagem
			int scrollbarX = rectangle.x + rectangle.width - 20; // Ajuste conforme necessário

			g2.setColor(Color.DARK_GRAY);
			g2.fillRect(scrollbarX, rectangle.y, scrollbarWidth, rectangle.height);


		/*// Adicionar log para depuração
		System.out.println("Scrollbar position: X=" + scrollbarX + ", Y=" + scrollbarY);
		System.out.println("Scrollbar dimensions: Width=" + scrollbarWidth + ", Height=" + scrollbarHeight);*/

			// Desenhar a alça da barra de rolagem
			int handleHeight = (rectangle.height) / totalItems;
			int handleY = (scrollPosition * (rectangle.height - handleHeight)) / (totalItems - visibleItems);

			handleY = Math.max(handleY, 0); // Limitar handleY ao valor mínimo de 0
			handleY = Math.min(handleY, rectangle.height - handleHeight);

			g2.setColor(Color.LIGHT_GRAY);
			g2.fillRect(scrollbarX, rectangle.y + handleY, scrollbarWidth, handleHeight);

		/*// Adicionar log para depuração da alça da barra de rolagem
		System.out.println("Handle position: Y=" + (scrollbarY + handleY) + ", Height=" + handleHeight);*/
		}
	}

	private void drawItemDescriptionText(Graphics2D g2, FixedSizeList<Entity> inventory, int descriptionFrameX, int descriptionFrameY, int descriptionFrameWidth, int descriptionFrameHeight, int itemIndex) {
		// DRAW DESCRIPTION TEXT
		int textX = descriptionFrameX + 18;
		int textY = descriptionFrameY + 24;

		setColorAndFont(g2, Color.WHITE, Font.PLAIN, 12);

		int i = getItemIndexFromSlot(itemIndex);

		if (i >= 0 && i < inventory.size()) {

			if(inventory.get(i) != null){
				drawSubWindow(g2, descriptionFrameX, descriptionFrameY, descriptionFrameWidth, descriptionFrameHeight);

				List<String> lines = wrapText(g2, inventory.get(i).getDescription(), descriptionFrameWidth - GameManager.tileSize);

				for (String line : lines) {
					g2.drawString(line, textX, textY);
					textY += 20;
				}
			}
		}
		else{
			g2.drawString("", textX, textY);
		}


	}
	// ================== INVENTORY ====================================


	// ================== TOOLS ===========================
	public int getXCenterText(Graphics2D g2, String text){
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		return GameManager.screenWidth/2 - length/2;
	}

	public int getYCenterText(Graphics2D g2, String text){
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getHeight();
		return GameManager.screenHeight/2 - length/2;
	}

	public int getItemIndexFromSlot(int row) {
        if (row < 0 || row >= GameManager.getPlayer().inventory.size()) {
			return -1; // Retorna -1 se o índice for inválido
		}
		return row;
	}

	public void setColorAndFont(Graphics2D g2, Color color, int font, int size){
		g2.setFont(maruMonica);
		g2.setColor(color);
		g2.setFont(g2.getFont().deriveFont(font, size));
	}

	public void scrollUp() {
		if (scrollPosition > 0) {
			scrollPosition--;
		}
	}

	public void scrollDown() {
		if (scrollPosition <= totalItems) {
			scrollPosition++;
		}
	}

	public void npcScrollDown() {
		if (npcScrollPosition <= totalItems) {
			npcScrollPosition++;
		}
	}

	public void npcScrollUp() {
		if (npcScrollPosition > 0) {
			npcScrollPosition--;
		}
	}

	public void drawSubWindow(Graphics2D g2, int x, int y, int width, int height) {
		Color color = new Color(0, 0, 0, 100);
		g2.setColor(color);
		g2.fillRoundRect(x, y, width, height, 10, 10);

		color = new Color(255, 255, 255);
		g2.setColor(color);
		g2.setStroke(new BasicStroke(2));
		g2.drawRoundRect(x , y, width, height, 10, 10);
	}

	public void setMenuPanel(){

		playerPanel = new Panel(
				(int) (GameManager.tileSize * 8.5),
				(int) (GameManager.tileSize * 2.3),
				(int) (GameManager.tileSize * 5.5),
				GameManager.tileSize * 8
		);

		statsCharPanel = new Panel(
				(int) (GameManager.tileSize * 8.5),
				(int) (GameManager.tileSize * 2.3),
				(int) (GameManager.tileSize * 5.5),
				GameManager.tileSize * 8
		);

		headerMenuPanel = new Panel(
				GameManager.tileSize * 2,
                (int) (GameManager.tileSize * 1.3),
                (int) (GameManager.tileSize  * 12),
				GameManager.tileSize);

		menuPanel = new Panel(
				GameManager.tileSize * 2,
				(int) (GameManager.tileSize * 2.3),
                (int) (GameManager.tileSize * 6.5),
				GameManager.tileSize * 5
		);

		goldAndTimePanel = new Panel(
				GameManager.tileSize * 2,
				(int) (GameManager.tileSize * 7.3),
                (int) (GameManager.tileSize * 6.5),
				(int) (GameManager.tileSize * 3)
		);

		enemiesBattlePanel = new Panel(0,
				GameManager.tileSize *9,
				GameManager.screenWidth - (GameManager.tileSize *8),
				GameManager.tileSize *3);

		playerBattlePanel = new Panel(0,
				GameManager.tileSize *9,
				GameManager.screenWidth - (GameManager.tileSize *8),
				GameManager.tileSize *3);

		battleOptionsPanel = new Panel(
				GameManager.tileSize *2,
				GameManager.tileSize *8,
				GameManager.screenWidth - (GameManager.tileSize *10),
				GameManager.tileSize *4
		);

		dialoguePanel = new Panel(
				GameManager.tileSize *2,
				GameManager.tileSize *8,
				GameManager.screenWidth - (GameManager.tileSize *4),
				GameManager.tileSize *4);
	}

	public Panel getDialoguePanel(){
		return dialoguePanel;
	}
	// ================== TOOLS ===========================
}
