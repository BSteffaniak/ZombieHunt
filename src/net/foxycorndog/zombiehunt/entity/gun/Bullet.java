package net.foxycorndog.zombiehunt.entity.gun;

import java.io.IOException;
import java.util.ArrayList;

import net.foxycorndog.jbiscuit.actor.JActor;
import net.foxycorndog.jbiscuit.item.JItemContainer;
import net.foxycorndog.jfoxylib.Color;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.opengl.bundle.Bundle;
import net.foxycorndog.jfoxylib.opengl.texture.SpriteSheet;
import net.foxycorndog.jfoxylib.util.Distance;
import net.foxycorndog.jfoxylib.util.Intersects;
import net.foxycorndog.jfoxyutil.LocationCollection;
import net.foxycorndog.zombiehunt.actor.Actor;
import net.foxycorndog.zombiehunt.map.Map;

/**
 * 
 * 
 * @author	Braden Steffaniak
 * @since	Aug 4, 2013 at 2:35:44 PM
 * @since	v0.1
 * @version	Aug 4, 2013 at 2:35:44 PM
 * @version	v0.1
 */
public class Bullet
{
	private boolean			disposed;
	
	private float			x, y;
	private float			startX, startY;
	private float			destX, destY;
	private float			rotation;
	
	private Gun				gun;
	
	private static boolean	initialized;
	
	private static Bundle	bundle;
	
	private static final int	WIDTH = 1, HEIGHT = 2;
	
	public Bullet(float x, float y, float destX, float destY, Gun gun)
	{
		init();
		
		this.x      = x;
		this.y      = y;
		this.startX = x;
		this.startY = y;
		
		this.destX  = destX;
		this.destY  = destY;
		
		this.gun    = gun;
		
		rotation    = -(float)Math.toDegrees(Math.atan2(destX - x, destY - y));
	}
	
	/**
	 * Initialize the Bullet class.
	 */
	private static void init()
	{
		if (initialized)
		{
			return;
		}
		
		bundle = new Bundle(3 * 2, 2, true, false);
		
		bundle.beginEditingVertices();
		{
			bundle.addVertices(GL.genRectVerts(0, 0, WIDTH, HEIGHT));
		}
		bundle.endEditingVertices();
		
		bundle.beginEditingTextures();
		{
			bundle.addTextures(GL.genRectTextures(GL.WHITE));
		}
		bundle.endEditingTextures();
		
		initialized = true;
	}
	
	/**
	 * Get whether or not the Bullet is disposed.
	 * 
	 * @return Whether or not the Bullet is disposed.
	 */
	public boolean isDisposed()
	{
		return disposed;
	}
	
	/**
	 * Dispose the Bullet and delete it.
	 */
	public void dispose()
	{
		disposed = true;
	}
	
	public void render()
	{
		GL.pushMatrix();
		{
			GL.translate(x, y, 0);
			
			Color color = GL.getColor();
			
			GL.setColor(0, 0, 0, 1);
			
			GL.translate(0.5f, 1, 0);
			GL.rotate(0, 0, rotation);
			GL.translate(-0.5f, -1, 0);
			
			bundle.render(GL.TRIANGLES, GL.WHITE);
			
			GL.setColor(color);
		}
		GL.popMatrix();
	}
	
	/**
	 * 
	 * 
	 * @param delta
	 */
	public void update(float delta)
	{
		float offX  = destX - startX;
		float offY  = destY - startY;
		
		float hypot = (float)Math.sqrt(offX * offX + offY * offY);
		
		float dx    = offX / hypot * gun.getVelocity();
		float dy    = offY / hypot * gun.getVelocity();
		
		int   num   = gun.getVelocity() > 1 ? (int)gun.getVelocity() : 1;
		
		boolean tc = isTileCollision(gun.getVelocity());
		boolean ac = isActorCollision(gun.getVelocity());
		
		if (!tc && !ac)
		{
			num = 0;
		}
		
		for (int i = 0; i <= num; i++)
		{
			if (num == 0)
			{
				x += dx;
				y += dy;
				
//				break;
			}
			else if (i == num)
			{
				x += (dx / num) * (gun.getVelocity() - num);
				y += (dy / num) * (gun.getVelocity() - num);
			}
			else
			{
				x += dx / num;
				y += dy / num;
			}
			
			if (x + WIDTH < 0 || y + HEIGHT < 0 ||  x >= gun.getOwner().getMap().getChunkWidth() || y >= gun.getOwner().getMap().getChunkHeight())
			{
				dispose();
				return;
			}
			else
			{
				if (isTileCollision())
				{
					dispose();
					return;
				}
				else if (isActorCollision())
				{
					dispose();
					return;
				}
			}
		}
	}
	
