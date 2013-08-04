package net.foxycorndog.zombiehunt.tiles;

import java.io.IOException;
import java.util.ArrayList;

import net.foxycorndog.jbiscuit.item.tile.JTile;
import net.foxycorndog.jbiscuit.item.tile.JTileContainer;
import net.foxycorndog.jfoxylib.opengl.texture.SpriteSheet;
import net.foxycorndog.zombiehunt.map.Map;

/**
 * 
 * 
 * @author	Braden Steffaniak
 * @since	Aug 2, 2013 at 9:24:33 PM
 * @since	v
 * @version	Aug 2, 2013 at 9:24:33 PM
 * @version	v
 */
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
		
		container.addTile(new Tile("Grass",        3, 0, 1, 1, false, 0x125E00));
		container.addTile(new Tile("Water",        9, 0, 1, 1, false, 0x2F71AD));
		container.addTile(new Tile("Road",         0, 0, 1, 1, false, 0x3B3B3B));
		container.addTile(new Tile("Sidewalk",     1, 0, 1, 1, false, 0x6E6E6E));
		container.addTile(new Tile("Carpet",       7, 0, 1, 1, false, 0xC8B98F));
		container.addTile(new Tile("Wooden Tile", 11, 0, 1, 1, false, 0xB08548));
		container.addTile(new Tile("Brick",        6, 0, 1, 1, true,  0x913100));
		container.addTile(new Tile("Driveway",     4, 0, 1, 1, false, 0x606070));
		container.addTile(new Tile("Sand",        10, 0, 1, 1, false, 0xFFECCD));
		container.addTile(new Tile("Wood",         2, 1, 1, 1, false, 0x855E1A));
//		container.addTile(new Tile("Grass", 3, 0, 1, 1, false, 0x125E00));
//		container.addTile(new Tile("Grass", 3, 0, 1, 1, false, 0x125E00));
//		container.addTile(new Tile("Grass", 3, 0, 1, 1, false, 0x125E00));
		
		initialized = true;
	}
	
	public int[] getPixels()
	{
		int pixels[] = null;
		
		SpriteSheet sprites = container.getSpriteSheet();
		
		pixels = sprites.getPixels(getX() * Map.TILE_SIZE, (sprites.getNumRows() - getY() - 1) * Map.TILE_SIZE, getCols() * Map.TILE_SIZE, getRows() * Map.TILE_SIZE);
		
		return pixels;
	}
	
	public int[] getColoredPixels()
	{
		int pixels[] = getPixels();
		
		int col = 0x010101 * (int)(Math.random() * 3) + 0x010101 * 80;
		
		for (int i = 0; i < pixels.length; i++)
		{
			pixels[i] = darkenColor(pixels[i], col);
			
//			if (pixels[i] < 0)
//			{
//				pixels[i] = 0;
//			}
		}
		
		return pixels;
	}
	
	private static int darkenColor(int col, int darken)
	{
		int r = (col >> 16) & 0xff;
		int g = (col >>  8) & 0xff;
		int b = (col      ) & 0xff;
		
		int dr = (darken >> 16) & 0xff;
		int dg = (darken >>  8) & 0xff;
		int db = (darken      ) & 0xff;
		
		int fr = r - dr;
		int fg = g - dg;
		int fb = b - db;
		
		if (fr < 0)
		{
			fr = 0;
		}
		if (fg < 0)
		{
			fg = 0;
		}
		if (fb < 0)
		{
			fb = 0;
		}
		
		int ar = fr * 0x10000;
		int ag = fg * 0x100;
		int ab = fb * 0x1;
		
		return (255 * 0x1000000) + ar + ag + ab;
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