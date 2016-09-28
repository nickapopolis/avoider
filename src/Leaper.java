import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Leaper extends GameObject
{

	static BufferedImage leaperImg;
	
	public Leaper(Point2D pos, float radius, float orient)
	{
		super(pos, radius, orient, leaperImg);
		this.maxSpeed = 500;
		this.maxHealth = 5;
		this.health = maxHealth;
	}
	
	public static void loadContent()
	{
		try{
			leaperImg = ImageIO.read(new File("Images/leaper.png"));
			}
			catch(Exception e)
			{
				try {
					leaperImg = ImageIO.read(Game.class.getResource("Images/leaper.png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
	}
	public void setOrient(float orient)
	{
		super.setOrient(orient);
		rotation = orient;
	}
}
