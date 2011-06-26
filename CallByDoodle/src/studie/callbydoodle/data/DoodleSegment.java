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
	private long timeStart, timeEnd;
	private float pressureStart, pressureEnd;
	private Rect rect;
	
	// The time may come that you need a dummy segment..
	public static final DoodleSegment DUMMY = new DoodleSegment(new Vec(-1,-1), new Vec(-1,-1), (long)0, (long)0, (float)0, (float)0);
	
	public DoodleSegment(Vec vecStart, Vec vecEnd, long timeStart, long timeEnd, float pressureStart, float pressureEnd)
	{
		this.vecStart = vecStart;
		this.vecEnd = vecEnd;
		this.timeStart = timeStart;
		this.timeEnd = timeEnd;
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
	
	public long getTimeStart()
	{
		return timeStart;
	}
	
	public long getTimeEnd()
	{
		return timeEnd;
	}
	
	public long getTimeDiff()
	{
		return timeEnd - timeStart;
	}
	
	public float getPressureStart()
	{
		return pressureStart;
	}
	
	public float getPressureEnd()
	{
		return pressureEnd;
	}
	
	public float getPressureDiff()
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
				timeStart, timeEnd,
				pressureStart, pressureEnd);
	}
	
	public String toString()
	{
		return vecStart.toString()+" -> "+vecEnd.toString()+"; "+
		       timeStart+" -> "+timeEnd+"; "+
		       pressureStart+" -> "+pressureEnd;
	}
	
	public Pair<ArrayList<Vec>, ArrayList<Seg>> makeDotsAndSegs(double segLength)
	{
		ArrayList<Vec> dots = new ArrayList<Vec>();
		ArrayList<Seg> segs = new ArrayList<Seg>();
		
		Vec vecDiff = getVecDiff();
		double angle = vecDiff.getAngle();
		double length = vecDiff.getLength();
		double totalSegLength = 0;
		Vec at = vecStart.clone();
		dots.add(at);
		Vec between = new Vec(1, (int)Math.tan(angle)).setLength((float)segLength);
		while (totalSegLength < length) {
			segs.add(new Seg(at, angle));
			totalSegLength += segLength;
			at = at.add(between);
			dots.add(at);
		}
		
		return new Pair<ArrayList<Vec>, ArrayList<Seg>>(dots, segs);
	}
}
