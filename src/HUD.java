import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class HUD
{
	Player player;
	Level level;
	Image heartImg;
	
	int lifeSize = 30;
	public HUD(Player player, int width, int height)
	{
		this.player = player;
		
		try{
			heartImg = ImageIO.read(new File("Images/heart.png"));
			}
			catch(Exception e)
			{
				try {
					heartImg = ImageIO.read(Game.class.getResource("Images/heart.png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
	}
	public void setLevel(Level level)
	{
		this.level = level;
	}
	public void draw(boolean paused, Graphics g, int width, int height)
	{
		Graphics2D g2d  = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		//draw life
		for(int i=0; i<player.getHealth();i++)
		{
			g.drawImage(heartImg, 10 + i * (10+ lifeSize), 10, lifeSize, lifeSize, null);
		}
		//draw remaining spawner count
		g.setColor(new Color(126.0f/256.0f, 246.0f/256.0f, 0, 1));
		g.setFont(new Font("Book Antiqua", Font.PLAIN, 30));

		//draw score
		int prevScore = level.getNumSpawnersInitial()>1?((level.getNumSpawnersInitial())/2 * (level.getNumSpawnersInitial()-1)):0;
		int score = ( prevScore+ level.getNumSpawnersInitial() - level.getNumSpawnersRemaining()) * 10;
		g.drawString("Score: "+score, width - 200, 30);
		
		g.drawString("Remaining: "+ level.getNumSpawnersRemaining(), width - 200, 70);
		
		if(paused)
		{
			g.setColor(Color.white);
			g.drawString("Paused", width/2 -50, height/2 -100);
		}
		
	}
}
