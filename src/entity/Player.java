package entity;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Objects;

import cutscene_scripts.NotPassCutscene;
import entity.npcs.NPC;
import main.GameManager;
import main.InputManager;
import object.*;
import object.Object;
import object.consumables.OBJ_Health_Potion;
import object.consumables.OBJ_Key;
import quest.BlacksmithQuestObjective;
import quest.Quest;
import quest.QuestEnum;

public class Player extends Entity {

	public final Rectangle TOP_LIMIT;
	public final Rectangle BOTTOM_LIMIT;
	public final Rectangle LEFT_LIMIT;
	public final Rectangle RIGHT_LIMIT;

	public boolean isWalking = false;

	public Player(Point battlePosition) {
		super();
		battlePos = battlePosition;
		entityType = EntityType.PLAYER;

		screenX = GameManager.screenWidth / 2 - (GameManager.tileSize / 2);
		screenY = GameManager.screenHeight / 2 - (GameManager.tileSize / 2);

		loadPlayerAnimation("/images/characters/heroes/player.png");

		TOP_LIMIT = new Rectangle(screenX,
				screenY, GameManager.tileSize, 1);

		BOTTOM_LIMIT = new Rectangle(screenX,
				screenY + GameManager.tileSize, GameManager.tileSize, 1);

		LEFT_LIMIT = new Rectangle(screenX,
				screenY, 1, GameManager.tileSize);

		RIGHT_LIMIT = new Rectangle(screenX + GameManager.tileSize,
				screenY, 1, GameManager.tileSize);

		solidArea = new Rectangle();
		solidArea.x = 0;
		solidArea.y = 0;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 32;
		solidArea.height = 32;

		setDefaultValues();
		setItems();
	}

	public void update() {
		final int speedX = evaluateSpeedX();
		final int speedY = evaluateSpeedY();
		animation.update();

		if(Objects.equals(direction, "up")){
			animation = walkUp;
			animation.start();
		} else if (Objects.equals(direction, "left")) {
			animation = walkLeft;
			animation.start();
		} else if (Objects.equals(direction, "right")) {
			animation = walkRight;
			animation.start();
		} else if (Objects.equals(direction, "down")) {
			animation = walkDown;
			animation.start();
		}

		if (InputManager.getInstance().isKeyHeld(KeyEvent.VK_UP) ||
				InputManager.getInstance().isKeyHeld(KeyEvent.VK_RIGHT) ||
				InputManager.getInstance().isKeyHeld(KeyEvent.VK_DOWN) ||
				InputManager.getInstance().isKeyHeld(KeyEvent.VK_LEFT)) {

			isWalking = true;

			if (InputManager.getInstance().isKeyHeld(KeyEvent.VK_UP)) {
				direction = "up";
				animation.start();

			} else if (InputManager.getInstance().isKeyHeld(KeyEvent.VK_DOWN)) {
				direction = "down";
				animation.start();

			} else if (InputManager.getInstance().isKeyHeld(KeyEvent.VK_LEFT)) {
				direction = "left";
				animation.start();

			} else {
				direction = "right";
				animation.start();
			}

			// CHECK TILE COLLISION
			collisionOn = false;
			// GameManager.currentTiledMap.checkTile(this);

			// CHECK OBJECT COLLISION
			int objIndex = GameManager.currentTiledMap.checkObject(this, true);
			pickUpObject(objIndex);

			// INTERACT WITH NPCS
			int npcIndex = GameManager.currentTiledMap.checkEntity(this, GameManager.npcs);
			interactNPC(npcIndex);

			// IF COLLISION IS FALSE, PLAYER CAN MOVE
			if (!collisionOn) {

				switch (direction) {
					case "up":
						if(!collidingTop(speedY)){
							position.y -= speed;
						}
						break;
					case "down":
						if(!collidingBottom(speedY)){
							position.y += speed;
						}
						break;
					case "left":
						if(!collidingLeft(speedX)){
							position.x -= speed;
						}
						break;
					case "right":
						if(!collidingRight(speedX)){
							position.x += speed;
						}
						break;
				}
			}
		} else {
			animation.stop();
			animation.reset();
			isWalking = false;
		}
	}

