import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Game extends GameEngine
{
	
	Level currentLevel;
	Player player;
	Point lastMousePos;
	Vector<Collision> collisions;
	Image backgroundImg;
	boolean mousePressed = false;
	JFrame pauseFrame;
	HUD hud;
	public Game(Container parentFrame) {
		super(parentFrame);
	}
	public void initialize()
	{
		pauseFrame = null;
		player = new Player(new Point2D.Float(0.0f,0.0f), 25);
		hud = new HUD(player, viewWidth, viewHeight);
		setLevel(createNextLevel());
		lastMousePos = null;
		collisions = null;
	}
	public void loadContent()
	{
		Leaper.loadContent();
		Spawner.loadContent();
		Floater.loadContent();
		Player.loadContent();
		Animation.loadContent("explosion.png");
		
		try{
			backgroundImg = ImageIO.read(new File("Images/background.jpg"));
			}
			catch(Exception e)
			{
				try {
					backgroundImg = ImageIO.read(Game.class.getResource("Images/background.jpg"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
	}
	public void updateGame(long elapsedTime)
	{
		this.verifyFrameDimensions();
		if(!paused)
		{
			if(player.getHealth() == 0)
			{
				player.resetHealth();
				currentLevel = null;
			}
			if(currentLevel == null || currentLevel.getNumSpawnersRemaining() ==0 )
				setLevel(createNextLevel());
			
			currentLevel.update(elapsedTime, player, viewWidth, viewHeight);
			GameObject closestSpawner = currentLevel.getClosestSpawner(player);
	
			Point mousePos = getMousePosition();
			if(mousePos != null)
			{
				if(lastMousePos == null)
					lastMousePos = new Point((int) player.getPos().getX(), (int) player.getPos().getY());
				
				player.setPosition(mousePos.x, mousePos.y);
			}
			player.updateState(elapsedTime, closestSpawner);
			
			checkCollisions();
			
		}
	}
	public void paint(Graphics g)
	{
		long currentTime = System.currentTimeMillis();
		updateGame(currentTime-lastTime);
		
		g.setColor(new Color(255,255,255));
		
		//background
		g.drawImage(backgroundImg, 0, 0, this.getWidth(), this.getHeight(), null);
		
		//level
		if (currentLevel!= null)
			currentLevel.paint(g, this);
		
		//player
		player.draw(g);
		
		//hud
		hud.draw(paused, g, viewWidth, viewHeight);
		lastTime = currentTime;
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			paused = !paused;
		}
	}
	protected void checkCollisions()
	{
		Vector<Collision> collisions = currentLevel.checkCollisions(player);
		this.collisions = new Vector<Collision>();
		for(int i=0; i<collisions.size();i++)
		{
			Collision collision = collisions.get(i);
			GameObject obj1 = collision.obj1;
			GameObject obj2 = collision.obj2;
			
			if(obj2 instanceof Spawner)
			{
				obj1.setPosition(obj1.getPos().getX() + collision.collisionVector.getX(), obj1.getPos().getY() + collision.collisionVector.getY());
				this.collisions.add(collision);
			}
			else if(obj2 instanceof Floater|| obj2 instanceof Leaper)
			{
				player.adjustHealth(-1);
				obj2.adjustHealth(-obj2.getHealth());
			}
		}
	}
	
	public void mouseReleased(MouseEvent evt) 
	{
		System.out.println("clicked");
		Attack attack = player.performAction();
		
		if(attack!=null)
		{
			System.out.println("attack");
			Vector<Collision> hits = currentLevel.checkCollisions(attack);
			for(int i=0;i<hits.size();i++)
			{
				System.out.println("attack hit");
				Collision hit = hits.get(i);
				GameObject objectHit = hit.obj2;
				objectHit.adjustHealth(-attack.getAttackStrength());
			}
		}
		
		
	}
	
	public void setLevel(Level level)
	{
		currentLevel = level;
		hud.setLevel(level);
	}
	public Level createNextLevel()
	{
		Level level = new Level(currentLevel==null?1:(currentLevel.getNumSpawnersInitial()+1), viewWidth, viewHeight);
		return level;
	}
	public void verifyFrameDimensions()
	{
		int height = frame.getHeight();
		if(frame.getWidth() != height *aspectRatio);
		{
			frame.setSize((int) (height*aspectRatio), height);
			
			viewHeight = frame.getHeight();
			viewWidth = (int) (viewHeight * aspectRatio);
			this.setSize(viewWidth, viewHeight);
			this.setLocation(frame.getWidth()/2 - viewWidth/2, 0);
		}
	}
	public static void main(String args[])
	{
		JFrame frame = new JFrame("Ghool Factory by Nick Tierney");
		System.setProperty("awt.useSystemAAFontSettings","on");
		System.setProperty("swing.aatext", "true");
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		Game g = new Game(frame);
		g.loadContent();
		g.initialize();
		
		frame.add(g);
		//frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorImg, new Point(0, 0), "blank cursor");

		// Set the blank cursor to the JFrame.
		frame.getContentPane().setCursor(blankCursor);

		g.start();
		
	}
	
}

