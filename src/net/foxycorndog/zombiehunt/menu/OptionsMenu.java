package net.foxycorndog.zombiehunt.menu;

import net.foxycorndog.jfoxylib.Frame;
import net.foxycorndog.jfoxylib.components.Button;
import net.foxycorndog.jfoxylib.events.ButtonEvent;
import net.foxycorndog.jfoxylib.events.ButtonListener;
import net.foxycorndog.zombiehunt.ZombieHunt;

public class OptionsMenu extends Menu
{
	private Button				displayOptions;
	private Button				back;
	
	private DisplayOptionsMenu	displayOptionsMenu;
	
	public OptionsMenu(final Object parent)
	{
		displayOptions = new Button(this);
		displayOptions.setText("DISPLAY OPTIONS");
		displayOptions.setAlignment(Button.CENTER, Button.CENTER);
		displayOptions.setLocation(0, 10);
		displayOptions.setFont(ZombieHunt.getFont());
		
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
				
				if (source == displayOptions)
				{
					displayOptions.setText("DISPLAY OPTIONS");
				}
				else if (source == back)
				{
					back.setText("BACK");
				}
			}
			
			public void buttonReleased(ButtonEvent event)
			{
				Button source = event.getSource();
				
				if (source == displayOptions)
				{
					openDisplayOptionsMenu();
				}
				else if (source == back)
				{
					if (parent instanceof MainMenu)
					{
						((MainMenu)parent).closeOptionsMenu();
					}
					else if (parent instanceof PauseMenu)
					{
						((PauseMenu)parent).closeOptionsMenu();
					}
				}
			}
			
			public void buttonPressed(ButtonEvent event)
			{
				
			}
			
			public void buttonHovered(ButtonEvent event)
			{
				Button source = event.getSource();
				
				if (source == displayOptions)
				{
					displayOptions.setText("<DISPLAY OPTIONS>");
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
		
		displayOptions.addButtonListener(listener);
		back.addButtonListener(listener);
	}
	
	public void openDisplayOptionsMenu()
	{
		displayOptionsMenu = new DisplayOptionsMenu(this);
		
		setEnabled(false);
	}
	
	public void closeDisplayOptionsMenu()
	{
		displayOptionsMenu.dispose();
		displayOptionsMenu = null;
		
		setEnabled(true);
	}
	
	public void render()
	{
		if (displayOptionsMenu != null)
		{
			displayOptionsMenu.render();
		}
		else
		{
			displayOptions.render();
			back.render();
		}
	}
	
	public boolean dispose()
	{
		displayOptions.dispose();
		back.dispose();
		
		return super.dispose();
	}
}