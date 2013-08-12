package net.foxycorndog.zombiehunt.map;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.ImageIO;

import net.foxycorndog.jbiscuit.actor.JActor;
import net.foxycorndog.jbiscuit.item.tile.JTileContainer;
import net.foxycorndog.jbiscuit.map.JImageChunk;
import net.foxycorndog.jbiscuit.map.JImageMap;
import net.foxycorndog.jfoxylib.Color;
import net.foxycorndog.jfoxylib.Frame;
import net.foxycorndog.jfoxylib.components.Image;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.opengl.texture.Texture;
import net.foxycorndog.jfoxylib.util.Distance;
import net.foxycorndog.jfoxylib.util.Intersects;
import net.foxycorndog.jfoxyutil.LocationCollection;
import net.foxycorndog.zombiehunt.actor.Actor;
import net.foxycorndog.zombiehunt.actor.Player;
import net.foxycorndog.zombiehunt.actor.enemy.Enemy;
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
	private int									blood[];
	
	private Image								bloodImage;
	
	private Image								minimap;
	
	private LocationCollection<Actor>			actors;
	
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
		
		minimap = new Image(null);
		minimap.setTexture(img);
		
		int pixels[] = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
		
		Texture mgTexture = new Texture(image.getWidth() * TILE_SIZE, image.getHeight() * TILE_SIZE);
		Image middleground = new Image(null);
		middleground.setTexture(mgTexture);

		Texture bgTexture = new Texture(image.getWidth() * TILE_SIZE, image.getHeight() * TILE_SIZE);
		Image background = new Image(null);
		background.setTexture(bgTexture);
		
		Texture fgTexture = new Texture(image.getWidth() * TILE_SIZE, image.getHeight() * TILE_SIZE);
		Image foreground = new Image(null);
		foreground.setTexture(fgTexture);
		
		addChunk(0, 0, new JImageChunk(this, 0, 0, background, middleground, foreground));
		
		for (int y = image.getHeight() - 1; y >= 0; y--)
		{
			for (int x = image.getWidth() - 1; x >= 0; x--)
			{
				int color = pixels[x + (image.getHeight() - y - 1) * image.getWidth()];
				
				Tile tile = Tile.getTileByColor(color);
				
				if (tile != null)
				{
					int layer = 0;
					
					if (tile.isCollidable())
					{
						layer = JImageChunk.FOREGROUND;
					}
					else
					{
						layer = JImageChunk.BACKGROUND;
					}
					
					getChunk(0, 0).getLayerImage(layer).getTexture().bind();
					
					int values[] = tile.getColoredPixels();
					
					setPixels(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, layer, values);
					
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
		
		actors = new LocationCollection<Actor>(image.getWidth() * TILE_SIZE / 3, image.getHeight() * TILE_SIZE / 3);
		
		BufferedImage bloodImg = null;
		
		try
		{
			bloodImg = ImageIO.read(new File("res/images/blood.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		BufferedImage bloodImg2 = new BufferedImage(bloodImg.getWidth(), bloodImg.getHeight(), BufferedImage.BITMASK);
		
		g = bloodImg2.createGraphics();
		g.drawImage(bloodImg, 0, 0, null);
		g.dispose();
		
		bloodImage = new Image(null);
		bloodImage.setTexture(bloodImg2);
		
		blood = ((DataBufferInt)bloodImg2.getRaster().getDataBuffer()).getData();
	}
	
	public int getWidth()
	{
		return minimap.getTexture().getWidth();
	}

	public int getHeight()
	{
		return minimap.getTexture().getHeight();
	}
	
	/**
	 * 
	 * @return
	 */
	public LocationCollection<Actor> getActorLocations()
	{
		return actors;
	}
	
	/**
	 * Get the Image that portrays a pool of blood.
	 * 
	 * @return The Image instance.
	 */
	public Image getBloodImage()
	{
		return bloodImage;
	}
	
	public void addBloodStain(Actor actor)
	{
		int x = (actor.getWidth()  - bloodImage.getTexture().getWidth())  / 2 + Math.round(actor.getX());
		int y = (actor.getHeight() - bloodImage.getTexture().getHeight()) / 2 + Math.round(actor.getY());
		
		Image image = getChunk(0, 0).getLayerImage(JImageChunk.BACKGROUND);
		
		image.getTexture().bind();
		image.getTexture().setPixels(x, y, bloodImage.getTexture().getWidth(), bloodImage.getTexture().getHeight(), blood, true);
	}
	
	public void renderActors()
	{
		ArrayList<JActor> actors = getActors(); 
		
		for (int i = 0; i < actors.size(); i++)
		{
			Actor actor = (Actor)actors.get(i);
			
			if (i == 1)
			{
				actor.bind();
			}
			if (i >= 1)
			{
				actor.draw();
			}
			else
			{
				actor.render();
			}
		}
	}
	
	private void renderMinimap(Player player)
	{
		GL.pushMatrix();
		{
			GL.unscale();
			
			GL.translate(0, 0, 5);
			
			int x      = 10;
			int y      = 10;
			int width  = 150;
			int height = 150;
			
			int borderSize = 5;
			
			float scale = 2;
			
			Color color = GL.getColor();
			GL.setColor(0, 0, 0, 1);
			
			GL.translate(x - borderSize, Frame.getHeight() - height - y - borderSize, 0);
			GL.scale(width + borderSize * 2, height + borderSize * 2, 1);
			GL.WHITE_IMAGE.render();
			GL.unscale();
			GL.untranslate();
			
			GL.translate(0, 0, 5);
			
			GL.setColor(1, 1, 1, 1);
			
			GL.scale(scale, scale, 1);
			GL.translateIgnoreScale(x, Frame.getHeight() - height - y, 0);
			GL.translateIgnoreScale((-player.getX() / TILE_SIZE) * scale, (-player.getY() / TILE_SIZE) * scale, 0);
			GL.translateIgnoreScale(width / 2f, height / 2f, 0);
			
			GL.beginFrameClipping(x, Frame.getHeight() - height - y, width, width);
			{
				minimap.render();
			}
			GL.endFrameClipping();
			
			GL.setColor(color);
		}
		GL.popMatrix();
	}
	
	public void render(Player player)
	{
		super.render();
		
		renderMinimap(player);
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
				if (isCollision((int)x, (int)y))
				{
					return true;
				}
			}
		}
		
		Actor a = (Actor)actor;
		
		int xOff = Math.round((a.getX() - a.getWidth()  * 1f) / 3);
		int yOff = Math.round((a.getY() - a.getHeight() * 1f) / 3);

		int wOff = Math.round(a.getWidth()  * 2f / 3f + xOff);
		int hOff = Math.round(a.getHeight() * 2f / 3f + yOff);
		
		for (int y = yOff; y < hOff; y++)
		{
			for (int x = xOff; x < wOff; x++)
			{
				if (x < 0 || x >= actors.getWidth() || y < 0 || y >= actors.getHeight())
				{
					continue;
				}
				
				Actor a2 = actors.get(x, y);
				
				if (a2 == null || a2.isDisposed() || a == a2)
				{
					continue;
				}
				
				if (actorCollided(a, a2))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean actorCollided(Actor actor, Actor actor2)
	{
		float delta = 60f / Frame.getFPS();
		
		//if (rectangles(actor.getX() + actor.getXOffset(), actor.getY() + actor.getYOffset(), actor.getWidth() + actor.getWidthOffset(), actor.getHeight() + actor.getHeightOffset(), actor2.getX() + actor2.getXOffset(), actor2.getY() + actor2.getYOffset(), actor2.getWidth() + actor2.getWidthOffset(), actor2.getHeight() + actor2.getHeightOffset()))
		if (distance(actor, actor2) < actor.getWidth() / 2)
		{
			if (actor2 instanceof Player)
			{
				if (actor instanceof Enemy)
				{
					Enemy enemy = (Enemy)actor;
					
					float amount = enemy.getAttack() * delta;
					
					actor2.damage(amount);
				}
			}
			
			return true;
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
	
	/**
	 * Get the Player instance that is closest to the specified Actor.
	 * 
	 * @param actor The Actor to find the closest Player to.
	 * @return The Closest Player to the specified Actor.
	 */
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
	
	public void update(float delta)
	{
		ArrayList<JActor> actors = getActors();
		
		for (int i = actors.size() - 1; i >= 0; i--)
		{
			Actor actor = (Actor)actors.get(i);
			
			if (actor.isDisposed())
			{
				getActorLocations().remove(Math.round(actor.getX() / 3), Math.round(actor.getY() / 3));
				actors.remove(i);
				
				if (players.contains(actor))
				{
					players.remove(actor);
				}
			}
		}
		
		super.update(delta);
	}
	
	/**
	 * Get the distance between the two Actors.
	 * 
	 * @param a1 The first Actor.
	 * @param a2 The second Actor.
	 * @return The distance between the Actors.
	 */
	public static float distance(Actor a1, Actor a2)
	{
		return (float)Distance.points(a1.getX() + a1.getWidth() / 2f, a1.getY() + a1.getHeight() / 2f, a2.getX() + a2.getWidth() / 2f, a2.getY() + a2.getHeight() / 2f);
	}
}