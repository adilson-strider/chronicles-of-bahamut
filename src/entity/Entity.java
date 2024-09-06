package entity;

import cutscene.CutsceneCamera;
import gamestate.StateMachine;
import graphics.Animation;
import graphics.SpriteSheet;
import main.AssetSetter;
import main.Camera;
import main.GameManager;
import object.Object;
import tools.FixedSizeList;
import ui.UI;
import object.Shield;
import object.Weapon;
import tools.Asset;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

public class Entity implements Asset{

	//Stats
	public String name;
	public int speed;
	public int level;
	public int strength;
	public int hp;
	public int maxHp;
	public int atk;
	public int def;
	public int dexterity;
	public int exp;
	public int nextLevelExp;
	public int gold;
	public int mana;
	public int maxMana;
	public Weapon currentWeapon;
	public Shield currentShield;
	public String description = "";

	public Entity loot;
	public boolean opened = false;
	public BufferedImage closedChest;
	public BufferedImage openedChest;

	public int entityID;

	public EntityType entityType;

	public BufferedImage image;

	public Point position = new Point();
	public Point destination; // Posição de destino

	private int index;
	private int value;
	public boolean collision = false;
	public Rectangle solidArea = new Rectangle(0,0,40,40);
	public int solidAreaDefaultX = 0;
	public int solidAreaDefaultY = 0;
	private int price;
	public boolean stackable = false;
	public int amount = 1;
	protected int attack;
	protected int defense;

	// Images for each animation
	public BufferedImage[] walkingLeft;

	public BufferedImage[] walkingDown;

	public BufferedImage[] walkingUp;
	public BufferedImage[] walkingRight;
	public BufferedImage[] standing;

	public int screenX;
	public int screenY;

	protected Animation walkLeft;
	protected Animation walkUp;
	protected Animation walkDown;
	protected Animation walkRight;
	protected Animation stand;

	final int spriteWidth = 48;
	final int spriteHeight = 48;

	public Animation animation;

	public String direction;
	public boolean collisionOn = false;
	public boolean moving = false;
	protected int actionLockCounter;

	// COLLISION
	protected Rectangle TOP_LIMIT;
	protected Rectangle BOTTOM_LIMIT;
	protected Rectangle LEFT_LIMIT;
	protected Rectangle RIGHT_LIMIT;

	// DIALOGUE FIELDS
	public int dialogueIndex = 0;
	public int dialogueSet = 0;
	public String[][] dialogues = new String[20][20];

	//================ BATTLE ANIMATIONS =========================

	protected BufferedImage enemyBattleImage;
	public BufferedImage playerBattleImage;
	public BufferedImage deadPlayerBattleImage;

	// BATTLE ANIMATION
	public Animation weaponAnimation;
	public BufferedImage[] weaponFrame;

	public Animation playerAttackAnimation;
	public BufferedImage[] playerAttack;

	public Animation enemyAttackAnimation;
	public BufferedImage[] enemyAttack;

	public boolean isAttackAnim = false;

	// =============== BATTLE ANIMATIONS ==========================

	public Point battlePos;
	public String npcType;

	public FixedSizeList<Entity> inventory = new FixedSizeList<>(20);

	public BufferedImage portrait;

	public boolean sleep = false;

	public int attackCooldown;
	public int minCooldown;
	public boolean turnOn;

	public Entity(){
		direction = "down";

		TOP_LIMIT = new Rectangle();
		BOTTOM_LIMIT = new Rectangle();
		LEFT_LIMIT = new Rectangle();
		RIGHT_LIMIT = new Rectangle();
		turnOn = false;
	}

	public void loadPlayerAnimation(String path){
		int framesPerDirection = 3;

		walkingDown = AssetSetter.loadAnimationFromRow(path, 0, 3, 48, 48);
		walkingLeft = AssetSetter.loadAnimationFromRow(path, 1, framesPerDirection, spriteWidth, spriteHeight);
		walkingRight = AssetSetter.loadAnimationFromRow(path, 2, framesPerDirection, spriteWidth, spriteHeight);
		walkingUp = AssetSetter.loadAnimationFromRow(path, 3, framesPerDirection, spriteWidth, spriteHeight);

		standing = new BufferedImage[]{ SpriteSheet.getSprite(path, 0, 0, spriteWidth, spriteHeight) };

		walkDown = new Animation(walkingDown, 10);
		walkLeft = new Animation(walkingLeft, 10);
		walkRight = new Animation(walkingRight, 10);
		walkUp = new Animation(walkingUp, 10);
		stand = new Animation(standing, 10);

		animation = walkDown;
	}

