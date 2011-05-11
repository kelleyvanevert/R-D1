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


package studie.callbydoodle.data;

/**
 * Very small, simple and straightforward 2d vector class.
 * The name should say enough ;)
 * 
 * By the way, I found out that floats really aren't that
 *  necessary. Int's work great too, you can't see any difference
 *  when you draw your doodles.. (at least I couldn't)
 * Using int's helps me implement a nicer Doodle interface, check it for details..
 */
public class Vec
{
	private int x, y;
	
	public Vec(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vec(float x, float y)
	{
		this.x = (int)x;
		this.y = (int)y;
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
	
	public boolean equals(Object other)
	{
		if (other.getClass() != this.getClass())
			return false;
		
		return (x == ((Vec)other).getX() && y == ((Vec)other).getY());
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public static Vec parseVec(String str) throws Exception {
		String[] s = str.replaceAll("[\\(\\)\\s]", "").split(",");
		return new Vec(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
	}
	
	@Override
	public String toString()
	{
		return "("+x+","+y+")";
	}
	
	public Vec clone()
	{
		return new Vec(x, y);
	}
}
