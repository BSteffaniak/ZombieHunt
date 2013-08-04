package net.foxycorndog.zombiehunt;

import java.io.IOException;

import net.foxycorndog.jbiscuit.item.tile.JTileContainer;
import net.foxycorndog.jfoxylib.Frame;
import net.foxycorndog.jfoxylib.GameStarter;
import net.foxycorndog.jfoxylib.input.Keyboard;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.opengl.bundle.Bundle;
import net.foxycorndog.zombiehunt.actor.Actor;
import net.foxycorndog.zombiehunt.actor.Player;
import net.foxycorndog.zombiehunt.actor.enemy.BigJohn;
import net.foxycorndog.zombiehunt.actor.enemy.Zombie;
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
	private float	counter;
	
	private Player	player;
	
	private Map		map;
	
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
		
		player = new Player(map);
		player.setLocation(0, 0);
		map.addPlayer(player);

		BigJohn z = new BigJohn(map);
		map.addActor(z);
		
		BigJohn z2 = new BigJohn(map);
		z2.setLocation(100, 100);
		map.addActor(z2);

		Zombie z33 = new Zombie(map);
		z33.setLocation(50, 50);
		map.addActor(z33);
		
		Zombie z233 = new Zombie(map);
		z233.setLocation(0, 100);
		map.addActor(z233);
	}

	/**
	 * @see net.foxycorndog.jfoxylib.GameStarter#render2D()
	 */
	public void render2D()
	{
		float scale = 4;
		
		GL.scale(scale, scale, 1);
		
		player.center(scale);
		
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
		if (Frame.getFPS() > 0)
		{
			float delta = 60f / Frame.getFPS();
			
			if (!Float.isNaN(delta))
			{
				float speed = delta;
				
				if (Keyboard.isKeyDown(Keyboard.KEY_W))
				{
					player.moveUp(speed);
				}
				else if (Keyboard.isKeyDown(Keyboard.KEY_S))
				{
					player.moveDown(speed);
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_A))
				{
					player.moveLeft(speed);
				}
				else if (Keyboard.isKeyDown(Keyboard.KEY_D))
				{
					player.moveRight(speed);
				}
				
				if (player.isMoving())
				{
					counter += delta;
					
					if (counter >= 10)
					{
						counter = 0;
						
						player.incrementWalkCycle();
					}
				}
				else
				{
					counter = 0;
					
//					player.setWalkCycle(1);
				}
				
				player.update(delta);
			}
		}
	}
}