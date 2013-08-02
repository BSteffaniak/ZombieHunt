package net.foxycorndog.zombiehunt.map;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.foxycorndog.jbiscuit.item.tile.JTileContainer;
import net.foxycorndog.jbiscuit.map.JImageChunk;
import net.foxycorndog.jbiscuit.map.JImageMap;
import net.foxycorndog.jfoxylib.components.Image;
import net.foxycorndog.jfoxylib.opengl.texture.Texture;
import net.foxycorndog.zombiehunt.tiles.Tile;

/**
 * 
 * 
 * @author	Braden Steffaniak
 * @since	Jul 29, 2013 at 5:00:39 PM
 * @since	v0.1
 * @version	Jul 29, 2013 at 5:00:39 PM
 * @version	v0.1
 */
public class Map extends JImageMap
{
	public static final int	TILE_SIZE = 8;
	
	/**
	 * Create a Map that will hold chunks that are the specified
	 * units large in size.
	 * 
	 * @param location The location of the image File to load the map
	 * 		from.
	 * @param container The JTileContainer that holds the Tiles used
	 * 		in the Map.
	 */
	public Map(String location, JTileContainer container) throws IOException
	{
		this(new File(location), container);
	}

	/**
	 * Create a Map that will hold chunks that are the specified
	 * units large in size.
	 * 
	 * @param file The image File to load the Map from.
	 * @param container The JTileContainer that holds the Tiles used
	 * 		in the Map.
	 */
	public Map(File file, JTileContainer container) throws IOException
	{
		this(ImageIO.read(file), container);
	}
	
	/**
	 * Create a Map that will hold chunks that are the specified
	 * units large in size.
	 * 
	 * @param image The BufferedImage to create the Map from.
	 * @param container The JTileContainer that holds the Tiles used
	 * 		in the Map.
	 */
	public Map(BufferedImage image, JTileContainer container)
	{
		super(image.getWidth() * TILE_SIZE, image.getHeight() * TILE_SIZE, container);
		
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		Graphics g = img.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		
		int pixels[] = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
		
		Texture mgTexture = new Texture(image.getWidth() * TILE_SIZE, image.getHeight() * TILE_SIZE);
		
		Image middleground = new Image(null);
		middleground.setTexture(mgTexture);
		
		addChunk(0, 0, new JImageChunk(this, 0, 0, null, middleground, null));
		
		for (int y = image.getHeight() - 1; y >= 0; y--)
		{
			for (int x = image.getWidth() - 1; x >= 0; x--)
			{
				int color = pixels[x + (image.getHeight() - y - 1) * image.getWidth()];
				
				Tile tile = Tile.getTileByColor(color);
				
				if (tile != null)
				{
					getChunk(0, 0).getLayerImage(JImageChunk.MIDDLEGROUND).getTexture().bind();
					
					int values[] = tile.getPixels();
					
					setPixels(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, JImageChunk.MIDDLEGROUND, values);
				}
			}
		}
	}
}