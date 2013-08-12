package net.foxycorndog.zombiehunt.actor.enemy;

import java.io.IOException;
import java.util.ArrayList;

import net.foxycorndog.jbiscuit.map.JMap;
import net.foxycorndog.jfoxylib.input.Mouse;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.opengl.bundle.Bundle;
import net.foxycorndog.jfoxylib.opengl.texture.SpriteSheet;
import net.foxycorndog.zombiehunt.actor.Player;
import net.foxycorndog.zombiehunt.map.Map;

/**
 * 
 * 
 * @author	Braden Steffaniak
 * @since	Aug 4, 2013 at 8:28:02 PM
 * @since	v0.1
 * @version	Aug 4, 2013 at 8:28:02 PM
 * @version	v0.1
 */
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
	public Zombie(Map map)
	{
		super(map, 8, 4, 1.2f, 2, 40, 1, 2);
		
		init();
		
		setSprites(sprites);
		
		setXOffset(1);
		setYOffset(1);
		setWidthOffset(-2);
		setHeightOffset(-2);
	}
	
	/**
	 * Initialize the Zombie class.
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

	public void bind()
	{
		bundle.beginDraw(sprites);
	}

	public void draw()
	{
		GL.pushMatrix();
		{
			positionTowardPlayer();
			
			before();
			bundle.drawArrays(GL.TRIANGLES, 3 * 2 * getWalkCycle(), 3 * 2);
			after();
		}
		GL.popMatrix();
	}
	
	public void unbind()
	{
		bundle.endDraw();
	}
	
	/**
	 * @see net.foxycorndog.jbiscuit.actor.JActor#render()
	 */
	public void render()
	{
		super.render();
		
		bind();
		
		draw();
		
		unbind();
	}
}