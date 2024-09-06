package tools;

import entity.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface Asset {
    void update();

    void draw(Graphics2D graphics2D);

    String getName();

    void setIndex(int i);

    void setAmount(int i);

    int getAmount();

    int getIndex();

    boolean isCollision();

    boolean isStackable();

    int getLevel();

    int getStrength();

    int getDexterity();

    int getAttackPower();

    int getDefensePower();

    void setAttackPower(int attack);

    void setDefensePower(int defense);

    int getExp();

    int getNextLevelExp();

    int getCoins();

    Object getCurrentWeapon();

    Object getCurrentShield();

    BufferedImage getImage();

    String getDescription();

    boolean use(Entity entity);

    int getMaxLife();

    int getPrice();
}
