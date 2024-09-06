package graphics;

import java.awt.image.BufferedImage;

import tools.ResourceLoader;

public class SpriteSheet {

	final private int sheetWidthInPixels;
	final private int sheetHeightInPixels;

	private static BufferedImage spriteSheet;
	final private int sheetWidthInSprites;
	final private int sheetHeightInSprites;

	final private int spriteWidth;
	final private int spriteHeight;

	final private Sprite[] sprites;

	private static final int TILE_SIZE = 32;

	public SpriteSheet(final String path, final int spriteWidth, final int spriteHeight, final boolean opaqueSheet) {
		BufferedImage image;

		image = ResourceLoader.loadImage(path);

		sheetWidthInPixels = image.getWidth();
		sheetHeightInPixels = image.getHeight();

		sheetWidthInSprites = sheetWidthInPixels / spriteWidth;
		sheetHeightInSprites = sheetHeightInPixels / spriteHeight;

		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;

		sprites = new Sprite[sheetWidthInSprites * sheetHeightInSprites];

		fillSpritesFromImage(image);
	}

	private void fillSpritesFromImage(final BufferedImage image) {
		for (int y = 0; y < sheetHeightInSprites; y++) {
			for (int x = 0; x < sheetWidthInSprites; x++) {
				final int posX = x * spriteWidth;
				final int posY = y * spriteHeight;

				sprites[x + y * sheetWidthInSprites] = new Sprite(
						image.getSubimage(posX, posY, spriteWidth, spriteHeight));
			}
		}
	}

	public static BufferedImage loadSprite(String file) {

		BufferedImage sprite;

        sprite = ResourceLoader.loadImage(file);

        return sprite;
	}

	public static BufferedImage getSprite(String path, int row, int column, int spriteWidth, int spriteHeight) {

		spriteSheet = loadSprite(path);
		// Calcula as coordenadas corretas do sprite na spritesheet
		int x = column * spriteWidth;
		int y = row * spriteHeight;

		// Extrai o sprite da spritesheet usando as coordenadas ajustadas
		return spriteSheet.getSubimage(x, y, spriteWidth, spriteHeight);
	}



	public Sprite getSprite(final int index) {
		return sprites[index];
	}

	public Sprite getSprite2(final int x, final int y) {
		return sprites[x + y * sheetWidthInSprites];
	}
}
