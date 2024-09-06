package battle;

import main.GameManager;
import ui.UI;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DamageText{

    GameManager gp;
    private String damage;
    private Color color;
    private int counter;
    private int yOffset = 0;

    private Point position;

    public DamageText(Point position, int damage, Color color) {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.setFont(UI.getInstance().maruMonica);
        g.setColor(color);
        this.damage = String.valueOf(damage);
        this.color = color;
        this.counter = 0;

        this.position = position;
    }

    public boolean textCounter() {

        // boolean
        // delete the text after a few seconds
        this.counter += 1;
        return this.counter < 60;
    }

    public void drawDamageText(Graphics2D g2){

        if(textCounter()){
            yOffset -= 1;
            g2.drawString(damage, position.x + 56, (position.y + 32) + yOffset);
        }
    }

    public int getCounter(){
        return counter;
    }
}
