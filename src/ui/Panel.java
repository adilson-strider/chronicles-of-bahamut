package ui;

import main.GameManager;
import tools.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Panel {

    private int frameX;
    private int frameY;
    private final int frameWidth;
    private final int frameHeight;

    public Panel(int frameX, int frameY, int frameWidth, int frameHeight){
        this.frameX = frameX;
        this.frameY = frameY;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
    }

    public void drawPanel(Graphics2D g2){
        //Color color = new Color(0, 0, 128, 210);
        Color color = new Color(0, 0, 0, 100);
        g2.setColor(color);
        int arcWidth = 5;
        int arcHeight = 5;
        g2.fillRoundRect(frameX, frameY, frameWidth, frameHeight, arcWidth, arcHeight);

        color = new Color(255, 255, 255);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(frameX, frameY, frameWidth, frameHeight, arcWidth, arcHeight);
    }

    public int getFrameX() {
        return frameX;
    }

    public int getFrameY() {
        return frameY;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public void setFrameX(int x){
        frameX = x;
    }

    public void setFrameY(int y){
        frameY = y;
    }
}
