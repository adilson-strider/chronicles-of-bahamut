package tiledmap;

import java.awt.*;

public class Portal {

    private final Rectangle area;
    private final String targetMapPath;
    private final Point targetPosition;
    private final String mapName;

    public Portal(Rectangle area, String targetMapPath, Point targetPosition, String mapName) {
        this.area = area;
        this.targetMapPath = targetMapPath;
        this.targetPosition = targetPosition;
        this.mapName = mapName;
    }

    public String getMapName(){
        return mapName;
    }

    public Rectangle getArea() {
        return area;
    }

    public String getTargetMapPath() {
        return targetMapPath;
    }

    public Point getTargetPosition() {
        return targetPosition;
    }

    public void draw(Graphics2D g2, int x, int y){
        g2.setColor(Color.WHITE);
        g2.drawRect(x, y, area.width, area.height);
    }
}
