package net.foxycorndog.zombiehunt.actor.enemy;

import java.io.IOException;

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
 * @since	Aug 4, 2013 at 8:27:37 PM
 * @since	v0.1
 * @version	Aug 4, 2013 at 8:27:37 PM
 * @version	v0.1
 */
public class Greg extends Enemy
{
	private static boolean		initialized;
	
	private static SpriteSheet	sprites;
	
	private static Bundle		bundle;
	
	/**
	 * 
	 * 
	 * @param map The map to add the actor to.
	 */
	public Greg(Map map)
	{
		super(map, 12, 7, 1f, 2, 200, 4, 400);
		
		init();
		
		setSprites(sprites);
		
		setXOffset(1);
		setYOffset(1);
		setWidthOffset(-2);
		setHeightOffset(-2);
	}
	
	/**
	 * Initialize the Greg class.
	 */
	private static void init()
	{
		if (initialized)
		{
			return;
		}
		
		try
		{
			sprites = new SpriteSheet("res/images/actors/greg.png", 1, 1);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		bundle = new Bundle(3 * 2 * 2, 2, true, false);
		
		bundle.beginEditingVertices();
		{
			bundle.addVertices(GL.genRectVerts(0, 0, 12, 7));
			bundle.addVertices(GL.genRectVerts(0, 0, 12, 7));
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
			
			bundle.drawArrays(GL.TRIANGLES, 3 * 2 * getWalkCycle(), 3 * 2);
		}
		GL.popMatrix();
	}
	
	public void unbind()
	{
		bundle.endDraw();
	}
	
	/**
	 * @see net.foxycorndog.jbiscuit.actor.JActor#render()
	 * 
	 * @param bind Whether or not to bind the Buffers.
	 */
	public void render()
	{
		super.render();
		
		bind();
		
		draw();
		
		unbind();
	}
}