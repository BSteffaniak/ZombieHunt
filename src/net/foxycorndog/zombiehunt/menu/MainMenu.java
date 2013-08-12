package net.foxycorndog.zombiehunt.menu;

import net.foxycorndog.jfoxylib.Color;
import net.foxycorndog.jfoxylib.components.Button;
import net.foxycorndog.jfoxylib.events.ButtonEvent;
import net.foxycorndog.jfoxylib.events.ButtonListener;
import net.foxycorndog.jfoxylib.font.Font;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.zombiehunt.ZombieHunt;

/**
 * 
 * 
 * @author	Braden Steffaniak
 * @since	Aug 4, 2013 at 8:32:42 PM
 * @since	v0.1
 * @version	Aug 4, 2013 at 8:32:42 PM
 * @version	v0.1
 */
public class MainMenu extends Menu
{
	private Button		play;
	private Button		options;
	private Button		credits;
	private Button		quit;
	
	private CreditsMenu	creditsMenu;
	private OptionsMenu	optionsMenu;
	
	public MainMenu(final ZombieHunt game)
	{
		play = new Button(this);
		play.setText("PLAY");
		play.setAlignment(Button.CENTER, Button.CENTER);
		play.setLocation(0, -9);
		play.setFont(ZombieHunt.getFont());
		
		options = new Button(this);
		options.setText("OPTIONS");
		options.setAlignment(Button.CENTER, Button.CENTER);
		options.setLocation(0, -18);
		options.setFont(ZombieHunt.getFont());
		
		credits = new Button(this);
		credits.setText("CREDITS");
		credits.setAlignment(Button.CENTER, Button.CENTER);
		credits.setLocation(0, -27);
		credits.setFont(ZombieHunt.getFont());
		
		quit = new Button(this);
		quit.setText("QUIT");
		quit.setAlignment(Button.CENTER, Button.CENTER);
		quit.setLocation(0, -36);
		quit.setFont(ZombieHunt.getFont());
		
		ButtonListener listener = new ButtonListener()
		{
			public void buttonUp(ButtonEvent event)
			{
				
			}
			
			public void buttonUnHovered(ButtonEvent event)
			{
				Button source = event.getSource();
				
				if (source == play)
				{
					play.setText("PLAY");
				}
				else if (source == options)
				{
					options.setText("OPTIONS");
				}
				else if (source == credits)
				{
					credits.setText("CREDITS");
				}
				else if (source == quit)
				{
					quit.setText("QUIT");
				}
			}
			
			public void buttonReleased(ButtonEvent event)
			{
				Button source = event.getSource();
				
				if (source == play)
				{
					game.startGame();
				}
				else if (source == options)
				{
					openOptionsMenu();
				}
				else if (source == credits)
				{
					openCreditsMenu();
				}
				else if (source == quit)
				{
					dispose();
					System.exit(0);
				}
			}
			
			public void buttonPressed(ButtonEvent event)
			{
				
			}
			
			public void buttonHovered(ButtonEvent event)
			{
				Button source = event.getSource();
				
				if (source == play)
				{
					play.setText("<PLAY>");
				}
				else if (source == options)
				{
					options.setText("<OPTIONS>");
				}
				else if (source == credits)
				{
					credits.setText("<CREDITS>");
				}
				else if (source == quit)
				{
					quit.setText("<QUIT>");
				}
			}
			
			public void buttonDown(ButtonEvent event)
			{
				
			}
		};
		
		play.addButtonListener(listener);
		options.addButtonListener(listener);
		credits.addButtonListener(listener);
		quit.addButtonListener(listener);
	}
	
	public void openOptionsMenu()
	{
		optionsMenu = new OptionsMenu(this);
		
		setEnabled(false);
	}
	
	public void closeOptionsMenu()
	{
		optionsMenu.dispose();
		optionsMenu = null;
		
		setEnabled(true);
	}
	
	public void openCreditsMenu()
	{
		creditsMenu = new CreditsMenu(this);
		
		setEnabled(false);
	}
	
	public void closeCreditsMenu()
	{
		creditsMenu.dispose();
		creditsMenu = null;
		
		setEnabled(true);
	}
	
	public void render()
	{
		GL.setTextureScaleMinMethod(GL.NEAREST);
		GL.setTextureScaleMagMethod(GL.NEAREST);
		
		GL.scale(5, 5, 1);
		
		if (optionsMenu != null)
		{
			optionsMenu.render();
		}
		else if (creditsMenu != null)
		{
			creditsMenu.render();
		}
		else
		{
			Color color = GL.getColor();
			
			GL.setColor(0.4f, 0.4f, 0.4f, 1);
			ZombieHunt.getFont().render("Zombie Hunt", 1, -1 + 25, 0, 2, Font.CENTER, Font.CENTER, null);
			GL.setColor(0, 0.75f, 0, 1);
			ZombieHunt.getFont().render("Zombie Hunt", 0, 0 + 25, 0, 2, Font.CENTER, Font.CENTER, null);
			
			GL.setColor(color);
			
			play.render();
			options.render();
			credits.render();
			quit.render();
		}
		
		GL.unscale();
	}
	
	public boolean dispose()
	{
		play.dispose();
		options.dispose();
		credits.dispose();
		quit.dispose();
		
		return super.dispose();
	}
}