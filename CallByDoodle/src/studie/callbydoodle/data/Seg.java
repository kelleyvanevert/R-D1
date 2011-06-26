package studie.callbydoodle.data;

/**
 * A "Seg", in lack of a better name,
 * stands for a tiny (both in pixels, as in data)
 * segment of a doodle: it holds a starting point (Vec)
 * and an "angle" (the given angle in radians is
 * transformed to an int in [0..PRECISION) representing
 * equal portions of radian [0..PI/2)).
 */
public class Seg
{
	private Vec start;
	private int angle;
	
	public Seg(Vec start, double angle)
	{
		this.start = start;
		this.angle = ((int)((angle / Math.PI + 2) * Doodle.Specs.DISTINCT_ANGLES) + Doodle.Specs.DISTINCT_ANGLES)
			% Doodle.Specs.DISTINCT_ANGLES;
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
		return angle * (Math.PI / Doodle.Specs.DISTINCT_ANGLES);
	}
	
	@Override
	public String toString()
	{
		return start.toString() + " @ " + angle;
	}
}
