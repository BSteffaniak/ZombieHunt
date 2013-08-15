package net.foxycorndog.zombiehunt;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.foxycorndog.jbiscuit.item.tile.JTileContainer;
import net.foxycorndog.jfoxylib.Color;
import net.foxycorndog.jfoxylib.Frame;
import net.foxycorndog.jfoxylib.GameStarter;
import net.foxycorndog.jfoxylib.components.Image;
import net.foxycorndog.jfoxylib.events.KeyEvent;
import net.foxycorndog.jfoxylib.events.KeyListener;
import net.foxycorndog.jfoxylib.font.Font;
import net.foxycorndog.jfoxylib.input.Keyboard;
import net.foxycorndog.jfoxylib.input.Mouse;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.opengl.bundle.Bundle;
import net.foxycorndog.jfoxylib.opengl.texture.SpriteSheet;
import net.foxycorndog.jfoxylib.util.ImageData;
import net.foxycorndog.jfoxylib.util.ResourceLocator;
import net.foxycorndog.zombiehunt.actor.Actor;
import net.foxycorndog.zombiehunt.actor.Player;
import net.foxycorndog.zombiehunt.actor.enemy.BigJohn;
import net.foxycorndog.zombiehunt.actor.enemy.Greg;
import net.foxycorndog.zombiehunt.actor.enemy.Zombie;
import net.foxycorndog.zombiehunt.entity.gun.Glock;
import net.foxycorndog.zombiehunt.entity.gun.Gun;
import net.foxycorndog.zombiehunt.entity.gun.MachineGun;
import net.foxycorndog.zombiehunt.map.Map;
import net.foxycorndog.zombiehunt.menu.MainMenu;
import net.foxycorndog.zombiehunt.menu.PauseMenu;
import net.foxycorndog.zombiehunt.tiles.Tile;

/**
 * 
 * 
 * @author	Braden Steffaniak
 * @since	Jul 29, 2013 at 4:54:23 PM
 * @since	v0.1
 * @version	Jul 29, 2013 at 4:54:23 PM
 * @version	v0.1
 */
public class ZombieHunt extends GameStarter
{
	private float			counter;
	private float			startSceneTimer;
	private float			deathSceneTimer;
	private float			deathMenuTimer;
	private float			bloodPulse;
	private	float			mapScale;
	
	private Gun				gun;
	
	private Player			player;
	
	private Map				map;
	
	private MainMenu		mainMenu;
	private PauseMenu		pauseMenu;
	
	private KeyListener		keyListener;
	
	private static boolean	initialized;
	
	private static Bundle	squareBundle;
	
	private static Font		font;
	
	public ZombieHunt()
	{
		
	}
	
	public static void main(String args[])
	{
		ZombieHunt game = new ZombieHunt();
		
		Frame.create(1280, 756);
//		Frame.create(800, 600);
		Frame.setResizable(true);
		
		game.start();
	}
	
