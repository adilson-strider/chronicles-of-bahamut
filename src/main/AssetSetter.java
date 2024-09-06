package main;


import entity.Entity;
import entity.npcs.NPC;
import entity.Player;
import graphics.Animation;
import graphics.SpriteSheet;
import object.OBJ_Ore;
import object.OBJ_Special_Hammer;
import object.Object;
import object.consumables.OBJ_Health_Potion;
import object.consumables.OBJ_Key;
import object.swords.BeginnerBlade;
import tools.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class AssetSetter {

	private static int objIndex = 0;

	public AssetSetter(){

	}

	public static void setObject(int x, int y, int mapNumber, Object obj, String loot) {

		GameManager.obj[mapNumber][objIndex] = obj;

		switch (loot){
			case "Potion":
				GameManager.obj[mapNumber][objIndex].setLoot(new OBJ_Health_Potion());
				break;
			case "Key":
				GameManager.obj[mapNumber][objIndex].setLoot(new OBJ_Key());
				break;
			case "Hammer":
				GameManager.obj[mapNumber][objIndex].setLoot(new OBJ_Special_Hammer());
				break;
			case "Ore":
				GameManager.obj[mapNumber][objIndex].setLoot(new OBJ_Ore());
				break;
		}


		GameManager.obj[mapNumber][objIndex].position.x = x;
		GameManager.obj[mapNumber][objIndex].position.y = y;
		objIndex++;
	}

	public static void setNPC(NPC npc, int x, int y, int mapNumber, String npcImageName) {

		// Configura as propriedades do NPC
		npc.npcType = npcImageName;
		npc.loadPlayerAnimation("/images/characters/NPC/" + npcImageName + ".png");
		npc.position.x = x;
		npc.position.y = y;

		// Adiciona o NPC ao ArrayList apropriado
		GameManager.npcs.get(mapNumber).add(npc);
	}

	public void setPlayers(ArrayList<Player> players){
		// Kai
		players.getFirst().playerBattleImage = ResourceLoader.loadImage("/images/battlers/kai.png");
		players.getFirst().name = "Kai";
		players.getFirst().deadPlayerBattleImage = ResourceLoader.loadImage("/images/battlers/kai_dead.png");
		players.getFirst().playerAttack = loadAnimation("/images/battlers/kai_attack.png", 1, 3, 334, 200);
		players.getFirst().playerAttackAnimation = new Animation(players.getFirst().playerAttack, 20);
		players.getFirst().portrait = ResourceLoader.loadImage("/images/profiles/kai.png");
		players.getFirst().minCooldown = 8;
		players.getFirst().attackCooldown = players.getFirst().minCooldown;

		players.getFirst().currentWeapon = new BeginnerBlade();

		if(players.size() == 1){
			players.getFirst().battlePos = new Point(
					(GameManager.screenWidth/2)+(GameManager.tileSize*3)-50,
					(GameManager.screenHeight/2)-(GameManager.tileSize*2));
		}
		else{
			players.getFirst().battlePos = new Point(
					(GameManager.screenWidth/2)+(GameManager.tileSize*3)-120,
					(GameManager.screenHeight/2)-(GameManager.tileSize*2));
		}
	}

	public static BufferedImage[] loadAnimation(String path, int rows, int columns, int spriteWidth, int spriteHeight) {
		BufferedImage[] animationFrames = new BufferedImage[rows * columns];

		int frameIndex = 0;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				animationFrames[frameIndex++] = SpriteSheet.getSprite(path, row, col, spriteWidth, spriteHeight);
			}
		}

		return animationFrames;
	}

	public static BufferedImage[] loadAnimationFromRow(String path, int row, int frames, int spriteWidth, int spriteHeight) {
		BufferedImage[] animationFrames = new BufferedImage[frames];

		for (int col = 0; col < frames; col++) {
			animationFrames[col] = SpriteSheet.getSprite(path, row, col, spriteWidth, spriteHeight);
		}

		return animationFrames;
	}

	public static void addHelena(ArrayList<Player> players){

		int baseX = (GameManager.screenWidth / 2) + (GameManager.tileSize * 3);
		int baseY = (GameManager.screenHeight / 2) - (GameManager.tileSize * 2);
		players.add(new Player(new Point(baseX + (-60), baseY)));

		players.get(1).playerBattleImage = ResourceLoader.loadImage("/images/battlers/helena.png");
		players.get(1).name = "Helena";
		players.get(1).deadPlayerBattleImage = ResourceLoader.loadImage("/images/battlers/helena_dead.png");
		players.get(1).playerAttack = loadAnimation("/images/battlers/helena_attack.png", 1, 3, 334, 200);
		players.get(1).playerAttackAnimation = new Animation(players.get(1).playerAttack, 20);
		players.get(1).portrait = ResourceLoader.loadImage("/images/profiles/helena.png");
		players.get(1).minCooldown = 4;
		players.get(1).attackCooldown = players.get(1).minCooldown;
		players.get(1).maxHp = 250;
		players.get(1).hp = 100;
	}

	public static void addZetsu(ArrayList<Player> players){

		// Zetsu
		int baseX = (GameManager.screenWidth / 2) + (GameManager.tileSize * 3);
		int baseY = (GameManager.screenHeight / 2) - (GameManager.tileSize * 2);
		players.add(new Player(new Point(baseX + (40), baseY)));

		players.get(2).playerBattleImage = ResourceLoader.loadImage("/images/battlers/zetsu.png");
		players.get(2).name = "Zetsu";
		players.get(2).deadPlayerBattleImage = ResourceLoader.loadImage("/images/battlers/zetsu_dead.png");
		players.get(2).playerAttack = loadAnimation("/images/battlers/zetsu_attack.png", 1, 3, 334, 200);
		players.get(2).playerAttackAnimation = new Animation(players.get(2).playerAttack, 20);
		players.get(2).portrait = ResourceLoader.loadImage("/images/profiles/zetsu.png");
		players.get(2).minCooldown = 7;
		players.get(2).attackCooldown = players.get(2).minCooldown;
		players.get(2).hp = 100;
	}
}
