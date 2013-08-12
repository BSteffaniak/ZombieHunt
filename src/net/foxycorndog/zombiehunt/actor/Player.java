package net.foxycorndog.zombiehunt.actor;

import java.io.IOException;

import net.foxycorndog.jbiscuit.map.JMap;
import net.foxycorndog.jfoxylib.input.Mouse;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.opengl.bundle.Bundle;
import net.foxycorndog.jfoxylib.opengl.texture.SpriteSheet;
import net.foxycorndog.zombiehunt.map.Map;

/**
 * 
 * 
 * @author	Braden Steffaniak
 * @since	Aug 2, 2013 at 9:24:29 PM
 * @since	v
 * @version	Aug 2, 2013 at 9:24:29 PM
 * @version	v
 */
public class Player extends Actor
{
	private static boolean		initialized;
	
	private static SpriteSheet	sprites;
	
	private static Bundle		bundle;
	
	/**
	 * @param map The map to add the actor to.
	 */
	public Player(Map map)
	{
		super(map, 8, 4, 1, 2, 1000, 1000);
		
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
			sprites = new SpriteSheet("res/images/actors/player.png", 1, 1);
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
	
	private void positionTowardMouse()
	{
		int   mx      = Mouse.getX();
		int   my      = Mouse.getY();
		
		float screenX = getScreenX() + getWidth()  / 2f;
		float screenY = getScreenY() + getHeight() / 2f;
		
		screenX *= getMap().getScale();
		screenY *= getMap().getScale();
		
		float offX    = mx - screenX;
		float offY    = my - screenY;
		
		updateRotation(offX, offY);
	}
	
	public void before()
	{
		GL.translate(getX(), getY(), 0);
		
		GL.translate(getWidth() / 2f, getHeight() / 2f, 0);
		GL.rotate(0, 0, getRotation());
		GL.translate(-getWidth() / 2f, -getHeight() / 2f, 0);
	}
	
	public void after()
	{
		
	}
	
	public void bind()
	{
		bundle.beginDraw(sprites);
	}

	public void draw()
	{
		GL.pushMatrix();
		{
			positionTowardMouse();
			
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