	public int searchItemInIndex(String itemName) {
		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.get(i) != null && inventory.get(i).getName().equals(itemName)) {
				return i;
			}
		}
		return -1; // Item não encontrado
	}

	public boolean canObtainItem(Entity item) {
		boolean canObtain = false;

		if (item.isStackable()) {
			int index = searchItemInIndex(item.getName());

			if (index == -1) {
				item.setAmount(1); // Inicializa a quantidade como 1 ao adicionar um novo item
				canObtain = inventory.readd(item);
			} else {
				inventory.get(index).amount++;
				canObtain = true;
			}
		} else {
			if (inventory.countItems() < inventory.size()) {
				canObtain = inventory.readd(item);
			}
		}

		return canObtain;
	}

	public void setItems(){
		inventory.add(new Obj_Pickaxe(), 0);
	}

	public void setDefaultValues() {

		position.x = GameManager.tileSize * 33;
		position.y = GameManager.tileSize * 32;
		speed = 2;
		direction = "down";

		this.level = 1;
		this.name = "Kai";
		this.maxHp = 200;
		this.hp = 200;
		strength = 5;
		dexterity = 2;
		maxMana = 20;
		mana = maxMana;
		exp = 0;
		nextLevelExp = 0;
		gold = 5000;
		currentWeapon = null;
		currentShield = null;
		atk = getAttack();
		def = getDefense();
	}

	public int getAttack(){

		if(currentWeapon == null){
			return strength;
		}

		return atk = strength * currentWeapon.getAttackValue();
	}

	public int getDefense(){

		if(currentShield == null){
			return dexterity;
		}

		return def = dexterity * currentShield.getDefenseValue();
	}

	public void useItem(int itemIndex, int playerIndex) {
		if (itemIndex < inventory.size() && inventory.get(itemIndex) != null) {
			Entity selectedItem = inventory.get(itemIndex);

			if (selectedItem instanceof OBJ_Health_Potion) {
				selectedItem.use(GameManager.getPlayerAtIndex(playerIndex));
				selectedItem.amount--;
				if (selectedItem.getAmount() <= 0) {
					inventory.remove(itemIndex);
				}
			} else if (selectedItem instanceof OBJ_Key || selectedItem instanceof Obj_Pickaxe) {
				if(selectedItem.use(GameManager.getPlayerAtIndex(playerIndex)) && selectedItem.getAmount() <= 0){
					inventory.remove(itemIndex);
				}
			} else if (selectedItem instanceof OBJ_Ore){
				if(selectedItem.use(GameManager.getPlayerAtIndex(playerIndex))){
					inventory.remove(itemIndex);
				}
			}

		}
	}

	private boolean collidingTop(int speedY) {
		for (int r = 0; r < GameManager.currentTiledMap.collisionAreasForUpdate.size(); r++) {
			final Rectangle area = GameManager.currentTiledMap.collisionAreasForUpdate.get(r);

			int originX = area.x;
			int originY = area.y + speedY * (int) speed + 3 * (int) speed;

			final Rectangle futureArea = new Rectangle(originX, originY, area.width,
					area.height);

			if (TOP_LIMIT.intersects(futureArea)) {
				return true;
			}
		}

		return false;
	}

	private boolean collidingBottom(int speedY) {
		for (int r = 0; r < GameManager.currentTiledMap.collisionAreasForUpdate.size(); r++) {
			final Rectangle area = GameManager.currentTiledMap.collisionAreasForUpdate.get(r);

			int originX = area.x;
			int originY = area.y + speedY * (int) speed - 3 * (int) speed;

			final Rectangle futureArea = new Rectangle(originX, originY, area.width,
					area.height);

			if (BOTTOM_LIMIT.intersects(futureArea)) {
				return true;
			}
		}

		return false;
	}

	private boolean collidingLeft(int speedX) {
		for (int r = 0; r < GameManager.currentTiledMap.collisionAreasForUpdate.size(); r++) {
			final Rectangle area = GameManager.currentTiledMap.collisionAreasForUpdate.get(r);

			int originX = area.x + speedX * (int) speed + 3 * (int) speed;
			int originY = area.y;

			final Rectangle futureArea = new Rectangle(originX, originY, area.width,
					area.height);

			if (LEFT_LIMIT.intersects(futureArea)) {
				return true;
			}
		}

		return false;
	}

	private boolean collidingRight(int speedX) {
		for (int r = 0; r < GameManager.currentTiledMap.collisionAreasForUpdate.size(); r++) {
			final Rectangle area = GameManager.currentTiledMap.collisionAreasForUpdate.get(r);

			int originX = area.x + speedX * (int) speed - 3 * (int) speed;
			int originY = area.y;

			final Rectangle futureArea = new Rectangle(originX, originY, area.width,
					area.height);

			if (RIGHT_LIMIT.intersects(futureArea)) {
				return true;
			}
		}

		return false;
	}

	private void interactNPC(int i) {
		if (i != 999) {
			NPC npc = GameManager.npcs.get(GameManager.currentMap).get(i);
			switch (npc.npcType) {
				case "Blacksmith", "Helena":
					if (InputManager.getInstance().isKeyDown(KeyEvent.VK_SPACE)) {
						npc.speak();
					}
					break;
                // Case "OldMan" e outros NPCs podem ser adicionados aqui
			}
		}
	}

	public void pickUpObject(int i) {

		if(i != 999){

			String objectName = GameManager.obj[GameManager.currentMap][i].name;

			switch (objectName){
				case "Key", "Pickaxe":
					canObtainItem(GameManager.obj[GameManager.currentMap][i]);
					GameManager.obj[GameManager.currentMap][i] = null;
					break;
				case "Door":
					if(InputManager.getInstance().isKeyDown(KeyEvent.VK_SPACE)){
						GameManager.obj[GameManager.currentMap][i].interact();
					}
				case "Chest":
					if(InputManager.getInstance().isKeyDown(KeyEvent.VK_SPACE)){
						GameManager.obj[GameManager.currentMap][i].interact();
					}
					break;
				case "Rock":
					if(InputManager.getInstance().isKeyDown(KeyEvent.VK_SPACE)){
						GameManager.obj[GameManager.currentMap][i].interact();
					}
					break;
				case "Save":
					GameManager.obj[GameManager.currentMap][i].interact();
					break;
			}
		}
		else{
			if(GameManager.canSave){
				GameManager.canSave = false;
			}
		}
	}

	private void pickUpItem(Entity item) {
		canObtainItem(item);
	}

	private void handleChestInteraction() {
		if (playerHasOre()) {
			Quest blackSmithQuest = GameManager.questManager.getQuest(QuestEnum.HELP_BLACKSMITH);
			if (blackSmithQuest != null &&
					!blackSmithQuest.getObjectives().get(BlacksmithQuestObjective.FIND_ORE.getIndex()).isCompleted()) {
				GameManager.questManager.completeObjective(blackSmithQuest, 1);
			}
		}
	}

	public Rectangle getPlayerBounds() {

		return new Rectangle(position.x, position.y, solidArea.width, solidArea.height);
	}

	private boolean playerHasOre(){
		// Verifica se o jogador tem o martelo no inventário
		for(int i = 0; i < GameManager.getPlayer().inventory.size(); i++){

			Object item = (Object) GameManager.getPlayer().inventory.get(i);

			if(item.getName().equals("Ore")){
				return true;
			}
		}
		return false;
	}

	// Verifica se o jogador possui um item pelo nome
	public boolean hasItem(String itemName) {
		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.get(i).getName().equals(itemName)) {
				return true;
			}
		}
		return false;
	}

	// Remove um item do inventário pelo nome
	public boolean removeItemFromInventory(String itemName) {
		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.get(i).getName().equals(itemName)) {
				inventory.remove(i);
				return true;
			}
		}
		return false;
	}

	// Recupera o índice do item no inventário
	public int getItemIndex(String itemName) {
		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.get(i).getName().equals(itemName)) {
				return i;
			}
		}
		return -1; // Retorna -1 se o item não for encontrado
	}
}
