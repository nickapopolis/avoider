import java.awt.geom.Point2D;


public class Collision
{
	public GameObject obj1, obj2;
	public Point2D collisionPoint;
	public Point2D collisionVector;
	
	public Collision(GameObject obj1, GameObject obj2)
	{
		collisionPoint = new Point2D.Float();
		collisionVector = new Point2D.Float();
		this.obj1 = obj1;
		this.obj2 = obj2;
	}
	public static Collision sphereSphereCollision(GameObject obj1, GameObject obj2)
	{
		Point2D distVector = new Point2D.Float();
		distVector.setLocation(obj1.getPos().getX() - obj2.getPos().getX(), obj1.getPos().getY() - obj2.getPos().getY());
		float dist = (float) obj1.getPos().distance(obj2.getPos());
		float minDist = obj1.radius + obj2.radius;
		
		Collision collision = null;
		
		//found collision
		if (dist <= minDist)
		{
			collision = new Collision(obj1, obj2);
			
			float x = (float) Math.pow(distVector.getX(),  2.0f);
			float y = (float) Math.pow(distVector.getY(),  2.0f);
			
			float d = (float) Math.sqrt( x + y ); 
			float angleInRadian = (float) Math.atan2(distVector.getY(),distVector.getX()); //angle in radian
			float angleInDegree = (float) (angleInRadian * 180 / Math.PI);
			
			//calculate the collision point
			float angle = angleInRadian;
			/*float collisionDist  = dist - obj1.radius;
			float x = (float) (obj2.getPos().getX() + Math.cos(Math.toDegrees(angle))* collisionDist);
			float y = (float) (obj2.getPos().getY() + Math.sin(Math.toDegrees(angle))* collisionDist);
			collision.collisionPoint.setLocation(x,y);
			*/
			//calculate the collision vector
			float intersectionDist = obj1.radius + obj2.radius - Math.abs(dist);
			//System.out.println(angle);
			x = (float) (Math.cos(angle)* intersectionDist);
			y = (float) (Math.sin(angle)* intersectionDist);
			collision.collisionVector.setLocation(x, y);
		}
		return collision;
	}
}
