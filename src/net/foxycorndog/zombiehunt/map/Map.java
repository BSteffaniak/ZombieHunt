package net.foxycorndog.zombiehunt.map;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.imageio.ImageIO;

import net.foxycorndog.jbiscuit.actor.JActor;
import net.foxycorndog.jbiscuit.item.tile.JTileContainer;
import net.foxycorndog.jbiscuit.map.JImageChunk;
import net.foxycorndog.jbiscuit.map.JImageMap;
import net.foxycorndog.jfoxylib.components.Image;
import net.foxycorndog.jfoxylib.opengl.texture.Texture;
import net.foxycorndog.zombiehunt.actor.Actor;
import net.foxycorndog.zombiehunt.actor.Player;
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
	private ArrayList<Player>					players;
	
	private HashMap<Integer, HashSet<Integer>>	collisions;
	
	public static final int						TILE_SIZE = 8;
	
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
		
		players    = new ArrayList<Player>();
		
		collisions = new HashMap<Integer, HashSet<Integer>>();
		
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
					
					int values[] = tile.getColoredPixels();
					
					setPixels(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, JImageChunk.MIDDLEGROUND, values);
					
					if (tile.isCollidable())
					{
						if (!collisions.containsKey(x))
						{
							collisions.put(x, new HashSet<Integer>());
						}
						
						collisions.get(x).add(y);
					}
				}
			}
		}
	}
	
	/**
	 * Get whether the Tile at the specified location is collidable.
	 * 
	 * @param x The horizontal location of the Tile.
	 * @param y The vertical location of the Tile.
	 * @return Whether or not the Tile is collidable.
	 */
	public boolean isCollision(int x, int y)
	{
		if (collisions.containsKey(x))
		{
			return collisions.get(x).contains(y);
		}
		
		return false;
	}
	
	/**
	 * Checks whether there is a collision with the Actor and any of the
	 * Chunks.
	 * 
	 * @param actor The Actor to check collisions on.
	 * @return Whether there is a collision.
	 */
	public boolean isCollision(JActor actor)
	{
		float actorX      = actor.getX() + actor.getXOffset();
		float actorY      = actor.getY() + actor.getYOffset();
		float actorWidth  = actor.getWidth() + actor.getWidthOffset();
		float actorHeight = actor.getHeight() + actor.getHeightOffset();
		
		float xt          = actorX / getContainer().getItemWidth();
		float yt          = actorY / getContainer().getItemHeight();
		float wt          = actorWidth  / getContainer().getItemWidth();
		float ht          = actorHeight / getContainer().getItemHeight();
		
		int endX = (int)Math.ceil(xt + wt);
		int endY = (int)Math.ceil(yt + ht);
		
		for (float y = yt; y < endY; y++)
		{
			for (float x = xt; x < endX; x++)
			{
				if (isCollision((int)Math.floor(x), (int)Math.floor(y)))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Get all of the Players within the Map.
	 * 
	 * @return An ArrayList of the Players in the Map.
	 */
	public ArrayList<Player> getPlayers()
	{
		return players;
	}
	
	public Player getClosestPlayer(Actor actor)
	{
		float  min    = Float.MAX_VALUE;
		Player player = null;
		
		for (int i = 0; i < players.size(); i++)
		{
			float dist = distance(actor, players.get(i));
			
			if (dist < min)
			{
				min = dist;
				
				player = players.get(i);
			}
		}
		
		return player;
	}
	
	/**
	 * Add the specified Player to the list of Players in the Map.
	 * 
	 * @param player The Player to add to the Map.
	 */
	public void addPlayer(Player player)
	{
		players.add(player);
		
		addActor(player);
	}
	
	/**
	 * Get the distance between the two Actors.
	 * 
	 * @param a1 The first Actor.
	 * @param a2 The second Actor.
	 * @return The distance between the Actors.
	 */
	private static float distance(Actor a1, Actor a2)
	{
		return (float)Math.sqrt(Math.pow(a1.getX() - a2.getX(), 2) + Math.pow(a1.getY() - a2.getY(), 2));
	}
}