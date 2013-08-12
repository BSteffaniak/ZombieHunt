package net.foxycorndog.zombiehunt.menu;

import net.foxycorndog.jfoxylib.Color;
import net.foxycorndog.jfoxylib.Frame;
import net.foxycorndog.jfoxylib.components.Button;
import net.foxycorndog.jfoxylib.events.ButtonEvent;
import net.foxycorndog.jfoxylib.events.ButtonListener;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.zombiehunt.ZombieHunt;

public class PauseMenu extends Menu
{
	private Button		resume;
	private Button		restart;
	private Button		options;
	private Button		quit;
	
	private OptionsMenu	optionsMenu;
	
	public PauseMenu(final ZombieHunt parent)
	{
		resume = new Button(this);
		resume.setText("RESUME");
		resume.setAlignment(Button.CENTER, Button.CENTER);
		resume.setLocation(0, 15);
		resume.setFont(ZombieHunt.getFont());
		
		restart = new Button(this);
		restart.setText("RESTART");
		restart.setAlignment(Button.CENTER, Button.CENTER);
		restart.setLocation(0, 5);
		restart.setFont(ZombieHunt.getFont());
		
		options = new Button(this);
		options.setText("OPTIONS");
		options.setAlignment(Button.CENTER, Button.CENTER);
		options.setLocation(0, -5);
		options.setFont(ZombieHunt.getFont());
		
		quit = new Button(this);
		quit.setText("QUIT");
		quit.setAlignment(Button.CENTER, Button.CENTER);
		quit.setLocation(0, -15);
		quit.setFont(ZombieHunt.getFont());
		
		ButtonListener listener = new ButtonListener()
		{
			public void buttonUp(ButtonEvent event)
			{
				
			}
			
			public void buttonUnHovered(ButtonEvent event)
			{
				Button source = event.getSource();
				
				if (source == resume)
				{
					resume.setText("RESUME");
				}
				else if (source == restart)
				{
					restart.setText("RESTART");
				}
				else if (source == options)
				{
					options.setText("OPTIONS");
				}
				else if (source == quit)
				{
					quit.setText("QUIT");
				}
			}
			
			public void buttonReleased(ButtonEvent event)
			{
				Button source = event.getSource();
				
				if (source == resume)
				{
					parent.closePauseMenu();
				}
				else if (source == restart)
				{
					parent.quitGame();
					parent.startGame();
				}
				else if (source == options)
				{
					openOptionsMenu();
				}
				else if (source == quit)
				{
					parent.quitGame();
					parent.openMainMenu();
				}
			}
			
			public void buttonPressed(ButtonEvent event)
			{
				
			}
			
			public void buttonHovered(ButtonEvent event)
			{
				Button source = event.getSource();
				
				if (source == resume)
				{
					resume.setText("<RESUME>");
				}
				else if (source == restart)
				{
					restart.setText("<RESTART>");
				}
				else if (source == options)
				{
					options.setText("<OPTIONS>");
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
		
		resume.addButtonListener(listener);
		restart.addButtonListener(listener);
		options.addButtonListener(listener);
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
	
	public void render()
	{
		GL.pushMatrix();
		{
			GL.setTextureScaleMinMethod(GL.NEAREST);
			GL.setTextureScaleMagMethod(GL.NEAREST);
			
			GL.translate(0, 0, 7);
			
			GL.scale(5, 5, 1);
			
			GL.pushMatrix();
			{
				GL.scale(Frame.getWidth(), Frame.getHeight(), 1);
				
				Color color = GL.getColor();
				GL.setColor(0, 0, 0, 0.5f);
				
				GL.WHITE_IMAGE.render();
				
				GL.setColor(color);
			}
			GL.popMatrix();
			
			if (optionsMenu != null)
			{
				optionsMenu.render();
			}
			else
			{
				resume.render();
				restart.render();
				options.render();
				quit.render();
			}
		}
		GL.popMatrix();
	}
	
	public boolean dispose()
	{
		resume.dispose();
		restart.dispose();
		options.dispose();
		quit.dispose();
		
		return super.dispose();
	}
}