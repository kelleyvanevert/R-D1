package studie.callbydoodle.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class DoodleSegment
{
	private static final long serialVersionUID = 1L;
	
	private Vec vecStart, vecEnd;
	private long timeStart, timeEnd;
	private float pressureStart, pressureEnd;
	
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
	}
	
	public Vec getVecStart()
	{
		return vecStart;
	}
	
	public Vec getVecEnd()
	{
		return vecEnd;
	}
	
	public long getTimeStart()
	{
		return timeStart;
	}
	
	public long getTimeEnd()
	{
		return timeEnd;
	}
	
	public float getPressureStart()
	{
		return pressureStart;
	}
	
	public float getPressureEnd()
	{
		return pressureEnd;
	}
	
	public static DoodleSegment parseDoodleSegment(String line) throws Exception
	{
		line = line.replaceAll("\\s", "").replaceAll("->", ";");
		String[] pieces = line.split(";");
		return new DoodleSegment(Vec.parseVec(pieces[0]), Vec.parseVec(pieces[1]),
				Long.parseLong(pieces[2]), Long.parseLong(pieces[3]),
				Float.parseFloat(pieces[4]), Float.parseFloat(pieces[5]));
	}
	
	public String serialize()
	{
		return vecStart.toString()+" -> "+vecEnd.toString()+"; "+
		       timeStart+" -> "+timeEnd+"; "+
		       pressureStart+" -> "+pressureEnd;
	}
}
