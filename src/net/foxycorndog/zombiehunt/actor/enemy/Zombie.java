package net.foxycorndog.zombiehunt.actor.enemy;

import java.io.IOException;

import net.foxycorndog.jbiscuit.map.JMap;
import net.foxycorndog.jfoxylib.input.Mouse;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.opengl.bundle.Bundle;
import net.foxycorndog.jfoxylib.opengl.texture.SpriteSheet;
import net.foxycorndog.zombiehunt.actor.Player;
import net.foxycorndog.zombiehunt.map.Map;

public class Zombie extends Enemy
{
	private static boolean		initialized;
	
	private static SpriteSheet	sprites;
	
	private static Bundle		bundle;
	
	/**
	 * 
	 * 
	 * @param map The map to add the actor to.
	 */
	public Zombie(JMap map)
	{
		super(map, 8, 4, 1.5f, 2, 40, 1);
		
		init();
		
		setSprites(sprites);
		
		setXOffset(1);
		setYOffset(1);
		setWidthOffset(-2);
		setHeightOffset(-2);
	}
	
	/**
	 * Initialize the Player class.
	 */
	private static void init()
	{
		if (initialized)
		{
			return;
		}
		
		try
		{
			sprites = new SpriteSheet("res/images/actors/zombie.png", 1, 1);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		bundle = new Bundle(3 * 2 * 2, 2, true, false);
		
		bundle.beginEditingVertices();
		{
			bundle.addVertices(GL.genRectVerts(0, 0, 8, 4));
			bundle.addVertices(GL.genRectVerts(0, 0, 8, 4));
		}
		bundle.endEditingVertices();
		
		bundle.beginEditingTextures();
		{
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(0, 0, 1, 1)));
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(0, 0, 1, 1), true, false));
		}
		bundle.endEditingTextures();
		
		initialized = true;
	}
	
	/**
	 * @see net.foxycorndog.jbiscuit.actor.JActor#render()
	 */
	public void render()
	{
		GL.pushMatrix();
		{
			Player player  = ((Map)getMap()).getClosestPlayer(this);
			
			float  screenX = player.getX() + player.getWidth()  / 2f;
			float  screenY = player.getY() + player.getHeight() / 2f;
			
			float  mx      = getX() + getWidth()  / 2f;
			float  my      = getY() + getHeight() / 2f;
			
			screenX *= getMap().getScale();
			screenY *= getMap().getScale();
			
			mx *= getMap().getScale();
			my *= getMap().getScale();
			
			float offX = mx - screenX;
			float offY = my - screenY;
			
			updateRotation(offX, offY);
			
			GL.translate(getX(), getY(), 0);
			
			GL.translate(getWidth() / 2f, getHeight() / 2f, 0);
			GL.rotate(0, 0, getRotation());
			GL.translate(-getWidth() / 2f, -getHeight() / 2f, 0);
			
			bundle.render(GL.TRIANGLES, 3 * 2 * getWalkCycle(), 3 * 2, sprites);
		}
		GL.popMatrix();
	}
}