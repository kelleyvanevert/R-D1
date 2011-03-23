package studie.callbydoodle;

import java.io.Serializable;
import java.util.ArrayList;

import android.gesture.Gesture;
import android.gesture.GesturePoint;
import android.gesture.GestureStroke;

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
public class Doodle
{
	private ArrayList<DoodleSegment> segments;
	
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
	}
	
	public ArrayList<DoodleSegment> getSegments()
	{
		return segments;
	}
	
	public String serialize()
	{
		String segs = "";
		for (DoodleSegment segment : segments) {
			segs += "    " + segment.serialize() + "\n";
		}
		return
			"Doodle {\n" +
			     segs +
			"}";
	}
	
	/**
	 * Get the equivalent gesture.
	 * 
	 * In the future, we might find out we need more for recognition,
	 *  in which case we'll change this method to a set of methods that
	 *  do slightly different things, without having to change the
	 *  data representation choice of this class (which is: storing a list
	 *  of doodle segments).
	 * @return the equivalent gesture.
	 */
	public Gesture getGesture()
	{
		/*
		 * Since the Vec class uses int's, it is very easy to check for
		 *  each following doodle segment whether it is a new gesture stroke
		 *  or not.
		 * I (Kelley) thought it would be neat to have the recording interface
		 *  only use the "addDoodleSegment" method, instead of a bunch of methods
		 *  like "startStroke", "addSegment", "endStroke", ..
		 * Besides, then we're just mimicking the gesture class again, but what
		 *  we want to do with this class is make it optional if necessary.
		 */
		Gesture gesture = new Gesture();
		ArrayList<GesturePoint> points = new ArrayList<GesturePoint>();
		DoodleSegment segment, previousSegment = DoodleSegment.DUMMY;
		for (int i = 0; i < segments.size(); i++) {
			segment = segments.get(i);
			if (i == 0) {
				// For the first segment, store both the starting and ending point
				points.add(new GesturePoint((float)segment.getVecStart().getX(),
            	        (float)segment.getVecStart().getY(),
            	        segment.getTimeStart()));
				points.add(new GesturePoint((float)segment.getVecEnd().getX(),
            	        (float)segment.getVecEnd().getY(),
            	        segment.getTimeEnd()));
			} else {
				if (!previousSegment.getVecEnd().equals(segment.getVecStart())) {
					// We have to start a new gesture stroke
					// First, create and save current gesture stroke
					gesture.addStroke(new GestureStroke(points));
					// Initialize new list of points, start recording new gesture stroke..
					points = new ArrayList<GesturePoint>();
					points.add(new GesturePoint((float)segment.getVecStart().getX(),
	            	        (float)segment.getVecStart().getY(),
	            	        segment.getTimeStart()));
					points.add(new GesturePoint((float)segment.getVecEnd().getX(),
	            	        (float)segment.getVecEnd().getY(),
	            	        segment.getTimeEnd()));
				} else {
					// Continue creating current gesture stroke
					points.add(new GesturePoint((float)segment.getVecEnd().getX(),
	            	        (float)segment.getVecEnd().getY(),
	            	        segment.getTimeEnd()));
				}
			}
			previousSegment = segment;
		}
		
		// Finally, create and save the current gesture stroke
		gesture.addStroke(new GestureStroke(points));
		
		return gesture;
	}
}