	/**
	 * Initialize the static data if not already initialized.
	 */
	private static void initStatic()
	{
		if (initialized)
		{
			return;
		}
		
		squareBundle = new Bundle(3 * 2, 2, true, false);
		
		squareBundle.beginEditingVertices();
		{
			squareBundle.addVertices(GL.genRectVerts(0, 0, 1, 1));
		}
		squareBundle.endEditingVertices();
		
		squareBundle.beginEditingTextures();
		{
			squareBundle.addTextures(GL.genRectTextures(GL.WHITE));
		}
		squareBundle.endEditingTextures();
		
		SpriteSheet sprites = null;
		ImageData   data    = null;
		
		try
		{
			BufferedImage image = ImageIO.read(new File("res/images/fonts/font.png"));
			
			sprites = new SpriteSheet(image, 26, 4);
			data    = new ImageData(image);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		font = new Font(sprites,
				new char[]
				{
					'A', 'B', 'C', 'D', 'E', 'F',  'G', 'H', 'I', 'J', 'K', 'L',  'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
					'a', 'b', 'c', 'd', 'e', 'f',  'g', 'h', 'i', 'j', 'k', 'l',  'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
					'0', '1', '2', '3', '4', '5',  '6', '7', '8', '9', '_', '-',  '+', '=', '~', '`', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')',
					'?', '>', '<', ';', ':', '\'', '"', '{', '}', '[', ']', '\\', '|', ',', '.', '/', '©', '¨', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '
				}, data);

		font.setLetterMargin(1);
		font.setTrimBounds(true);
		
		initialized = true;
	}
	
	/**
	 * Start the game to play.
	 */
	public void startGame()
	{
		closePauseMenu();
		
		Tile.init();
		
		JTileContainer container = Tile.getTileContainer();
		
		try
		{
			map = new Map("res/images/maps/editedmap.png", container);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		player = new Player(map);
		player.setLocation(1190, 975);
		map.addPlayer(player);

		gun = new MachineGun();
		player.addGun(gun);
		
		for (int y = 100; y < map.getHeight() * Map.TILE_SIZE; y += 80)
		{
			for (int x = 100; x < map.getWidth() * Map.TILE_SIZE; x += 80)
			{
				Zombie zombie = new Zombie(map);
				
				zombie.setLocation(x, y);
				
				map.addActor(zombie);
			}
		}
		
		if (mainMenu != null)
		{
			mainMenu.dispose();
			mainMenu = null;
		}
		
		mapScale = 4;
		
		startSceneTimer = 10;
		deathSceneTimer = 0;
		
		keyListener = new KeyListener()
		{
			public void keyTyped(KeyEvent event)
			{
				int code = event.getKeyCode();
				
				if (code == Keyboard.KEY_ESCAPE)
				{
					if (pauseMenu == null)
					{
						openPauseMenu();
					}
					else
					{
						closePauseMenu();
					}
				}
			}
			
			public void keyReleased(KeyEvent event)
			{
				
			}
			
			public void keyPressed(KeyEvent event)
			{
				
			}
			
			public void keyDown(KeyEvent event)
			{
				
			}
		};
		
		Keyboard.addKeyListener(keyListener);
	}
	
	public void quitGame()
	{
		player = null;
		map    = null;
		gun    = null;
		
		Keyboard.removeKeyListener(keyListener);
	}
	
	/**
	 * @see net.foxycorndog.jfoxylib.GameStarter#init()
	 */
	public void init()
	{
		initStatic();
		
		mainMenu = new MainMenu(this);
	}
	
	public void openPauseMenu()
	{
		pauseMenu = new PauseMenu(this);
	}
	
	public void closePauseMenu()
	{
		if (pauseMenu == null)
		{
			return;
		}
		
		pauseMenu.dispose();
		pauseMenu = null;
	}
	
	public void openMainMenu()
	{
		closePauseMenu();
		
		mainMenu = new MainMenu(this);
	}
	
	public void closeMainMenu()
	{
		mainMenu.dispose();
		mainMenu = null;
	}
	
	private void renderHealth()
	{
		GL.translate(0, 0, 6);
		
		Color color = GL.getColor();
		
		GL.translate(10, 40, 0);
		GL.scale(200, 20, 1);
		GL.setColor(0, 0, 0, 1);
		squareBundle.render(GL.TRIANGLES, GL.WHITE);
		
		GL.unscale();
		
		if (player.isAlive())
		{
			GL.translate(3, 3, 0);
			GL.scale(194 * (player.getHealth() / player.getMaxHealth()), 14, 1);
			GL.setColor(1, 0, 0, 1);
			squareBundle.render(GL.TRIANGLES, GL.WHITE);
			
			GL.unscale();
		}
		
		GL.untranslate();
		
		GL.setColor(color);
	}
	
	private void renderStamina()
	{
		GL.translate(0, 0, 6);
		
		Color color = GL.getColor();
		
		GL.translate(10, 10, 0);
		GL.scale(200, 20, 1);
		GL.setColor(0, 0, 0, 1);
		squareBundle.render(GL.TRIANGLES, GL.WHITE);
		
		GL.unscale();
		
		if (player.getStamina() > 0)
		{
			GL.translate(3, 3, 0);
			GL.scale(194 * (player.getStamina() / player.getMaxStamina()), 14, 1);
			GL.setColor(1, 1, 0, 1);
			squareBundle.render(GL.TRIANGLES, GL.WHITE);
		}
		
		GL.unscale();
		GL.untranslate();
		
		GL.setColor(color);
	}
	
	private void renderStartScene()
	{
		if (startSceneTimer <= 1)
		{
			return;
		}
		
		float delta = Frame.getDelta();
		
		float scale = startSceneTimer;
		
		GL.scale(scale, scale, 1);
		
		startSceneTimer -= startSceneTimer * (0.15 * delta);
	}
	
	private void renderDeathScene()
	{
		float delta = Frame.getDelta();
		
		float scale = (deathSceneTimer + 1) / 2;
		
		GL.scale(scale, scale, 1);
		
		GL.translate(player.getWidth() * 2f, -player.getHeight() * 2f, 0);
		
		GL.rotate(0, 0, deathSceneTimer);
		
		deathSceneTimer += (0.15 * delta) / (scale);
		
		GL.setColor(1, deathSceneTimer / 10, deathSceneTimer / 10, 1);
	}

	/**
	 * @see net.foxycorndog.jfoxylib.GameStarter#render2D()
	 */
	public void render2D()
	{
		float delta = Frame.getDelta();
		
		if (mainMenu != null)
		{
			mainMenu.render();
		}
		else
		{
			GL.pushMatrix();
			{
				if (!player.isAlive())
				{
					renderDeathScene();
				}
				else
				{
					renderStartScene();
					
					float redAmount = player.getMaxHealth() - player.getHealth();
					
					redAmount /= player.getMaxHealth();
					
					GL.setColor(1, 1 - redAmount, 1 - redAmount, 1);
				}
				
				GL.scale(mapScale, mapScale, 1);
				
				player.center(GL.getAmountScaled()[0]);
				
				map.render(player);
				
				GL.untranslate();
				GL.unrotate();
				GL.unscale();
				
				renderHealth();
				renderStamina();
				
				font.render("Kills: " + player.getKills(), 0, 0, 6, 3, Font.RIGHT, Font.TOP, null);
				
				if (player.isAlive())
				{
					GL.pushMatrix();
					{
						Image bloodImage = map.getBloodImage();
						
						GL.translate(0, 0, 6);
						
						float scale = 1;
							
						if (bloodPulse >= 1)
						{
							scale *= 0.9f;
						}
						
						if (Frame.getWidth() > Frame.getHeight())
						{
							scale *= Frame.getHeight() / bloodImage.getWidth();
						}
						else
						{
							scale *= Frame.getWidth() / bloodImage.getWidth();
						}
							
						GL.translate((Frame.getWidth() - bloodImage.getWidth() * scale) / 2, (Frame.getHeight() - bloodImage.getWidth() * scale) / 2, 0);
						
						float alpha = player.getMaxHealth() - player.getHealth();
						
						alpha /= player.getMaxHealth() * 2;
						alpha += 0.5f;
						
						GL.scale(scale, scale, 1);
						
						Color color = GL.getColor();
						
						GL.setColor(1, 1, 1, alpha - 0.5f);
						
						bloodImage.render();
						
						GL.setColor(color);
						
						if (player.getHealth() < player.getMaxHealth())
						{
							bloodPulse += 0.1f * delta;
							
							if (bloodPulse > 2)
							{
								bloodPulse = 0;
							}
						}
						else
						{
							bloodPulse = 0;
						}
					}
					GL.popMatrix();
				}
			}
			GL.popMatrix();
		
			if (!player.isAlive())
			{
				if (pauseMenu == null && deathSceneTimer >= 7f)
				{
					deathMenuTimer = 6;
					
					openPauseMenu();
				}
				
				float amount = deathMenuTimer / 25f;
				amount *= delta;
				
				float scale = deathMenuTimer -= amount;
				
				if (scale > 1)
				{
					GL.scale(scale, scale, 1);
				}
			}
		}
		
		if (pauseMenu != null)
		{
			pauseMenu.render();
		}
	}

	/**
	 * @see net.foxycorndog.jfoxylib.GameStarter#render3D()
	 */
	public void render3D()
	{
		
	}

	/**
	 * @see net.foxycorndog.jfoxylib.GameStarter#update()
	 */
	public void update()
	{
		if (pauseMenu != null)
		{
			
		}
		else if (mainMenu != null)
		{
			
		}
		else
		{
			if (map.getScale() > 0 && Frame.getFPS() > 0)
			{
				float delta = Frame.getDelta();
				
				if (!Float.isNaN(delta))
				{
					if (player.isAlive())
					{
						if (Mouse.isButtonDown(Mouse.LEFT_MOUSE_BUTTON))
						{
							gun.shoot(Mouse.getX(), Mouse.getY());
						}
						
						if (Keyboard.isKeyDown(Keyboard.KEY_LEFT_SHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT_SHIFT))
						{
							player.setSprinting(player.getStamina() > 0);
						}
						else
						{
							player.setSprinting(false);
						}
						
						float speed = delta;
						
						if (Keyboard.isKeyDown(Keyboard.KEY_W))
						{
							player.moveUp(speed);
						}
						else if (Keyboard.isKeyDown(Keyboard.KEY_S))
						{
							player.moveDown(speed);
						}
						if (Keyboard.isKeyDown(Keyboard.KEY_A))
						{
							player.moveLeft(speed);
						}
						else if (Keyboard.isKeyDown(Keyboard.KEY_D))
						{
							player.moveRight(speed);
						}
						
						if (player.isMoving())
						{
							counter += delta;
							
							if (counter >= 10)
							{
								counter = 0;
								
								player.incrementWalkCycle();
							}
						}
						else
						{
							counter = 0;
							
//							player.setWalkCycle(1);
						}
					}

					map.update(delta);
				}
			}
		}
	}
	
	/**
	 * Get the ZombieHunt font.
	 * 
	 * @return The ZombieHunt font.
	 */
	public static Font getFont()
	{
		return font;
	}
}