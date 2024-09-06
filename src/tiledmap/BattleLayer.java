package tiledmap;

import entity.Enemy;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class BattleLayer {

    private final Rectangle area;
    private ArrayList<String> enemies;

    public BattleLayer(Rectangle area, ArrayList<String> enemies){

        this.area = area;
        this.enemies = enemies;
    }

    public Rectangle getArea() {
        return area;
    }

    public ArrayList<String> getEnemies(){
        return enemies;
    }

    public void draw(Graphics2D g2, int x, int y){
        g2.setColor(Color.GREEN);
        g2.drawRect(x, y, area.width, area.height);
    }
}
