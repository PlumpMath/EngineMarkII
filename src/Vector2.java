
public class Vector2 {
	public float x, y;
	
	public Vector2()
	{
		x = 0.0f;
		y = 0.0f;
	}
	public Vector2(float x, float y)
	{
		this.x =x;
		this.y = y;
	}
	
	public float magnitude()
	{
		return (float)Math.sqrt(x*x + y*y);
	}
	public float direction()
	{
		return (float)Math.atan2(y, x);
	}
	
	public float getX()
	{
		return x;
	}
	public float getY()
	{
		return y;
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	public void setY(float y)
	{
		this.y = y;
	}
	
	public float getMagnitude()
	{
		return (float)Math.sqrt(x*x+y*y);
	}
	public float getAngle()
	{
		return (float)Math.atan2(y, x);
	}
	public Vector2 getUnitVector()
	{
		float theta = getAngle();
		return new Vector2((float)Math.cos(theta), (float)Math.sin(theta));
	}
	
	public Vector2 add(Vector2 other)
	{
		return new Vector2(x+other.getX(), y+other.getY());
	}
	public Vector2 subtract(Vector2 other)
	{
		return new Vector2(x-other.getX(), y-other.getY());
	}
	public Vector2 dotProduct(float val)
	{
		return new Vector2(x*val, y*val);
	}
	

}
