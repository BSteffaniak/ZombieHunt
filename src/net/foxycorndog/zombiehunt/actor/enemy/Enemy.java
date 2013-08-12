package net.foxycorndog.zombiehunt.actor.enemy;

import net.foxycorndog.jbiscuit.map.JMap;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.zombiehunt.actor.Actor;
import net.foxycorndog.zombiehunt.actor.Player;
import net.foxycorndog.zombiehunt.map.Map;

/**
 * 
 * 
 * @author	Braden Steffaniak
 * @since	Aug 4, 2013 at 8:27:02 PM
 * @since	v0.1
 * @version	Aug 4, 2013 at 8:27:02 PM
 * @version	v0.1
 */
public abstract class Enemy extends Actor
{
	private int		attack;
	
	private float	counter;
	
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
	 * @param stamina The amount of stamina the Actor has to do activities.
	 * @param attack The amount of attack points the Enemy will have.
	 */
	public Enemy(Map map, int width, int height, float speed, int walkCyclePhases, float health, float stamina, int attack)
	{
		super(map, width, height, speed, walkCyclePhases, health, stamina);
		
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
	
	public void moveTowardPlayer(float delta)
	{
		Player player  = ((Map)getMap()).getClosestPlayer(this);
		
		if (player == null)
		{
			return;
		}
		
		if (Map.distance(player, this) <= 1)
		{
//			player.damage(attack);
			
			return;
		}
		
		float  px = player.getX() + player.getWidth()  / 2f;
		float  py = player.getY() + player.getHeight() / 2f;
		
		float  mx      = getX() + getWidth()  / 2f;
		float  my      = getY() + getHeight() / 2f;
		
		px *= getMap().getScale();
		py *= getMap().getScale();
		
		mx *= getMap().getScale();
		my *= getMap().getScale();
		
		float offX  = px - mx;
		float offY  = py - my;
		
		float hypot = (float)Math.sqrt(offX * offX + offY * offY);
		
		float dx    = offX / hypot * getSpeed();
		float dy    = offY / hypot * getSpeed();
		
		dx *= delta;
		dy *= delta;
		
		tryMove(dx, 0);
		tryMove(0, dy);
		
		counter += getSpeed() * delta;
		
		if (counter >= 10)
		{
			counter = 0;
			
			incrementWalkCycle();
		}
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
	
	/**
	 * Rotate the Enemy toward the closest Actor in the Map.
	 */
	public void positionTowardPlayer()
	{
		Player player = ((Map)getMap()).getClosestPlayer(this);
		
		if (player == null)
		{
			return;
		}
		
		float  px = player.getX() + player.getWidth()  / 2f;
		float  py = player.getY() + player.getHeight() / 2f;
		
		float  mx = getX() + getWidth()  / 2f;
		float  my = getY() + getHeight() / 2f;
		
		px *= getMap().getScale();
		py *= getMap().getScale();
		
		mx *= getMap().getScale();
		my *= getMap().getScale();
		
		float offX = mx - px;
		float offY = my - py;
		
		updateRotation(offX, offY);
	}
	
	public void update(float delta)
	{
		moveTowardPlayer(delta);
		
		super.update(delta);
	}
}