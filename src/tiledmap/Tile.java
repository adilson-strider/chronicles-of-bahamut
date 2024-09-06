package tiledmap;

import graphics.Sprite;

import java.awt.*;

public class Tile {
	
	public Sprite sprite;
	public int id;
	public boolean collision;

	public Tile(final Sprite sprite) {
		this.sprite = sprite;
		this.id = id;
		collision = false;
	}

	public Tile(final Sprite sprite, final int id, final boolean solido) {
		this.sprite = sprite;
		this.id = id;
		this.collision = solido;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public int getId() {
		return id;
	}

	public void makeSolid(final boolean isSolid) {
		this.collision = isSolid;
	}

	public Rectangle getLimits(final int x, final int y) {
		return new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());
	}

}
