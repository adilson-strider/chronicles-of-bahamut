package gamestate;

import org.json.simple.parser.ParseException;

import java.awt.*;

public interface IState {
    public void update();
    public void draw(Graphics2D g2);
    public void onEnter();
    public void onExit();
    public String getName();
}
