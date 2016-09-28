import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;


public class Spawner extends GameObject
{

	private static BufferedImage spawnerImg;
	protected float timeUntilNextSpawn;
	protected float spawnTime;
	protected Vector<GameObject> spawnedObjects;
	protected GameObject nextSpawnObject;
	
	public Spawner(Point2D pos, float radius)
	{
		super(pos, radius, spawnerImg);
		spawnedObjects = new Vector<GameObject>();
		Random rand = new Random();
		spawnTime = 3 + rand.nextInt(9);
		timeUntilNextSpawn = spawnTime;
		maxHealth = 8;
		health = maxHealth;
	}

	public static void loadContent()
	{
		try{
			spawnerImg = ImageIO.read(new File("Images/spawner.png"));
			}
			catch(Exception e)
			{
				try {
					spawnerImg = ImageIO.read(Game.class.getResource("Images/spawner.png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
	}
	public void updateState(long elapsedTime, Player player)
	{
		super.updateState(elapsedTime);
		if(isDespawned())
			return;
		timeUntilNextSpawn -= elapsedTime/1000.0f;

		if(nextSpawnObject == null)
			spawnEnemy(player);
		//check to see if timer has counted down to 0, if so spawn an enemy
		if(timeUntilNextSpawn <=0)
		{
			releaseEnemy();
			spawnEnemy(player);
			//start next cycle with time remaining on the other
			timeUntilNextSpawn = spawnTime - timeUntilNextSpawn;
		}
		if(nextSpawnObject instanceof Leaper)
		{
			rotation = this.getAngleBetween(player);
			nextSpawnObject.setOrient(rotation);
		}
		else if(nextSpawnObject instanceof Floater)
			rotation = nextSpawnObject.getOrient();
	}
	public void spawnEnemy(Player player)
	{
		Random rand = new Random();
		if(rand.nextInt(2) == 1)
		{
			nextSpawnObject = new Floater((Point2D)pos.clone(), radius*2, (float) (rand.nextFloat()* Math.PI*2.0f));
		}
		else
		{
			nextSpawnObject = new Leaper((Point2D)pos.clone(), radius,(float) (rand.nextFloat()* Math.PI*2.0f));
		}
	}
	public void releaseEnemy()
	{
		if(nextSpawnObject!= null)
			spawnedObjects.add(nextSpawnObject);
	}
	public void draw(Graphics g)
	{
		super.draw(g);
		if(health<=0)
			return;
		//draw spawn countdown
		int maxHeight = (int) (radius);
		int width = (int) (maxHeight - timeUntilNextSpawn /spawnTime * maxHeight);
		int height = (int) (maxHeight - timeUntilNextSpawn /spawnTime * maxHeight) ;
		//int height = (int) (maxHeight) ;
		int drawX = (int) (pos.getX() - width/2.0 );
		//int drawX = (int) (pos.getX() );
		int drawY = (int) (pos.getY() - height/2.0);
		
		//make the colour change from green to red as it counts down
		Color color = new Color( Math.max(0, 1 - timeUntilNextSpawn/spawnTime),Math.min(1, timeUntilNextSpawn/spawnTime), 0, 1 );
		g.setColor(color);
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//draw the bar
		g2d.fillOval(drawX,drawY, width, height);
		
		
	}
	public boolean hasObjects()
	{
		return spawnedObjects.size() >0;
	}
	public GameObject nextObject()
	{
		GameObject next = spawnedObjects.get(spawnedObjects.size()-1);
		spawnedObjects.remove(spawnedObjects.size()-1);
		return next;
	}
}
