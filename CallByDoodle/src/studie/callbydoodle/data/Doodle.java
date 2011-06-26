package studie.callbydoodle.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The doodle class is currently just a generalisation of the gesture.
 * We do not know if gestures will serve as our full solution, therefore
 *  we implement this class: if we find out it isn't, we needn't
 *  change the whole class structure of the program.
 * 
 * The first purpose of this doodle class is to provide an
 *  interface for recording and retrieving information for drawing purposes:
 *     The doodle-drawing view actively records a doodle,
 *      then draws the doodle.
 *     Theming is handled by extending the view, and specifically those
 *      methods that deal with turning the Doodle object into visual stuff.
 * The second purpose of this doodle class is to provide an
 *  interface for doodle recognition techniques. This is the part
 *  where we are not sure what we'll need yet. Is the gesture interface
 *  good enough or not? That's why we implement this class.
 * 
 * To sum it up: this class serves a semantic purpose.
 * It will record enough data to provide both an rich interface for drawing
 *  as well for an interface for recognition techniques which can be altered
 *  later according to need.
 */
public class Doodle implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<DoodleSegment> segments;
	private Rect rect = new Rect(0, 0, 0, 0);
	
	/*
	 * Constructs a new, empty doodle, to be recorded.
	 */
	public Doodle()
	{
		segments = new ArrayList<DoodleSegment>();
	}
	
	public void addDoodleSegment(DoodleSegment segment)
	{
		segments.add(segment);
		rect = rect.union(segment.getRect());
	}
	
	public ArrayList<DoodleSegment> getSegments()
	{
		return segments;
	}
	
	public Doodle clone()
	{
		Doodle clone = new Doodle();
		for (DoodleSegment segment : segments) {
			clone.addDoodleSegment(segment.clone());
		}
		return clone;
	}
	
	public static class Specs
	{
		public static final int DOT_DISTANCE = 10;
		public static final int PIXELIZATION_SIZE = 20;
		public static final int PIXELIZATION_PAD = 2;
		public static final int DISTINCT_ANGLES = 10;
		
		// Segs
		private ArrayList<Seg> segs;
		public void setSegs(ArrayList<Seg> s) {
			segs = s;
		}
		public ArrayList<Seg> getSegs() {
			return segs;
			// TODO clone
		}
		
		// Angle distribution
		private int[] angleDistribution;
		public void setAngleDistribution(int[] d) {
			angleDistribution = d;
		}
		public int[] getAngleDistribution() {
			return angleDistribution.clone();
		}
		
		// Pixelization
		private Pixelization pixelization;
		public void setPixelization(Pixelization px) {
			pixelization = px;
		}
		public Pixelization getPixelization() {
			return pixelization.clone();
		}
		
		// Compare
		public boolean like(Specs other)
		{
			return true;
		}
	}
	
	public Specs getSpecs()
	{
		Specs specs = new Specs();
		
		ArrayList<Vec> dots = new ArrayList<Vec>();
		ArrayList<Seg> segs = new ArrayList<Seg>();
		for (DoodleSegment segment : segments) {
			Pair<ArrayList<Vec>, ArrayList<Seg>> dotsAndSegs = segment.makeDotsAndSegs(Specs.DOT_DISTANCE);
			dots.addAll(dotsAndSegs.A());
			segs.addAll(dotsAndSegs.B());
		}
		Vec.normalize(dots, Specs.PIXELIZATION_SIZE);
		
		specs.setSegs(segs);
		
		// Analyse the angle distribution of small segments of
		// the doodle.
		// An improvement (TODO) would be to not "double analyse"
		// segs drawn "over each other".
		int[] distribution = new int[Doodle.Specs.DISTINCT_ANGLES];
		for (Seg seg : segs) {
			distribution[seg.Angle()]++;
		}
		specs.setAngleDistribution(Distributions.prepare(distribution));
		
		// "Pixelize"
		int[][] pixelization = new int[Specs.PIXELIZATION_SIZE][Specs.PIXELIZATION_SIZE];
		for (Vec dot : dots) {
			/*
			ArrayList<Vec> nei = dot.neighbours(Specs.PIXELIZATION_SIZE, Specs.PIXELIZATION_PAD);
			for (Vec d : nei) {
				try {
					pixelization[d.getY()][d.getX()] = 1;
				} catch (Exception e) {}
			}
			*/
			//pixelization[dot.getY()][dot.getX()] = 1;
		}
		specs.setPixelization(new Pixelization(pixelization));
		
		return specs;
	}
}
