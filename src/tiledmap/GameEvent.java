package tiledmap;

import entity.Entity;
import tools.ResourceLoader;

import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

public class GameEvent extends Entity implements Serializable {
    private Rectangle area;
    private String eventType;
    private String name;
    private boolean eventDone;
    private String id;

    public GameEvent(Rectangle area, String eventType, String name, int x, int y, String id) {
        this.area = area;
        this.eventType = eventType;
        this.name = name;
        this.eventDone = false;
        this.id = id;
        this.position.x = x;
        this.position.y = y;
    }

    public GameEvent(Rectangle area, String eventType, String name, String x, String y, String id) {
        this.area = area;
        this.eventType = eventType;
        this.name = name;
        this.eventDone = false;
        this.position.x = Integer.parseInt(x);
        this.position.y = Integer.parseInt(y);
        this.id = id;
    }

    public Rectangle getArea() {
        return area;
    }

    public String getEventType() {
        return eventType;
    }

    public String getName() {
        return name;
    }

    public boolean isEventDone() {
        return eventDone;
    }

    public void setEventDone(boolean eventDone) {
        this.eventDone = eventDone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameEvent gameEvent = (GameEvent) o;
        return Objects.equals(id, gameEvent.id);
    }

    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

