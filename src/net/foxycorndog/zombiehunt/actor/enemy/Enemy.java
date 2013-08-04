package net.foxycorndog.zombiehunt.actor.enemy;

import net.foxycorndog.jbiscuit.map.JMap;
import net.foxycorndog.zombiehunt.actor.Actor;

public class Enemy extends Actor
{
	private int	attack;
	
	/**
	 * 
	 * 
	 * @param map The map to add the actor to.
	 * @param width The width of the sprite of the Actor.
	 * @param height The height of the sprite of the Actor.
	 * @param speed The speed of the actor when walking.
	 * @param walkCyclePhases The number of phases that are included in
	 * 		the walk cycle.
	 * @param health The amount of health points the Enemy will have.
	 * @param attack The amount of attack points the Enemy will have.
	 */
	public Enemy(JMap map, int width, int height, float speed, int walkCyclePhases, float health, int attack)
	{
		super(map, width, height, speed, walkCyclePhases, health);
		
		this.attack = attack;
	}
	
	/**
	 * Get the amount of attack points the Enemy has.
	 * 
	 * @return The amount of attack points the Enemy has.
	 */
	public int getAttack()
	{
		return attack;
	}
}