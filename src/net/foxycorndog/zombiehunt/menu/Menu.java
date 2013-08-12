package net.foxycorndog.zombiehunt.menu;

import net.foxycorndog.jfoxylib.Frame;
import net.foxycorndog.jfoxylib.components.Panel;
import net.foxycorndog.jfoxylib.events.FrameEvent;
import net.foxycorndog.jfoxylib.events.FrameListener;

/**
 * 
 * 
 * @author	Braden Steffaniak
 * @since	Aug 4, 2013 at 8:27:42 PM
 * @since	v0.1
 * @version	Aug 4, 2013 at 8:27:42 PM
 * @version	v0.1
 */
public abstract class Menu extends Panel
{
	private FrameListener listener;
	
	public Menu()
	{
		super(null);
		
		setSize(Frame.getWidth(), Frame.getHeight());
		
		listener = new FrameListener()
		{
			public void frameResized(FrameEvent e)
			{
				setSize(Frame.getWidth(), Frame.getHeight());
			}
		};
		
		Frame.addFrameListener(listener);
	}

	public abstract void render();
	
	public boolean dispose()
	{
		return Frame.removeFrameListener(listener);
	}
}