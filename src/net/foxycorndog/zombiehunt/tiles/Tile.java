package net.foxycorndog.zombiehunt.tiles;

import java.io.IOException;
import java.util.ArrayList;

import net.foxycorndog.jbiscuit.item.tile.JTile;
import net.foxycorndog.jbiscuit.item.tile.JTileContainer;
import net.foxycorndog.jfoxylib.opengl.texture.SpriteSheet;
import net.foxycorndog.zombiehunt.map.Map;

public class Tile extends JTile
{
	private int						color;
	
	private static JTileContainer	container;
	
	private static boolean			initialized;
	
	public Tile(String name, int x, int y, int cols, int rows, boolean collidable, int color)
	{
		super(name, x, y, cols, rows, collidable, 0, container);
		
		this.color = color;
	}
	
	public static void init()
	{
		if (initialized)
		{
			return;
		}
		
		container = new JTileContainer(8, 8);
		
		SpriteSheet sprites = null;
		
		try
		{
			sprites = new SpriteSheet("res/images/terrain/tiles.png", 12, 12);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		container.setSpriteSheet(sprites);
		
		container.addTile(new Tile("Grass",    3, 0, 1, 1, false, 0x125E00));
		container.addTile(new Tile("Water",    9, 0, 1, 1, false, 0x2F71AD));
		container.addTile(new Tile("Road",     0, 0, 1, 1, false, 0x3B3B3B));
		container.addTile(new Tile("Sidewalk", 1, 0, 1, 1, false, 0x6E6E6E));
		container.addTile(new Tile("Carpet",   7, 0, 1, 1, false, 0xC8B98F));
//		container.addTile(new Tile("Grass", 3, 0, 1, 1, false, 0x125E00));
//		container.addTile(new Tile("Grass", 3, 0, 1, 1, false, 0x125E00));
//		container.addTile(new Tile("Grass", 3, 0, 1, 1, false, 0x125E00));
//		container.addTile(new Tile("Grass", 3, 0, 1, 1, false, 0x125E00));
//		container.addTile(new Tile("Grass", 3, 0, 1, 1, false, 0x125E00));
//		container.addTile(new Tile("Grass", 3, 0, 1, 1, false, 0x125E00));
//		container.addTile(new Tile("Grass", 3, 0, 1, 1, false, 0x125E00));
//		container.addTile(new Tile("Grass", 3, 0, 1, 1, false, 0x125E00));
	}
	
	public int[] getPixels()
	{
		int pixels[] = null;
		
		SpriteSheet sprites = container.getSpriteSheet();
		
		pixels = sprites.getPixels(getX() * Map.TILE_SIZE, (sprites.getNumRows() - getY() - 1) * Map.TILE_SIZE, getCols() * Map.TILE_SIZE, getRows() * Map.TILE_SIZE);
		
		return pixels;
	}
	
	public static JTileContainer getTileContainer()
	{
		return container;
	}
	
	/**
	 * Get the Tile that is linked to the specified color.
	 * 
	 * @param color The integer representation of the color.
	 * @return The Tile linked to the specified color.
	 */
	public static Tile getTileByColor(int color)
	{
		ArrayList<JTile> tiles = container.getTiles();
		
		for (int i = 0; i < tiles.size(); i++)
		{
			Tile tile = (Tile)tiles.get(i);
			
			if (tile.color == color)
			{
				return tile;
			}
		}
		
		return null;
	}
}