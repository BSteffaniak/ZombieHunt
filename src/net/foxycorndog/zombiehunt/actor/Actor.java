package net.foxycorndog.zombiehunt.actor;

import java.util.ArrayList;

import net.foxycorndog.jbiscuit.actor.JActor;
import net.foxycorndog.jbiscuit.map.JMap;
import net.foxycorndog.zombiehunt.entity.gun.Gun;
import net.foxycorndog.zombiehunt.map.Map;

/**
 * 
 * 
 * @author	Braden Steffaniak
 * @since	Aug 3, 2013 at 10:57:20 AM
 * @since	v
 * @version	Aug 3, 2013 at 10:57:20 AM
 * @version	v
 */
public abstract class Actor extends JActor
{
	private boolean			disposed;

	private int				kills;
	
	private float			health, maxHealth;
	private float			stamina, maxStamina;
	private float			rotation;
	
	private Map				map;
	
	private ArrayList<Gun>	guns;
	
	/**
	 * 
	 * 
	 * @param map The map to add the actor to.
	 * @param width The width of the sprite of the Actor.
	 * @param height The height of the sprite of the Actor.
	 * @param speed The speed of the actor when walking.
	 * @param walkCyclePhases The number of phases that are included in
	 * 		the walk cycle.
	 * @param health The amount of health points the Actor will have.
	 * @param stamina The amount of stamina the Actor has to do activities.
	 */
	public Actor(Map map, int width, int height, float speed, int walkCyclePhases, float health, float stamina)
	{
		super(map, width, height, speed, 0, walkCyclePhases);
		
		this.health     = health;
		this.maxHealth  = health;

		this.stamina    = stamina;
		this.maxStamina = stamina;
		
		this.map        = map;
		
		guns = new ArrayList<Gun>();
	}
	
	/**
	 * Get the amount of stamina points the Actor has.
	 * 
	 * @return The amount of stamina points the Actor has.
	 */
	public float getStamina()
	{
		return stamina;
	}
	
	/**
	 * Get the max amount of stamina points the Actor can possibly have.
	 * 
	 * @return The max amount of stamina points the Actor can possibly
	 * 		have.
	 */
	public float getMaxStamina()
	{
		return maxStamina;
	}
	
	/**
	 * Get whether or not the Actor is alive.
	 * 
	 * @return Whether or not the Actor is alive.
	 */
	public boolean isAlive()
	{
		return health > 0;
	}
	
	/**
	 * Get the amount of health points the Actor has.
	 * 
	 * @return The amount of health points the Actor has.
	 */
	public float getHealth()
	{
		return health;
	}
	
	/**
	 * Get the max amount of health points the Actor can possibly have.
	 * 
	 * @return The max amount of health points the Actor can possibly
	 * 		have.
	 */
	public float getMaxHealth()
	{
		return maxHealth;
	}
	
	/**
	 * Get the current rotation that the Actor is facing with.
	 * 
	 * @return The current rotation that the Actor is facing with.
	 */
	public float getRotation()
	{
		return rotation;
	}
	
	/**
	 * Update the rotation of the Actor to face toward the specified
	 * offset location.
	 * 
	 * @param x The horizontal offset to rotate toward.
	 * @param y The vertical offset to rotate toward.
	 */
	public void updateRotation(float x, float y)
	{
		rotation = -(float)Math.toDegrees(Math.atan2(x, y));
	}
	
	/**
	 * Add the specified Gun instance to the Actors gun inventory.
	 * 
	 * @param gun The Gun instance to add.
	 */
	public void addGun(Gun gun)
	{
		gun.setOwner(this);
		
		guns.add(gun);
	}
	
	/**
	 * Deal the specified amount of damage to the Actor.
	 * 
	 * @param amount The amount of health to take away.
	 */
	public void damage(float amount)
	{
		health -= amount;
		
		if (health <= 0)
		{
			health = 0;
			
			map.addBloodStain(this);
			
			dispose();
		}
	}
	
	/**
	 * Get the number of kills that the Actor has.
	 * 
	 * @return The number of kills that the Actor has.
	 */
	public int getKills()
	{
		return kills;
	}
	
	/**
	 * Add to the number of kills by 1.
	 */
	public void incrementKills()
	{
		kills++;
	}
	
	/**
	 * Get whether or not the Actor is disposed.
	 * 
	 * @return Whether or not the Actor is disposed.
	 */
	public boolean isDisposed()
	{
		return disposed;
	}
	
	/**
	 * Dispose the Actor and delete it.
	 */
	public void dispose()
	{
		map.getActorLocations().remove(Math.round(getX() / 3), Math.round(getY() / 3));
		
		disposed = true;
	}
	
	public void setLocation(float x, float y)
	{
		int oldX = Math.round(getX() / 3);
		int oldY = Math.round(getY() / 3);
		
		super.setLocation(x, y);
		
		int newX = Math.round(getX() / 3);
		int newY = Math.round(getY() / 3);
		
		if (map.getActorLocations().get(oldX, oldY) != null)
		{
			map.getActorLocations().move(oldX, oldY, newX, newY, this);
		}
		else
		{
			map.getActorLocations().put(newX, newY, this);
		}
	}
	
	public void move(float dx, float dy)
	{
		if (getX() + dx < 0)
		{
			setLocation(0, getY());
			
			dx = 0;
		}
		else if (getX() + (getWidth() + getWidthOffset()) + dx >= map.getChunk(0, 0).getWidth())
		{
			setLocation(map.getChunk(0, 0).getWidth() - (getWidth() + getWidthOffset()), getY());
			
			dx = 0;
		}
		if (getY() + dy < 0)
		{
			setLocation(getX(), 0);
			
			dy = 0;
		}
		else if (getY() + (getHeight() + getHeightOffset()) + dy >= map.getChunk(0, 0).getHeight())
		{
			setLocation(getX(), map.getChunk(0, 0).getHeight() - (getHeight() + getHeightOffset()));
			
			dy = 0;
		}
		
		int oldX = Math.round(getX() / 3);
		int oldY = Math.round(getY() / 3);
		
		super.move(dx, dy);
		
		map.getActorLocations().move(oldX, oldY, Math.round(getX() / 3), Math.round(getY() / 3), this);
	}
	
	public abstract void bind();
	
	public abstract void draw();
	
	public abstract void unbind();
	
	public abstract void before();
	
	public abstract void after();

	/**
	 * Render the Actor and its component to the scene.
	 */
	public void render()
	{
		renderBullets();
	}
	
	/**
	 * Render the Bullets fired by this Actor to the scene.
	 */
	public void renderBullets()
	{
		for (int i = 0; i < guns.size(); i++)
		{
			Gun gun = guns.get(i);
			
			gun.render();
		}
	}
	
	/**
	 * @see net.foxycorndog.jbiscuit.actor.JActor#update()
	 */
	public void update(float delta)
	{
		super.update(delta);
		
		for (int i = 0; i < guns.size(); i++)
		{
			Gun gun = guns.get(i);
			
			gun.update(delta);
		}
		
		if (isSprinting())
		{
			stamina -= 2 * delta;
			
			if (stamina < 0)
			{
				stamina = 0;
			}
		}
		else
		{
			stamina += 0.5f * delta;
			
			if (stamina > maxStamina)
			{
				stamina = maxStamina;
			}
		}
		
		health += 0.5f * delta;
		
		if (health > maxHealth)
		{
			health = maxHealth;
		}
	}
}