	public void setAction(){}

	public void interact(){}

	public void startDialogue(Entity entity, int setNum, String state){
		StateMachine.getInstance().change(state);
		UI.getInstance().entity = entity;
		dialogueSet = setNum;
	}

	public Point getPosition() {
		return position;
	}

	public int getX() {
		return position.x;
	}

	public void setX(int x) {
		this.position.x = x;
	}

	public int getY() {
		return position.y;
	}

	public void setY(int y) {
		position.y = y;
	}

	public boolean hasReached(Point destination) {
		return position.x == destination.x && position.y == destination.y;
	}

	public void setPosition(Point position) {
		this.position.x = position.x * GameManager.tileSize;
		this.position.y = position.y * GameManager.tileSize;
	}

	public void moveToNextPosition(Entity entity, boolean movingHorizontally, Point destination) {
		int currentX = entity.getX();
		int currentY = entity.getY();
		int targetX = destination.x;
		int targetY = destination.y;

		if (movingHorizontally) {
			if (currentX != targetX) {
				int deltaX = targetX - currentX;
				int moveX = Math.min(speed, Math.abs(deltaX)) * Integer.signum(deltaX);
				entity.setX(currentX + moveX);
			} else {
				movingHorizontally = false;
			}
		}

		if (!movingHorizontally) {
			if (currentY != targetY) {
				int deltaY = targetY - currentY;
				int moveY = Math.min(speed, Math.abs(deltaY)) * Integer.signum(deltaY);
				entity.setY(currentY + moveY);
			}
		}

		setDirection(destination);
	}

	private void setDirection(Point destination) {
		int currentX = this.getX();
		int currentY = this.getY();
		int targetX = destination.x;
		int targetY = destination.y;

		if (currentX < targetX) {
			animation = walkRight;
			direction = "right";
			animation.start();
			animation.update();
		} else if (currentX > targetX) {
			animation = walkLeft;
			direction = "left";
			animation.start();
			animation.update();
		} else if (currentY < targetY) {
			animation = walkDown;
			direction = "down";
			animation.start();
			animation.update();
		} else if (currentY > targetY) {
			direction = "up";
			animation = walkUp;
			animation.start();
			animation.update();
		}
	}

	public int getLeftX() {
		int leftX = position.x + solidArea.x;
		System.out.println("getLeftX: " + leftX);
		return leftX;
	}

	public int getRightX() {
		int rightX = position.x + solidArea.x + solidArea.width;
		System.out.println("getRightX: " + rightX);
		return rightX;
	}

	public int getTopY() {
		int topY = position.y + solidArea.y;
		System.out.println("getTopY: " + topY);
		return topY;
	}

	public int getBottomY() {
		int bottomY = position.y + solidArea.y + solidArea.height;
		System.out.println("getBottomY: " + bottomY);
		return bottomY;
	}

	public int getCol() {
		int col = (position.x + solidArea.x) / GameManager.tileSize;
		System.out.println("getCol: " + col);
		return col;
	}

	public int getRow() {
		int row = (position.y + solidArea.y) / GameManager.tileSize;
		System.out.println("getRow: " + row);
		return row;
	}

