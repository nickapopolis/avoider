import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


public class GameObject
{
	BufferedImage img;
	Point2D pos;
	Point2D vel;
	float gravityVel;
	Point2D acc;
	float radius;
	float orient;
	float maxSpeed;
	int health;
	int maxHealth;
	float rotation;
	Animation deathAnimation;
	boolean gravityEnabled;
	public boolean despawnOnBoundary;
	protected static final float gravity = 9.81f/10.0f;
	public GameObject(Point2D pos, float radius, BufferedImage img)
	{
		deathAnimation = new Animation(true);
		this.pos = pos;
		this.radius = radius;
		this.img = img;
		this.orient = 0;
		this.rotation = 0;
		this.vel = new Point2D.Float(0.0f,0.0f);
		this.acc = new Point2D.Float(0.0f, 0.0f);
		this.gravityVel = 0.0f;
		gravityEnabled = false;
		despawnOnBoundary = true;
	}
	public GameObject(Point2D pos, float radius,float orient, BufferedImage img)
	{
		this.pos = pos;
		this.radius = radius;
		this.img = img;
		this.orient = orient;
		this.vel = new Point2D.Float(0.0f,0.0f);
		this.acc = new Point2D.Float(0.0f, 0.0f);
		this.gravityVel = 0.0f;
		deathAnimation= new Animation(true);
		gravityEnabled =  false;
		despawnOnBoundary = true;
	}
	public void draw(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		AffineTransform rotate = new AffineTransform();
		
		g.setColor(new Color(0,0,0,0.67f));
		 rotate.translate(pos.getX(), pos.getY() );
	     rotate.rotate(rotation);
	     rotate.translate(-pos.getX()- radius,-pos.getY()- radius);
	     
	     // take a copy of the transformation that g2d is using
	     AffineTransform prevTransform = g2d.getTransform();
	    
	     // apply the transformation for this object and draw
	    g2d.setTransform(rotate);
	      

		int posX = (int)(pos.getX()-radius);
		int posY = (int)(pos.getY()-radius);
		
	    if(img!= null)
	    {
	    	g2d.drawImage(img, (int) pos.getX(), (int) pos.getY(), (int)radius *2, (int)radius *2, null);
	    }
	    else
	    	g2d.fillOval((int) pos.getX(), (int) pos.getY(),(int)( radius*2), (int)(radius*2));
	    	
	 
	    // revert g2d back to the old transformation
	    g2d.setTransform(prevTransform);
		
		
	}
	public void setPosition(float x, float y)
	{
		pos.setLocation(x,y);
	}
	public Point2D getPos()
	{
		return pos;
	}
	public float radius()
	{
		return radius;
	}
	public float getOrient()
	{
		return orient;
	}
	public void setOrient(float orient)
	{
		this.orient = orient;
	}
	public void setGravityEnabled(boolean enabled)
	{
		gravityEnabled = enabled;
	}
	public void updateState(long elapsedTime)
	{
		if(health<=0)
		{
			deathAnimation.updateState(elapsedTime);
			if(!deathAnimation.isComplete())
				img = deathAnimation.getNextFrame();
		}
		vel.setLocation(maxSpeed * Math.cos(orient), maxSpeed * Math.sin(orient));
		if(gravityEnabled)
			gravityVel += gravity;
		pos.setLocation(pos.getX() +elapsedTime/1000.0f *vel.getX(), pos.getY() +elapsedTime/1000.0f *vel.getY() + (gravityEnabled?gravityVel*elapsedTime/1000.0f:0.0f));
	}
	public void setPosition(double x, double y)
	{
		setPosition((float)x, (float)y);
	}
	public void adjustHealth(int amount)
	{
		if(amount>0)
			health = Math.min(maxHealth, health+amount);
		else if(amount<0)
			health = Math.max(0, health+amount);
			
	}
	public int getHealth()
	{
		return health;
	}
	public float getAngleBetween(GameObject obj)
	{
		return getAngleBetween(obj.getPos());
	}
	public float getAngleBetween(Point2D loc1, Point2D loc2)
	{
		float angleInRadian = 0;
		Point2D distVector = new Point2D.Float();
		distVector.setLocation(loc2.getX() - loc1.getX(), loc2.getY() - loc1.getY());
		angleInRadian = (float) Math.atan2(distVector.getY(),distVector.getX()); //angle in radian
		
		return angleInRadian;
	}
	public float getDistanceBetween(GameObject obj)
	{
		return (float) this.pos.distance(obj.getPos());
	}
	public float getAngleBetween(Point2D loc)
	{
		float angleInRadian = 0;
		Point2D distVector = new Point2D.Float();
		distVector.setLocation(loc.getX() - pos.getX(), loc.getY() - pos.getY());
		angleInRadian = (float) Math.atan2(distVector.getY(),distVector.getX()); //angle in radian
		
		return angleInRadian;
	}
	public void explode()
	{
		
	}
	public void resetHealth()
	{
		health = maxHealth;
	}
	
	public boolean isDespawned()
	{
		return health <=0 && deathAnimation.isComplete();
	}
	
	public void resetGravity()
	{
		this.gravityVel = 0;
	}
}
