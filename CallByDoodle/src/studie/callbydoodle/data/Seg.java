package studie.callbydoodle.data;

import java.io.Serializable;

/**
 * A "Seg", in lack of a better name,
 * stands for a tiny (both in pixels, as in data)
 * segment of a doodle: it holds a starting point (Vec)
 * and an "angle" (the given angle in radians is
 * transformed to an int in [0..PRECISION) representing
 * equal portions of radian [0..PI/2)).
 */
public class Seg implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Vec start;
	private int angle;
	
	public Seg(Vec start, double angle)
	{
		this.start = start;
		this.angle = ((int)((angle / Math.PI + 2) * Specs.NUM_DISTINCT_ANGLES) + Specs.NUM_DISTINCT_ANGLES)
			% Specs.NUM_DISTINCT_ANGLES;
	}
	
	private Seg(Vec start, int distinctAngle)
	{
		this.start = start;
		this.angle = distinctAngle;
	}
	
	public Vec Start()
	{
		return start.clone();
	}
	
	public int Angle()
	{
		return angle;
	}
	
	public double RealAngle()
	{
		return angle * (Math.PI / Specs.NUM_DISTINCT_ANGLES);
	}
	
	@Override
	public String toString()
	{
		return start.toString() + " @ " + angle;
	}
	
	public Seg clone()
	{
		return new Seg(start, (int)angle);
	}
}
