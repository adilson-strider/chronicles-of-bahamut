package tools;

import main.GameManager;
import ui.UI;

import java.awt.*;

public class GameNotification {

    GameManager gp;
    private final String notification;
    public int counter;
    public boolean flag;
    int timer;

    public GameNotification(String notification, int timerValue){
        this.notification = notification;
        flag = false;
        counter = 0;
        timer = timerValue;
    }

    public void update() {

        if(counter <= timer){
            this.counter += 1;
        }
        else {
            flag = true;
        }
    }

    public void drawNotification(Graphics2D g2, int x, int y){

        if(!flag){
            UI.getInstance().drawSubWindow(g2, x, y, GameManager.screenWidth, GameManager.tileSize);

            UI.getInstance().setColorAndFont(g2, Color.WHITE, Font.PLAIN, 12);
            g2.drawString(notification, x + 20, y + 22);
        }
    }

    public boolean isFlag(){
        return flag;
    }
}