	private boolean isTileCollision()
	{
		return isTileCollision(0);
	}
	
	private boolean isTileCollision(float velocity)
	{
		Map map = (Map)gun.getOwner().getMap();
		JItemContainer container = gun.getOwner().getMap().getContainer();
		
		float max = Math.min(WIDTH, HEIGHT) + 0.05f * 2 + velocity * 2;
		
		float xv = x - 0.05f - velocity;
		float yv = y - 0.05f - velocity;
		
		float xt = xv  / container.getItemWidth();
		float yt = yv  / container.getItemHeight();
		float wt = max / container.getItemWidth();
		float ht = max / container.getItemHeight();
		
		int endX = (int)(xt + wt) + 1;
		int endY = (int)(yt + ht) + 1;
		
		for (float y = yt; y < endY; y++)
		{
			for (float x = xt; x < endX; x++)
			{
				if (map.isCollision((int)x, (int)y))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean isActorCollision()
	{
		return isActorCollision(0);
	}
	
	private boolean isActorCollision(float velocity)
	{
		LocationCollection<Actor> actors = ((Map)gun.getOwner().getMap()).getActorLocations();
//		ArrayList<JActor> actors = gun.getOwner().getMap().getActors();
		
		JItemContainer container = gun.getOwner().getMap().getContainer();
		
		float max = Math.min(WIDTH, HEIGHT) + 0.55f * 2 + velocity * 2;
		
		float xv = x - 0.55f - velocity;
		float yv = y - 0.55f - velocity;
		
		int xOff = Math.round((xv - max * 2f) / 3);
		int yOff = Math.round((yv - max * 2f) / 3);

		int wOff = Math.round(max * 4f / 3f + xOff);
		int hOff = Math.round(max * 4f / 3f + yOff);
		
		for (int y = yOff; y < hOff; y++)
		{
			for (int x = xOff; x < wOff; x++)
			{
				if (x < 0 || x >= actors.getWidth() || y < 0 || y >= actors.getHeight())
				{
					continue;
				}
				
				Actor actor = actors.get(x, y);
				
				if (actor == null || actor.isDisposed() || actor == gun.getOwner())
				{
					continue;
				}
				
//				if (Distance.points(xv, yv, actor.getX() + actor.getXOffset() + (actor.getWidth() + actor.getWidthOffset()) / 2f, actor.getY() + actor.getYOffset() + (actor.getHeight() + actor.getHeightOffset()) / 2f) < actor.getWidth() / 2f)
				if (Intersects.rectangles(xv, yv, max, max, actor.getX() + actor.getXOffset(), actor.getY() + actor.getYOffset(), actor.getWidth() + actor.getWidthOffset(), actor.getHeight() + actor.getHeightOffset()))
				{
					if (velocity == 0 && Distance.points(xv + max / 2, yv + max / 2, actor.getX() + actor.getXOffset() + (actor.getWidth() + actor.getWidthOffset()) / 2f, actor.getY() + actor.getYOffset() + (actor.getHeight() + actor.getHeightOffset()) / 2f) <= actor.getWidth() / 1.3f)
					{
						actor.damage(gun.getDamage());
						
						if (!actor.isAlive())
						{
							gun.getOwner().incrementKills();
						}
					}
					
					return true;
				}
			}
		}
		
//		for (int i = actors.size() - 1; i >= 0; i--)
//		{
//			Actor actor = (Actor)actors.get(i);
//			
//			if (actor != gun.getOwner())
//			{
//				if (Intersects.rectangles(xv, yv, max, max, actor.getX() + actor.getXOffset(), actor.getY() + actor.getYOffset(), actor.getWidth() + actor.getWidthOffset(), actor.getHeight() + actor.getHeightOffset()))
//				{
//					actor.damage(gun.getDamage());
//					
//					if (!actor.isAlive())
//					{
//						gun.getOwner().incrementKills();
//					}
//					
//					return true;
//				}
//			}
//		}
		
		return false;
	}
	
	/**
	 * Get the sign of the specified number.
	 * 
	 * @param num The number to get the sign of.
	 * @return
	 */
	private static int sign(float num)
	{
		return num < 0 ? -1 : 1;
	}
}