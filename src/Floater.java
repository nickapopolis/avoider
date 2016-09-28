import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Floater extends GameObject
{
	static BufferedImage floaterImg;
	
	public Floater(Point2D pos, float radius, float orient)
	{
		super(pos, radius, orient, floaterImg);
		maxSpeed = 100;
		this.maxHealth = 5;
		this.health = maxHealth;
	}
	
	public static void loadContent()
	{
		try{
			floaterImg = ImageIO.read(new File("Images/floater.png"));
			}
			catch(Exception e)
			{
				try {
					floaterImg = ImageIO.read(Game.class.getResource("Images/floater.png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
	}
	public void updateState(long elapsedTime)
	{
		super.updateState(elapsedTime);
		rotation +=Math.PI*0.5f  * elapsedTime/1000.0f;
	}

}
