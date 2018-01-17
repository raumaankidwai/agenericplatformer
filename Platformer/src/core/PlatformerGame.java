package core;
import java.awt.*;

import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;

import java.util.List;
import java.util.*;
import java.util.concurrent.*;

import javax.sound.sampled.*;

import org.dyn4j.dynamics.*;
import org.dyn4j.geometry.*;

import framework.*;
import objects.*;

public class PlatformerGame extends Game {
	// Global references
	public World world;
	public double scale;
	
	public GameObject player;
	
	// Constants
	public double MAX_VELOCITY = 15;
	public boolean LOOP_AROUND = false;
	public boolean LOSABLE = true;
	
	// Screens
	protected enum Screen {
		START,
		GAME,
		NEWLEVEL,
		WIN,
		LOSE
	}
	
	public Screen screen;
	
	// Drawing variables
	protected int offset = 3;
	protected int roundness = 15;
	
	protected String[] compliments = {
		"Awesome!",
		"Great job!",
		"Amazing!",
		"Unbelievable!",
		"Perfect!"
	};
	
	// Booleans
	protected boolean jumping = false;
	protected boolean spring = false;
	
	// Levels
	protected ArrayList<PlatformerLevel> levels = new ArrayList<PlatformerLevel>();
	protected int curLevel = 0;
	
	public void init (World world, double scale) {
		this.world = world;
		this.scale = scale;
		
		start();
	}
	
