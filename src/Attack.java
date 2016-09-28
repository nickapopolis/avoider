import java.awt.geom.Point2D;


public class Attack extends GameObject
{

	float totalTime;
	float timeElapsed;
	float maxRadius;
	int attackStrength;
	public Attack(Point2D pos, float radius, float time, int attackStrength)
	{
		super(pos, radius, null);
		totalTime = time;
		maxRadius = radius;
		timeElapsed = 0;
		this.attackStrength = attackStrength;
	}
	public void updateState(long elapsedTime)
	{
		timeElapsed += elapsedTime /1000.0f;
		float y = 0.0f;
		float x = totalTime;
		float h = totalTime*0.25f;
		float k = maxRadius;
		float a = (float) ((y-k) / Math.pow(x-h, 2.0f));
		
		x = timeElapsed;
		radius = (float) (a * Math.pow(x-h, 2.0f) + k);
	}
	public float getTimeRemaining()
	{
		return totalTime - timeElapsed;
	}
	public int getAttackStrength()
	{
		return attackStrength;
	}
}
