import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;


public class ThrowerBoss extends GameObject
{

	enum state{THROWING, CHARGING, JUMPING };
	public ThrowerBoss(Point2D pos, float radius, BufferedImage img)
	{
		super(pos, radius, img);
		despawnOnBoundary = true;
		gravityEnabled = true;
	}

}
