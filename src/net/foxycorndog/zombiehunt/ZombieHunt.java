package net.foxycorndog.zombiehunt;

import java.io.IOException;

import net.foxycorndog.jbiscuit.item.tile.JTileContainer;
import net.foxycorndog.jfoxylib.Frame;
import net.foxycorndog.jfoxylib.GameStarter;
import net.foxycorndog.zombiehunt.map.Map;
import net.foxycorndog.zombiehunt.tiles.Tile;

/**
 * 
 * 
 * @author	Braden Steffaniak
 * @since	Jul 29, 2013 at 4:54:23 PM
 * @since	v0.1
 * @version	Jul 29, 2013 at 4:54:23 PM
 * @version	v0.1
 */
public class ZombieHunt extends GameStarter
{
	private Map				map;
	
	public ZombieHunt()
	{
		
	}
	
	public static void main(String args[])
	{
		ZombieHunt game = new ZombieHunt();
		
		Frame.create(800, 600);
		Frame.setResizable(true);
		
		game.start();
	}

	/**
	 * @see net.foxycorndog.jfoxylib.GameStarter#init()
	 */
	public void init()
	{
		Tile.init();
		
		JTileContainer container = Tile.getTileContainer();
		
		try
		{
			map = new Map("res/images/maps/editedmap.png", container);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * @see net.foxycorndog.jfoxylib.GameStarter#render2D()
	 */
	public void render2D()
	{
		map.render();
	}

	/**
	 * @see net.foxycorndog.jfoxylib.GameStarter#render3D()
	 */
	public void render3D()
	{
		
	}

	/**
	 * @see net.foxycorndog.jfoxylib.GameStarter#update()
	 */
	public void update()
	{
		
	}
}