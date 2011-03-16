/*=============================================================================
 * CallByDoodle
 * 
 * CallByDoodle is an Android project created in the Spring of 2011
 * for Research & Development 1, a course the authors follow at
 * Radboud University, Nijmegen.
 * 
 * Authors:
 * Roelf Leenders
 * Kelley van Evert
 * Jochem Kooistra
 * 
 * Supervisors:
 * Erik Barendsen
 * Sjaak Smetsers
 * 
 * Project started in February of 2011, scheduled for completion 
 * in the summer of 2011.
 ============================================================================*/


package studie.callbydoodle;

// Very simple 2D vector class
// Immutable
// Joepie, we leren wat nuttigs bij Bart Jacobs!
public class Vec
{
	private float x, y;
	
	public Vec(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vec add(Vec other)
	{
		return new Vec(x + other.x, y + other.y);
	}
	
	public Vec subtract(Vec other)
	{
		return new Vec(x - other.x, y - other.y);
	}
	
	public Vec times(float c)
	{
		return new Vec(x*c, y*c);
	}
	
	public float getLength()
	{
		return (float)Math.sqrt((double)x*x + y*y);
	}
	
	public Vec setLength(float len)
	{
		return this.times(len/this.getLength());
	}
	
	public Vec rotateLeft()
	{
		return new Vec(y, -x);
	}
	
	public Vec rotateRight()
	{
		return new Vec(-y, x);
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
}
