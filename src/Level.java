import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.Vector;



public class Level 
{
	
	Vector<GameObject> enemies;
	Vector<Spawner> spawners;
	int numSpawnersInitial;
	public Level(int numSpawners, int width, int height)
	{
		numSpawnersInitial = numSpawners;
		enemies = new Vector<GameObject>();
		spawners = new Vector<Spawner>();
		createLevel(numSpawners, width, height);
		
	}
	public void update(long elapsedTime, Player player, int width, int height)
	{
		int i=0;
		while(i<enemies.size())
		{
			GameObject enemy = enemies.get(i);
			enemy.updateState(elapsedTime);
			if(enemy.isDespawned() || (enemy.despawnOnBoundary && isOutOfBounds(enemy, width, height)))
				enemies.remove(i);
			else if(enemy.getPos().getY() > height && enemy.gravityEnabled && !enemy.despawnOnBoundary)
			{
				System.out.println("ob");
				enemy.resetGravity();
				enemy.setPosition(enemy.getPos().getX(), height);
				i++;
			}
			else
				i++;
		}
		
		//update the spawners
		Vector<GameObject> newEnemies = new Vector<GameObject>();
		i=0;
		while (i<spawners.size())
		{
			Spawner enemy = spawners.get(i);
			
			if(enemy.isDespawned())
				spawners.remove(i);
			else
			{
				enemy.updateState(elapsedTime, player);
				
				//add all the objects that the spawner created
				while(enemy.hasObjects())
				{
					newEnemies.add(enemy.nextObject());
				}
				i++;
			}
			
		}
		enemies.addAll(newEnemies);
	}
	public void paint(Graphics g, Container c )
	{
		for(int i= 0; i<enemies.size(); i++)
			enemies.get(i).draw(g);
		for(int i= 0; i<spawners.size(); i++)
			spawners.get(i).draw(g);
		
	}
	private void createLevel(int numSpawners, int width, int height)
	{
		System.out.println(numSpawners + " " + width+" "+height);
		Random rand = new Random();
		for(int i=0;i<numSpawners;i++)
			spawners.add(new Spawner(new Point2D.Float(rand.nextInt(width - 100) + 50,rand.nextInt(height - 100) + 50), 50));
	}
	public Vector<Collision> checkCollisions(GameObject object)
	{
		Vector<Collision> collisions = new Vector<Collision>();
		Collision collision = null;
		for(int i= 0; i<enemies.size(); i++)
		{
			//object has already exploded, its death animation is just playing
			if(enemies.get(i).health<=0)
				continue;
			collision = Collision.sphereSphereCollision(object, enemies.get(i));
			if(collision != null)
				collisions.add(collision);
		}
		for(int i= 0; i<spawners.size(); i++)
		{
			if(spawners.get(i).health<=0)
				continue;
			collision = Collision.sphereSphereCollision(object, spawners.get(i));
			if(collision != null)
				collisions.add(collision);
		}
		return collisions;
	}
	public GameObject getClosestSpawner(GameObject obj)
	{
		float smallestDistSquared = Integer.MAX_VALUE;
		GameObject closest = null;
		for(int i= 0; i<spawners.size(); i++)
		{
			Spawner spawner = spawners.get(i);
			float distSquared = (float) obj.getPos().distanceSq(spawner.getPos());
			if (distSquared < smallestDistSquared)
			{
				smallestDistSquared = distSquared;
				closest = spawner;
			}
		}
		return closest;
	}
	public int getNumSpawnersRemaining()
	{
		return spawners.size();
	}
	public int getNumSpawnersInitial()
	{
		return this.numSpawnersInitial;
	}
	public void remove(GameObject obj)
	{
		if(obj instanceof Spawner)
			spawners.remove(obj);
		else
			enemies.remove(obj);
	}
	
	public boolean isOutOfBounds(GameObject obj, int width, int height)
	{
		return obj.getPos().getX() <0 || obj.getPos().getX()>width|| obj.getPos().getY()<0 || obj.getPos().getY()>height;
	}
}
