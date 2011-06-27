
package studie.callbydoodle.data;

import java.io.Serializable;
import java.util.ArrayList;

import android.util.Log;

public class Specs implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static final double SEG_DISTANCE = 10;
	public static final int PIXELIZATION_SIZE = 50;
	public static final int PIXELIZATION_STROKE_WIDTH = 7;
	public static final int NUM_DISTINCT_ANGLES = 7;
	
	public static final int TOTAL_DISTRIBUTION_SIMILARITY_THRESHOLD = 60;
	public static final int PARTIAL_DISTRIBUTION_SIMILARITY_THRESHOLD = 60;
	
	private ArrayList<Seg> segs;
	private AngleDistribution[] distributions;
	public static final int TOTAL = 0;
	public static final int TOPLEFT = 1;
	public static final int TOPRIGHT = 2;
	public static final int BOTTOMLEFT = 3;
	public static final int BOTTOMRIGHT = 4;
	
	private Doodle owner;
	
	public Specs(Doodle owner)
	{
		this.owner = owner;
		
		segs = new ArrayList<Seg>();
		
		distributions = new AngleDistribution[5];
		for (int i = 0; i < distributions.length; i++) {
			distributions[i] = new AngleDistribution();
		}
	}
	
	public ArrayList<Seg> getSegs()
	{
		return segs;
	}
	
	public AngleDistribution getPreparedAngleDistribution(int which)
	{
		try {
			return distributions[which].prepare();
		} catch (Exception e) {
			return null;
		}
	}
	
	public void update(DoodleSegment segment)
	{
		ArrayList<Seg> newsegs = segment.makeSegs(Specs.SEG_DISTANCE);
		segs.addAll(newsegs);
		
		for (int i = 1; i < distributions.length; i++) {
			distributions[i].reset();
		}
		for (Seg seg : newsegs) {
			distributions[TOTAL].increment(seg.Angle());
		}
		for (Seg seg : segs) {
			distributions[getPartialPositionOf(seg.Start())].increment(seg.Angle());
		}
	}
	
	private int getPartialPositionOf(Vec v)
	{
		Rect bound = owner.getRect();
		int x = v.getX(),
			y = v.getY(),
			height = bound.Height(),
			halfheight = height / 2,
			width = bound.Width(),
			halfwidth = width / 2;
		
		if (y < halfheight) {
			if (x < halfwidth) {
				return TOPLEFT;
			} else {
				return TOPRIGHT;
			}
		} else {
			if (x < halfwidth) {
				return BOTTOMLEFT;
			} else {
				return BOTTOMRIGHT;
			}
		}
	}
	
	// Compare
	public boolean like(Specs other)
	{
		StringBuilder logEntry = new StringBuilder("Specs.like() -- ");
		
		int totalSim = AngleDistribution.similarity(
				this.getPreparedAngleDistribution(TOTAL),
				other.getPreparedAngleDistribution(TOTAL)
		);
		logEntry.append(" Total ["+totalSim+"/"+TOTAL_DISTRIBUTION_SIMILARITY_THRESHOLD+"]");
		if (totalSim < TOTAL_DISTRIBUTION_SIMILARITY_THRESHOLD) {
			logEntry.append(" CANCELLED");
			Log.v("Specs.like()", logEntry.toString());
			return false;
		}
		
		for (int i = 1; i < distributions.length; i++) {
			int partialSim = AngleDistribution.similarity(
					this.getPreparedAngleDistribution(i),
					other.getPreparedAngleDistribution(i)
			);
			logEntry.append(" Partial #"+i+" ["+partialSim+"/"+PARTIAL_DISTRIBUTION_SIMILARITY_THRESHOLD+"]");
			if (partialSim < PARTIAL_DISTRIBUTION_SIMILARITY_THRESHOLD) {
				logEntry.append(" CANCELLED");
				Log.v("Specs.like()", logEntry.toString());
				return false;
			}
		}
		
		logEntry.append(" SUCCESS!");
		Log.v("Specs.like()", logEntry.toString());
		return true;
	}
	
	public Specs clone()
	{
		Specs clone = new Specs(owner);
		for (Seg seg : segs) {
			clone.segs.add(seg.clone());
		}
		for (int i = 0; i < distributions.length; i++) {
			clone.distributions[i] = new AngleDistribution(distributions[i]);
		}
		
		return clone;
	}
}