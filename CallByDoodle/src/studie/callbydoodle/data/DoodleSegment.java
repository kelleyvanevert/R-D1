package studie.callbydoodle.data;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * A segment within a Doodle object.
 * Roelf implemented the DPoint class, this is a refinement.
 * De DPoint class saved the complicated tetragon that I (Kelley)
 *  used to draw to the canvas, though this is not really important for
 *  the doodle.
 * We must only save the starting and ending points, and times, and pressures.
 * The view / theme must then choose to draw with tetragons or any other
 *  way (circles, lines, whatever..)
 * 
 * IMMUTABLE
 */
public class DoodleSegment implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Vec vecStart, vecEnd;
	private int pressureStart, pressureEnd;
	private Rect rect;
	
	// The time may come that you need a dummy segment..
	public static final DoodleSegment DUMMY = new DoodleSegment(new Vec(-1,-1), new Vec(-1,-1), 0, 0);
	
	public DoodleSegment(Vec vecStart, Vec vecEnd, int pressureStart, int pressureEnd)
	{
		this.vecStart = vecStart;
		this.vecEnd = vecEnd;
		this.pressureStart = pressureStart;
		this.pressureEnd = pressureEnd;
		rect = new Rect(vecStart, vecEnd);
	}
	
	public Vec getVecStart()
	{
		return vecStart;
	}
	
	public Vec getVecEnd()
	{
		return vecEnd;
	}
	
	public Vec getVecDiff()
	{
		return vecEnd.subtract(vecStart);
	}
	
	public int getPressureStart()
	{
		return pressureStart;
	}
	
	public int getPressureEnd()
	{
		return pressureEnd;
	}
	
	public int getPressureDiff()
	{
		return pressureEnd - pressureStart;
	}
	
	public Rect getRect()
	{
		return new Rect(rect);
	}
	
	public DoodleSegment clone()
	{
		return new DoodleSegment(vecStart.clone(), vecEnd.clone(),
				pressureStart, pressureEnd);
	}
	
	public String toString()
	{
		return vecStart.toString()+" -> "+vecEnd.toString()+"; "+
		       pressureStart+" -> "+pressureEnd;
	}
	
	public ArrayList<Seg> makeSegs(double segLength)
	{
		ArrayList<Vec> dots = Vec.getStops(vecStart, vecEnd, segLength);
		ArrayList<Seg> segs = new ArrayList<Seg>();
		
		double angle = vecEnd.subtract(vecStart).getAngle();
		for (Vec dot : dots) {
			segs.add(new Seg(dot.clone(), angle));
		}
		
		return segs;
	}
}