	@Override
	public void setIndex(int i) {
		index = i;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public void setAmount(int i) {
		amount = i;
	}

	@Override
	public int getAmount() {
		return amount;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public boolean isCollision() {
		return collision;
	}

	@Override
	public boolean isStackable() {
		return stackable;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public int getStrength() {
		return strength;
	}

	@Override
	public int getDexterity() {
		return dexterity;
	}

	@Override
	public int getAttackPower() {
		return attack;
	}

	@Override
	public int getDefensePower() {
		return defense;
	}

	@Override
	public void setAttackPower(int attack) {
		this.attack = attack;
	}

	@Override
	public void setDefensePower(int defense) {
		this.defense = defense;
	}

	@Override
	public int getExp() {
		return exp;
	}

	@Override
	public int getNextLevelExp() {
		return nextLevelExp;
	}

	@Override
	public int getCoins() {
		return gold;
	}

	@Override
	public Entity getCurrentWeapon() {
		return currentWeapon;
	}

	@Override
	public Entity getCurrentShield() {
		return currentShield;
	}

	@Override
	public BufferedImage getImage() {
		return image;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int getMaxLife() {
		return maxHp;
	}

	@Override
	public int getPrice() {
		return price;
	}

	public void update() {

		if(!sleep){
			final int speedX = evaluateSpeedX();
			final int speedY = evaluateSpeedY();

			if(animation != null){
				animation.update();
			}

			setAction();
			collisionOn = false;

			// gp.cChecker.checkTile(this);
			GameManager.currentTiledMap.checkPlayer(this);

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
		}
	}

	@Override
	public void draw(Graphics2D g2) {

		int screenX = position.x - GameManager.getPlayer().position.x + GameManager.getPlayer().screenX;
		int screenY = position.y - GameManager.getPlayer().position.y + GameManager.getPlayer().screenY;

		g2.drawRect(solidArea.x + screenX, solidArea.y + screenY, 32, 32);
		g2.drawImage(image, screenX, screenY, GameManager.tileSize, GameManager.tileSize, null);
	}

	protected void moveNpc() {
		final int speedX = evaluateSpeedX();
		final int speedY = evaluateSpeedY();

		if (speedX == 0 && speedY == 0) {
			return;
		}

		move(speedX, speedY);
	}

	public BufferedImage getBattleImage(){
		return enemyBattleImage;
	}

	public int evaluateSpeedX() {
		int speedX = 0;

		if (Objects.equals(direction, "left")) {
			speedX = -1;
		} else if (Objects.equals(direction, "right")) {
			speedX = 1;
		}

		return speedX;
	}

	public int evaluateSpeedY() {
		int speedY = 0;

		if (Objects.equals(direction, "up")) {
			speedY = -1;
		} else if (Objects.equals(direction, "down")) {
			speedY = 1;
		}

		return speedY;
	}

	public void setLoot(Object loot){}

	public int getDetected(Entity user, Entity[][] target, String targetName) {
		System.out.println("Método getDetected foi chamado.");
		int index = 999;

		int nextWorldX = user.getLeftX();
		int nextWorldY = user.getTopY();

		System.out.println("Valores iniciais - nextWorldX: " + nextWorldX + ", nextWorldY: " + nextWorldY);

		// Ajustando as coordenadas com base na direção do usuário
		switch (user.direction) {
			case "up":
				nextWorldY = user.getTopY() - 1;
				break;
			case "down":
				nextWorldY = user.getBottomY() + 1;
				break;
			case "left":
				nextWorldX = user.getLeftX() - 1;
				break;
			case "right":
				nextWorldX = user.getRightX() + 1;
				break;
		}

		System.out.println("Valores ajustados - nextWorldX: " + nextWorldX + ", nextWorldY: " + nextWorldY);

		// Convertendo coordenadas do mundo para coordenadas da matriz
		int col = nextWorldX / GameManager.tileSize;
		int row = nextWorldY / GameManager.tileSize;

		System.out.println("Coluna calculada: " + col + ", Linha calculada: " + row);

		// Verificando se a posição calculada está dentro dos limites da matriz
		if (col < 0 || row < 0 || col >= target[0].length || row >= target.length) {
			System.out.println("Fora dos limites da matriz. col: " + col + ", row: " + row + ", target length: " + target.length + ", target[0] length: " + target[0].length);
			return index; // Posição fora dos limites, retorna o índice padrão
		}

		// Certificando-se de que GameManager.currentMap está dentro dos limites
		if (GameManager.currentMap < 0 || GameManager.currentMap >= target.length) {
			System.out.println("Mapa atual fora dos limites.");
			return index;
		}

		// Adicionando prints de depuração
		System.out.println("nextWorldX: " + nextWorldX);
		System.out.println("nextWorldY: " + nextWorldY);
		System.out.println("col: " + col);
		System.out.println("row: " + row);
		System.out.println("GameManager.currentMap: " + GameManager.currentMap);
		System.out.println("target length: " + target.length);

		// Percorrendo as entidades no mapa atual para encontrar o alvo
		for (int i = 0; i < target[GameManager.currentMap].length; i++) {
			Entity currentEntity = target[GameManager.currentMap][i];
			if (currentEntity != null) {
				int entityCol = currentEntity.getCol();
				int entityRow = currentEntity.getRow();
				System.out.println("Entity at index " + i + ": " + currentEntity.name + ", col: " + entityCol + ", row: " + entityRow + ", targetName: " + targetName);

				if (entityCol == col && entityRow == row && currentEntity.name.equals(targetName)) {
					index = i;
					break;
				}
			}
		}

		System.out.println("Índice encontrado: " + index);
		return index;
	}

	public void move(final int speedX, final int speedY) {
		moving = true;

		if(animation != null){
			animation.update();
		}

		changeDirection(speedX, speedY);
	}

	public boolean use(Entity entity){

		System.out.println("test");

		return true; }

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

	private boolean outOfBounds(final int speedX, final int speedY) {

		int futurePositionX = position.x + speedX * (int) speed;
		int futurePositionY = position.y + speedY * (int) speed;

		final Rectangle mapBounds = GameManager.currentTiledMap.getBounds(futurePositionX, futurePositionY);

		final boolean out;

		if (TOP_LIMIT.intersects(mapBounds) || BOTTOM_LIMIT.intersects(mapBounds)
				|| LEFT_LIMIT.intersects(mapBounds) || RIGHT_LIMIT.intersects(mapBounds)) {
			out = false;
		} else {
			out = true;
		}

		return out;
	}

	private void changeDirection(final int speedX, final int speedY) {
		if (speedX == -1) {
			direction = "left";
		} else if (speedX == 1) {
			direction = "right";
		}

		if (speedY == -1) {
			direction = "up";
		} else if (speedY == 1) {
			direction = "down";
		}
	}

	public void draw(Graphics2D g2, CutsceneCamera camera) {
		int screenX = position.x + GameManager.screenWidth / 2 - camera.getX();
		int screenY = position.y + GameManager.screenHeight / 2 - camera.getY();

		// Desenhe o jogador na tela
		g2.drawImage(animation.getSprite(), screenX, screenY, null);
	}

	public void draw(Graphics2D g2, Camera cam) {

		int screenX = position.x - (GameManager.tileSize/2) - cam.getX();
		int screenY = position.y - (GameManager.tileSize/2) - cam.getY();

		TOP_LIMIT = new Rectangle(screenX,
				screenY, GameManager.tileSize, 1);

		BOTTOM_LIMIT = new Rectangle(screenX,
				screenY + GameManager.tileSize, GameManager.tileSize, 1);

		LEFT_LIMIT = new Rectangle(screenX,
				screenY, 1, GameManager.tileSize);

		RIGHT_LIMIT = new Rectangle(screenX + GameManager.tileSize,
				screenY, 1, GameManager.tileSize);

		if(animation != null){
			// Desenha o jogador na tela
			g2.drawImage(animation.getSprite(), screenX, screenY, 40, 40, null);
		}
		else{
			// Desenha um objeto na tela
			g2.drawImage(image, screenX, screenY, 40, 40, null);
		}


		/*g2.drawRect(TOP_LIMIT.x, TOP_LIMIT.y, TOP_LIMIT.width, TOP_LIMIT.height);
		g2.drawRect(RIGHT_LIMIT.x, RIGHT_LIMIT.y, RIGHT_LIMIT.width, RIGHT_LIMIT.height);
		g2.drawRect(LEFT_LIMIT.x, LEFT_LIMIT.y, LEFT_LIMIT.width, LEFT_LIMIT.height);
		g2.drawRect(BOTTOM_LIMIT.x, BOTTOM_LIMIT.y, BOTTOM_LIMIT.width, BOTTOM_LIMIT.height);*/

		// g2.drawRect(solidArea.x + screenX, solidArea.y + screenY, solidArea.width, solidArea.height);
		//DebugDrawing.drawString(g, positionX + "-" + positionY,  centerX, centerY - 8);
	}

	@Override
	public String getName() {
		return name;
	}

	public FixedSizeList<Entity> getInventory() {
		return inventory;
    }
}
