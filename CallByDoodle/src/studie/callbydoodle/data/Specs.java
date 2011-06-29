
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

	public static final double TOTAL_DISTRIBUTION_WEIGHT = 5;
	public static final double PARTIAL_DISTRIBUTION_WEIGHT = 3;
	
	public static final int LIKENESS_THRESHOLD = 70;
	
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
	public static int likeness(Specs a, Specs b)
	{
		StringBuilder logEntry = new StringBuilder("Specs.like() -- ");
		
		double likeness = 0.0;
		double totalWeight = 0.0;
		
		int totalSim = AngleDistribution.similarity(
				a.getPreparedAngleDistribution(TOTAL),
				b.getPreparedAngleDistribution(TOTAL)
		);
		likeness += totalSim * TOTAL_DISTRIBUTION_WEIGHT;
		totalWeight += TOTAL_DISTRIBUTION_WEIGHT;
		logEntry.append(" Total: "+totalSim);
		
		for (int i = 1; i < 5; i++) {
			int partialSim = AngleDistribution.similarity(
					a.getPreparedAngleDistribution(i),
					b.getPreparedAngleDistribution(i)
			);
			likeness += partialSim * PARTIAL_DISTRIBUTION_WEIGHT;
			totalWeight += PARTIAL_DISTRIBUTION_WEIGHT;
			logEntry.append(", Partial#"+i+": "+partialSim);
		}
		
		int likenessInt = (int)(likeness / totalWeight);
		logEntry.append(" --> Likeness, weighted: " + likeness);
		Log.i("Specs.likeness()", logEntry.toString());
		
		return likenessInt;
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