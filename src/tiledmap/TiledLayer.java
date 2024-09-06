package tiledmap;

public abstract class TiledLayer {
	
	protected int width;
	protected int height;
	protected int x;
	protected int y;
	
	public TiledLayer(int width, int height, int x, int y) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}
}