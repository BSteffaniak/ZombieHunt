package net.foxycorndog.zombiehunt.entity.gun;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import net.foxycorndog.jfoxylib.openal.Sound;
import net.foxycorndog.zombiehunt.actor.Actor;
import net.foxycorndog.zombiehunt.entity.Entity;

/**
 * 
 * 
 * @author	Braden Steffaniak
 * @since	Aug 4, 2013 at 1:22:33 PM
 * @since	v
 * @version	Aug 4, 2013 at 1:22:33 PM
 * @version	v
 */
public class Gun extends Entity
{
	private int					rate;
	
	private float				velocity;
	private float				damage;
	
	private long				lastShot;
	
	private Actor				owner;
	
	private ArrayList<Bullet>	bullets;
	
	private static final Sound	shot;
	
	/**
	 * Load the sound.
	 */
	static
	{
		Sound sound = null;
		
		try
		{
			sound = new Sound("res/sounds/shot.wav");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		shot = sound;
	}
	
	/**
	 * Create a Gun with the specified stats.
	 * 
	 * @param velocity The velocity in which the bullets travel.
	 * @param damage The amount of damage the Bullet deals on a hit.
	 * @param rate The rate in which the Gun shoots. (How many ms pause
	 * 		before firing off another)
	 */
	public Gun(float velocity, float damage, int rate)
	{
		this.velocity = velocity;
		this.damage   = damage;
		
		this.rate     = rate;
		
		bullets       = new ArrayList<Bullet>();
	}
	
	/**
	 * Set the owner Actor of the Gun.
	 * 
	 * @param owner The owner of the Gun.
	 */
	public void setOwner(Actor owner)
	{
		this.owner = owner;
	}
	
	/**
	 * 
	 * 
	 * @param x
	 * @param y
	 */
	public void shoot(int x, int y)
	{
		long current = System.currentTimeMillis();
		
		if (current >= lastShot + rate)
		{
			float screenX = owner.getScreenX() + owner.getWidth()  / 2f;
			float screenY = owner.getScreenY() + owner.getHeight() / 2f;
			
			screenX *= owner.getMap().getScale();
			screenY *= owner.getMap().getScale();
			
			x = Math.round(x - screenX);
			y = Math.round(y - screenY);
			
			float actorX = owner.getX() + owner.getWidth()  / 2f;
			float actorY = owner.getY() + owner.getHeight() / 2f;
			
//			actorX *= owner.getMap().getScale();
//			actorY *= owner.getMap().getScale();
			
			Bullet bullet = new Bullet(actorX, actorY, actorX + x, actorY + y, this);
			
			bullets.add(bullet);
			
			lastShot = current;
			
//			shot.play();
		}
	}
	
	/**
	 * Get the rate in which the Gun shoots. (How many ms pause
	 * before firing off another)
	 * 
	 * @return The rate in which the Gun shoots. (How many ms pause
	 * 		before firing off another)
	 */
	public int getRate()
	{
		return rate;
	}
	
	/**
	 * Get the velocity in which the bullets travel.
	 * 
	 * @return The velocity in which the bullets travel.
	 */
	public float getVelocity()
	{
		return velocity;
	}
	
	/**
	 * Get the amount of damage the Bullet deals on a hit.
	 * 
	 * @return The amount of damage the Bullet deals on a hit.
	 */
	public float getDamage()
	{
		return damage;
	}
	
	
	/**
	 * Get the owner of the Gun.
	 * 
	 * @return The Actor instance that owns the Gun.
	 */
	public Actor getOwner()
	{
		return owner;
	}
	
	/**
	 * Render the bullets fired by the gun to the scene.
	 */
	public void render()
	{
		for (int i = 0; i < bullets.size(); i++)
		{
			Bullet bullet = bullets.get(i);
			
			bullet.render();
		}
	}
	
	/**
	 * 
	 * 
	 * @param delta
	 */
	public void update(float delta)
	{
		for (int i = bullets.size() - 1; i >= 0; i--)
		{
			Bullet bullet = bullets.get(i);
			
			if (bullet.isDisposed())
			{
				bullets.remove(i);
				
				continue;
			}
			
			bullet.update(delta);
		}
	}
}