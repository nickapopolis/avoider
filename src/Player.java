import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Player extends GameObject
{
	private static BufferedImage playerImg;
	protected int punchComboCount;
	protected Attack currentAttack;
	protected float timeToChainCombo = 0.25f;
	protected Point2D lastPos;
	protected GameObject closestObject;
	
	public Player(Point2D pos, float radius)
	{
		super(pos, radius, playerImg);
		punchComboCount = 1;
		currentAttack = null;
		maxHealth = 3;
		health = maxHealth;
		lastPos = pos;
	}

	public static void loadContent()
	{
		try{
			playerImg = ImageIO.read(new File("Images/player.png"));
			}
			catch(Exception e)
			{
				try {
					playerImg = ImageIO.read(Game.class.getResource("Images/player.png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
	}
	public void updateState(long elapsedTime, GameObject closestObject)
	{
		//rotation of player
		this.closestObject = closestObject;
		
		if(closestObject != null )
			rotation = getAngleBetween(closestObject);
		
		if(currentAttack != null)
		{
			currentAttack.updateState(elapsedTime);
			
			if(currentAttack.getTimeRemaining() <= 0)
				currentAttack = null;
		}
		
		if(currentAttack == null )
			punchComboCount = 0;

	     
		
	}
	public Attack performAction()
	{
		
		Attack attack = null;
		
		Point2D attackPos = new Point2D.Float();
		float attackDuration = 0;
		float attackRadius = 0;
		int attackStrength = 0;
		
		float angleInRadian;
		
		if(closestObject == null)
		{
			angleInRadian = orient;
		}
		else
		{
			angleInRadian = this.getAngleBetween(closestObject);
		}
		
		attackPos.setLocation(pos.getX() + Math.cos(angleInRadian)* radius, pos.getY() + Math.sin(angleInRadian)* radius) ;
		
		//first attack
		if(currentAttack == null )
		{
			punchComboCount = 1;
			attackDuration = 0.75f;
		}
		//too early
		else if(currentAttack.getTimeRemaining() > timeToChainCombo)
		{
			punchComboCount = 1;
		}
		//on time
		else if(currentAttack.getTimeRemaining() <= timeToChainCombo)
		{
			if(punchComboCount >4)
			{
				punchComboCount = 1;
				attackDuration = 0.75f;
			}
			else
			{
				punchComboCount++;
				attackDuration = (float) (0.75f + punchComboCount * 0.25f);
				attackDuration = 0.75f;
			}
		}
		attackRadius = radius + (punchComboCount-1) *radius; 
		attackStrength = punchComboCount;
		
		if (attackDuration >0 && attackRadius >0 && attackStrength >0)
			attack = new Attack(attackPos, attackRadius, attackDuration, attackStrength);
		
		if(attack!= null)
			currentAttack = attack;
		
		return attack;
	}
	public void draw(Graphics g)
	{
		super.draw(g);
		lastPos = (Point2D) pos.clone();
		if(currentAttack != null)
			currentAttack.draw(g);
	}
}
