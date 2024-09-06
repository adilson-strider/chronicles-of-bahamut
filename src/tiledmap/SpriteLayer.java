package tiledmap;

public class SpriteLayer extends TiledLayer {
	
	private int[] sprites;
	
	public SpriteLayer(int width, int height, int x, int y, int[] sprites) {
		super(width, height, x, y);
		this.sprites = sprites;
	}
	
	public int[] getSpritesArray() {
		return sprites;
	}

}