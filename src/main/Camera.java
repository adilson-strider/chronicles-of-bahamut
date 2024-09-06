package main;

import entity.Player;

public class Camera {
    private int x, y;
    private int width, height;
    private int worldWidth, worldHeight;

    public Camera() {

    }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public int getWorldWidth() { return worldWidth; }

    public int getWorldHeight() { return worldHeight; }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = clamp(x, 0, worldWidth - width);
        this.y = clamp(y, 0, worldHeight - height);
    }

    private int clamp(int value, int min, int max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public void centerOnPlayer(Player player) {
        setPosition(player.getX() - width / 2, player.getY() - height / 2);
    }

    public void setCameraBounds(int width, int height, int worldWidth, int worldHeight) {
        this.width = width;
        this.height = height;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }
}
