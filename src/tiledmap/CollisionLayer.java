package tiledmap;

import java.awt.Rectangle;

public class CollisionLayer extends TiledLayer {

	private Rectangle[] collidables;
	
	public CollisionLayer(int width, int height, int x, int y, Rectangle[] collidables) {
		super(width, height, x, y);
		this.collidables = collidables;
	}
	
	public Rectangle[] getCollidables() {
		return collidables;
	}
}
