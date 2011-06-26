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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;

public class Vec implements Serializable, Comparable<Vec>
{
	private static final long serialVersionUID = 1L;
	
	private double x, y;
	
	public Vec(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vec(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vec(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public static Vec make(double angle, double length)
	{
		return new Vec((double)1, Math.atan(angle)).setLength(length);
	}
	
	public Vec add(Vec other)
	{
		return new Vec(x + other.x, y + other.y);
	}
	
	public Vec subtract(Vec other)
	{
		return new Vec(x - other.x, y - other.y);
	}
	
	public Vec times(int c)
	{
		return new Vec(x*c, y*c);
	}
	
	public Vec times(float c)
	{
		return new Vec(x*c, y*c);
	}
	
	public Vec times(double c)
	{
		return new Vec(x*c, y*c);
	}
	
	public double getLength()
	{
		return (double)Math.sqrt(x*x + y*y);
	}
	
	public Vec setLength(int len)
	{
		return this.times(((double)len)/this.getLength());
	}
	
	public Vec setLength(float len)
	{
		return this.times(len/this.getLength());
	}
	
	public Vec setLength(double len)
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
		return (int)x;
	}
	
	public int getY()
	{
		return (int)y;
	}
	
	public double getDoubleX()
	{
		return x;
	}
	
	public double getDoubleY()
	{
		return y;
	}
	
	public double getAngle()
	{
		if (x == 0) {
			return Math.PI / 2;
		} else {
			return Math.atan(y / x);
		}
	}
	
	@Override
	public String toString()
	{
		return "("+x+","+y+")";
	}
	
	public String explain()
	{
		return "("+x+","+y+") ~= ("+(int)x+","+(int)y+")";
	}
	
	public Vec clone()
	{
		return new Vec(x, y);
	}
	
	public static void normalize(ArrayList<Vec> vecs, int size)
	{
		if (vecs.size() == 0) {
			return;
		}
		int num = vecs.size();
		
		Rect bound = new Rect(vecs.get(0), vecs.get(0));
		for (Vec vec : vecs) {
			bound = bound.fit(vec);
		}
		
		float scaleX = (size - 1) / (float)bound.Width();
		float scaleY = (size - 1) / (float)bound.Height();
		for (int i = 0; i < num; i++) {
			Vec n = vecs.get(i).subtract(bound.TopLeft());
			vecs.set(i, new Vec(n.getX() * scaleX, n.getY() * scaleY));
		}
	}
	
	public ArrayList<Vec> neighbours(int exclMax)
	{
		return neighbours(exclMax, 1);
	}
	
	public ArrayList<Vec> neighbours(int exclMax, int order)
	{
		TreeSet<Vec> n = new TreeSet<Vec>();
		
		for (int y = 0; y <= order; y++) {
			for (int x = 0; x <= order - y; x++) {
				n.add(add(new Vec(x, y)));
				n.add(add(new Vec(-x, y)));
				n.add(add(new Vec(x, -y)));
				n.add(add(new Vec(-x, -y)));
			}
		}
		
		return new ArrayList<Vec>(n);
	}
	
	public static ArrayList<Vec> getStops(Vec a, Vec b, double stopDistance)
	{
		ArrayList<Vec> stops = new ArrayList<Vec>();
		stops.add(a.clone());
		
		Vec d = make(b.subtract(a).getAngle(), stopDistance);
		int num = (int)Math.floor(b.subtract(a).getLength() / stopDistance);
		for (int i = 1; i <= num; i++) {
			stops.add(a.add(d.times(i)));
		}
		
		return stops;
	}
	
	@Override
	public int compareTo(Vec o)
	{
		if (y == o.y) {
			return x == o.x ? 0 : (x > o.x ? 1 : -1);
		} else {
			return y == o.y ? 0 : (y > o.y ? 1 : -1);
		}
	}
}
