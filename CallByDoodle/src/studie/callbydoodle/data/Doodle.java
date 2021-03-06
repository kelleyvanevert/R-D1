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
	private boolean firstSegment = false;
	
	// Keep track of bounding rect and specs
	private Rect rect;
	private Specs specs;
	
	/*
	 * Constructs a new, empty doodle, to be recorded.
	 */
	public Doodle()
	{
		segments = new ArrayList<DoodleSegment>();
		rect = new Rect(0, 0, 0, 0);
		specs = new Specs(this);
	}
	
	public void addDoodleSegment(DoodleSegment segment)
	{
		segments.add(segment);
		if (firstSegment) {
			// First added segment
			rect = segment.getRect();
			firstSegment = false;
		} else {
			rect = rect.union(segment.getRect());
		}
		specs.update(segment);
	}
	
	public Doodle clone()
	{
		Doodle clone = new Doodle();
		for (DoodleSegment segment : segments) {
			clone.segments.add(segment.clone());
		}
		clone.rect = new Rect(rect);
		clone.specs = specs.clone();
		clone.firstSegment = firstSegment;
		
		return clone;
	}
	
	public ArrayList<DoodleSegment> getSegments()
	{
		return segments;
	}
	
	public Rect getRect()
	{
		return new Rect(rect);
	}
	
	public Specs getSpecs()
	{
		return specs;
	}
}
