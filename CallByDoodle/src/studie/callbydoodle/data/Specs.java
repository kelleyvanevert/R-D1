
package studie.callbydoodle.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Specs implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static final double SEG_DISTANCE = 10;
	public static final int PIXELIZATION_SIZE = 50;
	public static final int PIXELIZATION_STROKE_WIDTH = 7;
	public static final int NUM_DISTINCT_ANGLES = 10;
	
	private ArrayList<Seg> segs;
	private AngleDistribution angleDistribution;
	//private Pixelization pixelization;
	
	public Specs()
	{
		segs = new ArrayList<Seg>();
		angleDistribution = new AngleDistribution();
		//pixelization = new Pixelization();
	}
	
	public ArrayList<Seg> getSegs()
	{
		return segs;
	}
	
	public AngleDistribution getAngleDistribution()
	{
		return angleDistribution.prepare();
	}
	
	/*
	public Pixelization getPixelization()
	{
		return pixelization;
	}
	*/
	
	public void update(DoodleSegment segment)
	{
		ArrayList<Seg> newsegs = segment.makeSegs(Specs.SEG_DISTANCE);
		segs.addAll(newsegs);
		for (Seg seg : newsegs) {
			angleDistribution.increment(seg.Angle());
		}
		
		//pixelization = new Pixelization(doodle);
	}
	
	// Compare
	public boolean like(Specs other)
	{
		int sim = AngleDistribution.similarity(getAngleDistribution(), other.getAngleDistribution());
		System.out.println("--similarity: "+sim);
		return sim >= 70;
	}
	
	public Specs clone()
	{
		Specs clone = new Specs();
		for (Seg seg : segs) {
			clone.segs.add(seg.clone());
		}
		clone.angleDistribution = new AngleDistribution(angleDistribution);
		
		return clone;
	}
}