

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Animation
{
	static int frameWidth;
	static int frameHeight;
	static int numFrames;
	static int framesPerSecond;
	int frameX;
	int frameY;
	static BufferedImage img;
	float deltaTime;
	boolean playOnce;
	boolean complete;
	public Animation(boolean playOnce)
	{
		deltaTime = 0;
		frameX = 0;
		frameY = 0;
		frameWidth = 5;
		frameHeight = 3;
		framesPerSecond = 25;
		this.playOnce = playOnce;
		complete = false;
	}
	public static void loadContent(String fileName)
	{
		try{
			img = ImageIO.read(new File("Images/" + fileName));
			}
			catch(Exception e)
			{
				try {
					img = ImageIO.read(Game.class.getResource("Images/" + fileName));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
	}
	public void updateState(long elapsedTime)
	{
		if(playOnce && complete)
			return;
		float elapsedTimeSeconds = elapsedTime / 1000.0f;
		deltaTime += elapsedTimeSeconds;
		float timePerFrame = 1.0f/framesPerSecond;
		int numFrames = (int) (deltaTime / timePerFrame);
		deltaTime -= numFrames * timePerFrame;
		frameX = (frameX+=numFrames) % frameWidth;
		if(frameX == 0 && numFrames>0)
		{
			frameY++;
			if(frameY>=frameHeight)
				complete = true;
			frameY %= frameHeight;
		}
	}
	public BufferedImage getNextFrame()
	{
		if(playOnce && complete)
			return null;
		int imgWidth = img.getWidth()/frameWidth;
		int imgHeight = img.getHeight()/frameHeight;
		BufferedImage nextImage = img.getSubimage(imgWidth * frameX,imgHeight*frameY, imgWidth, imgHeight);
		return nextImage;
	}
	
	public boolean isComplete()
	{
		return playOnce && complete;
	}
}
