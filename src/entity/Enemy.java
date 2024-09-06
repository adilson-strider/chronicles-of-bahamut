package entity;

import graphics.Animation;
import main.AssetSetter;
import main.GameManager;
import tools.ResourceLoader;

import java.awt.*;

public class Enemy extends Entity{

    public boolean isBlinking = false; // Indica se o inimigo está piscando
    public int blinkCounter = 0; // Contador de piscadas
    public int attackDelay = 50;
    public int cooldown; // Atraso entre ataques

    public Enemy(String name, int atk, int def, int hp, int gold, int exp, Point pos, int cooldown) {
        super();
        this.name = name;
        this.atk = atk;
        this.def = def;
        this.hp = hp;

        this.gold = gold;
        this.exp = exp;

        this.battlePos = pos;
        this.cooldown = cooldown;

        enemyBattleImage = ResourceLoader.loadImage(STR."/images/enemies/\{name}.png");

        enemyAttack = AssetSetter.loadAnimation("/images/fx/Attack1.png", 1, 3, 192, 192);
        enemyAttackAnimation = new Animation(enemyAttack, 5);
    }

    public String getName(){
        return name;
    }

    public void setPoint(int x, int y){
        battlePos.x = x;
        battlePos.y = y;
    }
}
