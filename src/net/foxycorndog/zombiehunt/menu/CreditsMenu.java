package net.foxycorndog.zombiehunt.menu;

import net.foxycorndog.jfoxylib.Frame;
import net.foxycorndog.jfoxylib.components.Button;
import net.foxycorndog.jfoxylib.events.ButtonEvent;
import net.foxycorndog.jfoxylib.events.ButtonListener;
import net.foxycorndog.jfoxylib.font.Font;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.util.Bounds2f;
import net.foxycorndog.zombiehunt.ZombieHunt;

public class CreditsMenu extends Menu
{
	private float	scrollY;
	
	private Button	back;
	
	public CreditsMenu(final MainMenu parent)
	{
		back = new Button(this);
		back.setText("BACK");
		back.setAlignment(Button.CENTER, Button.BOTTOM);
		back.setLocation(0, 10);
		back.setFont(ZombieHunt.getFont());
		
		ButtonListener listener = new ButtonListener()
		{
			public void buttonUp(ButtonEvent event)
			{
				
			}
			
			public void buttonUnHovered(ButtonEvent event)
			{
				Button source = event.getSource();
				
				if (source == back)
				{
					back.setText("BACK");
				}
			}
			
			public void buttonReleased(ButtonEvent event)
			{
				Button source = event.getSource();
				
				if (source == back)
				{
					parent.closeCreditsMenu();
				}
			}
			
			public void buttonPressed(ButtonEvent event)
			{
				
			}
			
			public void buttonHovered(ButtonEvent event)
			{
				Button source = event.getSource();
				
				if (source == back)
				{
					back.setText("<BACK>");
				}
			}
			
			public void buttonDown(ButtonEvent event)
			{
				
			}
		};
		
		back.addButtonListener(listener);
	}
	
	public void render()
	{
		float delta = Frame.getDelta();
		
		GL.pushMatrix();
		{
			GL.unscale();
			
			GL.beginFrameClipping(0, back.getDisplayY() + back.getDisplayHeight(), getWidth(), getHeight());
			{
				float scale = 5;
				
				ZombieHunt.getFont().render(
						"Lead dev:\nBraden Steffaniak\n\n\n\n" +
						"Lead Artist:\nBraden Steffaniak\n\n\n\n" +
						"Music:\nBraden Steffaniak\n\n\n\n" +
						"©2013 All rights not reserved yet",
						0, scrollY - Frame.getHeight() + 40, 0, scale, Font.CENTER, Font.TOP, this);
			}
			GL.endFrameClipping();
		}
		GL.popMatrix();
		
		scrollY += 1.25f * delta;
		
		back.render();
	}

	public boolean dispose()
	{
		back.dispose();
		
		return super.dispose();
	}
}