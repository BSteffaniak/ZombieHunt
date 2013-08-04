package net.foxycorndog.zombiehunt.actor;

import net.foxycorndog.jbiscuit.actor.JActor;
import net.foxycorndog.jbiscuit.map.JMap;

/**
 * 
 * 
 * @author	Braden Steffaniak
 * @since	Aug 3, 2013 at 10:57:20 AM
 * @since	v
 * @version	Aug 3, 2013 at 10:57:20 AM
 * @version	v
 */
public class Actor extends JActor
{
	private float	health;
	private float	rotation;
	
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
	 */
	public Actor(JMap map, int width, int height, float speed, int walkCyclePhases, float health)
	{
		super(map, width, height, speed, 0, walkCyclePhases);
		
		this.health = health;
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
}