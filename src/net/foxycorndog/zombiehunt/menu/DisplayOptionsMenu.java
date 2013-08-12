package net.foxycorndog.zombiehunt.menu;

import net.foxycorndog.jfoxylib.Frame;
import net.foxycorndog.jfoxylib.components.Button;
import net.foxycorndog.jfoxylib.events.ButtonEvent;
import net.foxycorndog.jfoxylib.events.ButtonListener;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.zombiehunt.ZombieHunt;

public class DisplayOptionsMenu extends Menu
{
	private Button	toggleGamma;
	private Button	toggleVSync;
	private Button	back;
	
	public DisplayOptionsMenu(final OptionsMenu parent)
	{
		toggleGamma = new Button(this);
		toggleGamma.setText("TOGGLE GAMMA");
		toggleGamma.setAlignment(Button.CENTER, Button.CENTER);
		toggleGamma.setLocation(0, 10);
		toggleGamma.setFont(ZombieHunt.getFont());
		
		toggleVSync = new Button(this);
		toggleVSync.setText("VSYNC: " + getVSyncText());
		toggleVSync.setAlignment(Button.CENTER, Button.CENTER);
		toggleVSync.setLocation(0, 0);
		toggleVSync.setFont(ZombieHunt.getFont());
		
		back = new Button(this);
		back.setText("BACK");
		back.setAlignment(Button.CENTER, Button.CENTER);
		back.setLocation(0, -10);
		back.setFont(ZombieHunt.getFont());
		
		ButtonListener listener = new ButtonListener()
		{
			public void buttonUp(ButtonEvent event)
			{
				
			}
			
			public void buttonUnHovered(ButtonEvent event)
			{
				Button source = event.getSource();
				
				if (source == toggleGamma)
				{
					toggleGamma.setText("TOGGLE GAMMA");
				}
				else if (source == toggleVSync)
				{
					toggleVSync.setText("VSYNC: " + getVSyncText());
				}
				else if (source == back)
				{
					back.setText("BACK");
				}
			}
			
			public void buttonReleased(ButtonEvent event)
			{
				Button source = event.getSource();
				
				if (source == toggleGamma)
				{
					
				}
				else if (source == toggleVSync)
				{
					Frame.setVSyncEnabled(!Frame.isVSyncEnabled());
					
					toggleVSync.setText("<VSYNC: " + getVSyncText() + ">");
				}
				else if (source == back)
				{
					parent.closeDisplayOptionsMenu();
				}
			}
			
			public void buttonPressed(ButtonEvent event)
			{
				
			}
			
			public void buttonHovered(ButtonEvent event)
			{
				Button source = event.getSource();
				
				if (source == toggleGamma)
				{
					toggleGamma.setText("<TOGGLE GAMMA>");
				}
				else if (source == toggleVSync)
				{
					toggleVSync.setText("<VSYNC: " + getVSyncText() + ">");
				}
				else if (source == back)
				{
					back.setText("<BACK>");
				}
			}
			
			public void buttonDown(ButtonEvent event)
			{
				
			}
		};
		
		toggleGamma.addButtonListener(listener);
		toggleVSync.addButtonListener(listener);
		back.addButtonListener(listener);
	}
	
	/**
	 * Get the text to display if vsync is on or not.
	 * 
	 * @return "ON" of vsync is on, "OFF" if it is not.
	 */
	private String getVSyncText()
	{
		return Frame.isVSyncEnabled() ? "ON" : "OFF";
	}
	
	public void render()
	{
		toggleGamma.render();
		toggleVSync.render();
		back.render();
	}
	
	public boolean dispose()
	{
		toggleGamma.dispose();
		toggleVSync.dispose();
		back.dispose();
		
		return super.dispose();
	}
}