	public void gameLoop (World world, double scale) {
		if (screen != Screen.GAME) {
			return;
		}
		
		// Jumping & bouncing
		// Whenever player isn't touching a platform, `jumping` is set to true, and the player can't jump.
		jumping = true;
		spring = false;
		
		for (Body body: world.getBodies()) {
			try {
				if (body instanceof Platform && body.isInContact(player)) {
					if (body instanceof NWJPlatform) {
						if (player.getWorldCenter().y - body.getWorldCenter().y > 0.49) {
							jumping = false;
						}
					} else {
						jumping = false;
					}
				}
				
				if (body instanceof Spring && body.isInContact(player) && player.getWorldCenter().y + 0.125 - body.getWorldCenter().y > 0.49) {
					jumping = false;
					spring = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// Speed constraint
		if (Math.abs(player.getLinearVelocity().x) > MAX_VELOCITY) {
			player.setLinearVelocity(Math.signum(player.getLinearVelocity().x) * MAX_VELOCITY, player.getLinearVelocity().y);
		}
		
		// Death on spikes
		for (Body body: world.getBodies()) {
			try {
				if (body instanceof Spike && body.isInContact(player)) {
					lose();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// Death on fall
		if (player.getWorldCenter().y < -20) {
			if (LOOP_AROUND) {
				player.translate(0, 40);
			} else if (LOSABLE) {
				lose();
			}
		}
		
		// Level completion
		try {
			if (levels.get(curLevel).checkpoint2.isInContact(player)) {
				proceed();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Adding levels
	public void addLevel (PlatformerLevel level) {
		levels.add(level);
	}
	
	public void addLevel (int[][] level, String levelText) {
		addLevel(new PlatformerLevel(level, levelText));
	}
	
	public void addLevel (int[][] level, List<Class<? extends GameObject>> map, String levelText) {
		addLevel(new PlatformerLevel(level, map, levelText));
	}
	
	public void addLevels (Object... newLevels) {
		for (int i = 0; i < newLevels.length; i += 2) {
			addLevel((int[][]) newLevels[i + 1], (String) newLevels[i]);
		}
	}
	
	public void addLevels (PlatformerLevel... newLevels) {
		for (PlatformerLevel level: newLevels) {
			addLevel(level);
		}
	}
	
	// Center screen
	public void paint (Graphics2D g, Canvas canvas) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if (screen == Screen.GAME) {
			g.translate(-player.getWorldCenter().x * scale, (-player.getWorldCenter().y - 2) * scale);
		}
	}
	
	public void apaint (Graphics2D g, Canvas canvas) {
		if (screen == Screen.GAME) {
			g.setColor(Color.BLACK);
			g.setFont(new Font("Verdana", Font.PLAIN, 20));
			
			g.transform(AffineTransform.getScaleInstance(1, -1));
			drawXAlignedString(g, levels.get(curLevel).getLevelText(), (int)(player.getWorldCenter().x * scale), (int)((-player.getWorldCenter().y - 2) * scale - 200));
			g.transform(AffineTransform.getScaleInstance(1, -1));
		}
	}
	
	// Other screens
	public void inactivepaint (Graphics2D g, Canvas canvas) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		switch (screen) {
			case START:
				// Start screen
				g.setColor(Color.CYAN);
				g.fillRect(0, 0, 800, 600);
				
				g.setColor(Color.BLACK);
				g.setFont(new Font("Verdana", Font.ITALIC, 36));
				drawXAlignedString(g, "A Generic Platformer", 400, 220);
				
				g.setColor(Color.GREEN);
				g.fillRoundRect(350 - offset, 300 + offset, 100, 50, roundness, roundness);
				
				g.setColor(Color.BLACK);
				g.drawRoundRect(350 - offset, 300 + offset, 100, 50, roundness, roundness);
				
				g.setColor(Color.GRAY);
				g.fillRoundRect(350, 300, 100, 50, roundness, roundness);
				
				g.setColor(Color.BLACK);
				g.drawRoundRect(350, 300, 100, 50, roundness, roundness);
				
				g.setFont(new Font("Verdana", Font.PLAIN, 24));
				drawXAlignedString(g, "Start!", 400, 335);
			return; case NEWLEVEL:
				// New level screen
				g.setColor(Color.CYAN);
				g.fillRect(0, 0, 800, 600);
				
				String str = getCompliment();
				
				g.setColor(Color.BLACK);
				g.setFont(new Font("Verdana", Font.PLAIN, 36));
				drawXAlignedString(g, str, 400, 220);
				
				g.setColor(Color.GREEN);
				g.fillRoundRect(350 - offset, 300 + offset, 100, 50, roundness, roundness);
				
				g.setColor(Color.BLACK);
				g.drawRoundRect(350 - offset, 300 + offset, 100, 50, roundness, roundness);
				
				g.setColor(Color.GRAY);
				g.fillRoundRect(350, 300, 100, 50, roundness, roundness);
				
				g.setColor(Color.BLACK);
				g.drawRoundRect(350, 300, 100, 50, roundness, roundness);
				
				g.setFont(new Font("Verdana", Font.PLAIN, 24));
				drawXAlignedString(g, "Start!", 400, 335);
			return; case WIN:
				g.setColor(Color.CYAN);
				g.fillRect(0, 0, 800, 600);
				
				g.setColor(Color.BLACK);
				g.setFont(new Font("Verdana", Font.PLAIN, 48));
				drawXAlignedString(g, "You won!", 400, 300);
			return; case LOSE:
				// Lose screen
				for (int alpha = 0; alpha < 50; alpha ++) {
					g.setColor(new Color(255, 0, 0, alpha));
					g.fillRect(0, 0, 800, 600);
					
					BufferStrategy strategy = canvas.getBufferStrategy();
					
					if (!strategy.contentsLost()) {
						strategy.show();
					}
					
					try {
						TimeUnit.MILLISECONDS.sleep(50);
					} catch (InterruptedException e) {}
				}
				
				g.setColor(Color.BLACK);
				g.setFont(new Font("Verdana", Font.PLAIN, 36));
				
				drawXAlignedString(g, "You Lost! Try again?", 400, 220);
				
				g.setColor(Color.GREEN);
				g.fillRoundRect(350 - offset, 300 + offset, 100, 50, roundness, roundness);
				
				g.setColor(Color.BLACK);
				g.drawRoundRect(350 - offset, 300 + offset, 100, 50, roundness, roundness);
				
				g.setColor(Color.GRAY);
				g.fillRoundRect(350, 300, 100, 50, roundness, roundness);
				
				g.setColor(Color.BLACK);
				g.drawRoundRect(350, 300, 100, 50, roundness, roundness);
				
				g.setFont(new Font("Verdana", Font.PLAIN, 24));
				drawXAlignedString(g, "Start!", 400, 335);
			default: break;
		}
	}
	
	// Play mp3 sounds from media
	protected void playSound (String name) {
		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(getClass().getResource("../media/" + name + ".wav"));
			Clip clip = AudioSystem.getClip();
			
			clip.open(audio);
			
			new Thread () {
				public void run () {
					clip.start();
					try {
						TimeUnit.MILLISECONDS.sleep((long)(1000.0 * audio.getFrameLength() / audio.getFormat().getFrameRate()));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Start or restart game
	protected void start () {
		screen = Screen.START;
		active = false;
		
		for (@SuppressWarnings("unused") Body body: world.getBodies()) {
			body = null;
		}
		
		world.removeAllBodies();
	}
	
	// Initialize and start game play
	protected void startGame () {
		// Initializing level
		PlatformerLevel level = levels.get(curLevel);
		level.add(world, scale, 0, -1);
		
		// Initializing player
		player = new Player(world, scale, level.checkpoint1.x + 0.75, level.checkpoint1.y - 0.5);
		
		screen = Screen.GAME;
		active = true;
	}
	
	// Proceed to next level
	protected void proceed () {
		if (curLevel + 1 >= levels.size()) {
			screen = Screen.WIN;
		} else {
			screen = Screen.NEWLEVEL;
		}
		
		active = false;
	}
	
	// Initiate lose screen
	protected void lose () {
		if (LOSABLE) {
			screen = Screen.LOSE;
			active = false;
		}
	}
	
	// Generate random compliment on level completion
	protected String getCompliment () {
		return compliments[new Random().nextInt(compliments.length)];
	}
	
	// Draw X-Aligned String (w/ newlines)
	protected void drawXAlignedString (Graphics2D g, String str, int x, int y) {
		int h = g.getFontMetrics().getHeight();
		
		for (String line: str.split("\n")) {
			g.drawString(line, x - g.getFontMetrics().stringWidth(line) / 2, y);
			y += h;
		}
	}
	
	protected boolean isKeyUp (KeyEvent e) {
		return e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W;
	}
	
	protected boolean isKeyLeft (KeyEvent e) {
		return e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A;
	}
	
	protected boolean isKeyDown (KeyEvent e) {
		return e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S;
	}
	
	protected boolean isKeyRight (KeyEvent e) {
		return e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D;
	}
	
	@Override public void keyPressed (KeyEvent e) {
		if (!active) {
			return;
		}
		
		// Jumping
		if (isKeyUp(e) && !jumping) {
			if (spring) {
				player.applyImpulse(new Vector2(0, 2.5));
			} else {
				player.applyImpulse(new Vector2(0, 1.5));
			}
			
			playSound("jump2");
		}
		
		// Moving
		if (isKeyLeft(e)) {
			player.applyImpulse(new Vector2(-0.5, 0));
		}
		if (isKeyRight(e)) {
			player.applyImpulse(new Vector2(0.5, 0));
		}
	}
	
	@Override public void mouseClicked (MouseEvent e) {
		if (e.getX() > 350 && e.getX() < 450 && e.getY() > 300 && e.getY() < 350) {
			if (screen == Screen.NEWLEVEL) {
				curLevel ++;
			}
			
			if (screen == Screen.START || screen == Screen.LOSE || screen == Screen.NEWLEVEL) {
				start();
				startGame();
			}
		}
	}
}
