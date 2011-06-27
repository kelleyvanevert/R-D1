/*=============================================================================
 * CallByDoodle
 * 
 * CallByDoodle is an Android project created in the Spring of 2011
 * for Research & Development 1, a course the authors follow at
 * Radboud University, Nijmegen.
 * 
 * Authors:
 * Roelf Leenders
 * Kelley van Evert
 * Jochem Kooistra
 * 
 * Supervisors:
 * Erik Barendsen
 * Sjaak Smetsers
 * 
 * Project started in February of 2011, scheduled for completion 
 * in the summer of 2011.
 ============================================================================*/

package studie.callbydoodle;

import studie.callbydoodle.data.Doodle;
import studie.callbydoodle.data.DoodleSegment;
import studie.callbydoodle.data.Vec;
import studie.callbydoodle.themes.*;
import android.os.SystemClock;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * The view in which the user can draw doodles.
 * How does it work?
 * It actively records a Doodle object.
 * Every time a touch event occurs, it:
 *  1. updates recording doodle as needed
 *  2. redraws the entire screen by calling the theme's "drawDoodle" method
 * 
 * This may be an inefficient way of working, but it is very clean and logical.
 * I don't think we'll really run into any problems, but if we do, I'm sure we'll
 *  find a way to optimize this technique without much alteration to the core idea.
 */
public class DoodleView extends View
{
	// These are necessary for drawing..
	private Bitmap bitmap;
	private Canvas canvas;
	Bitmap mBackground;
    
	// You have to move at least this amount for the view to respond to a line-draw
	private final int MOVE_THRESHOLD = 4;
	
	// Max amount of time between first tap down and second tap up in milliseconds to register double click
	private final int DOUBLECLICK_TIME_THRESHOLD = 500;
	
	// Check for double taps
	private long[] taps;
	
	// Keep quick and easy accounts on details of last and current event.
	@SuppressWarnings("unused")
	private MotionEvent lastEvent;
	private long lastTime;
	private Vec lastVec;
	private int lastPressure;
	private MotionEvent currentEvent;
	private long currentTime;
	private Vec currentVec;
	private int currentPressure;
	
	// Is a new stroke starting?
	private boolean moved;
	
	// We're currently recording this doodle
	private Doodle doodle;
	// Whether the user is drawing at this moment
	private boolean drawing = false;
	
	// Theme
	private DoodleTheme theme;
	
	public DoodleView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setFocusable(true);
		
		taps = new long[2];
		taps[0] = SystemClock.uptimeMillis() - 1000;
		taps[1] = SystemClock.uptimeMillis() - 1000;
		
		// Currently recording doodle
		doodle = new Doodle();
		
		// Load theme setting
		theme = new DefaultTheme();
	}
	
	public void setTheme(DoodleTheme t)
	{
		theme = t;
		invalidate();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		// Create Canvas based on Bitmap, and clear it with color white
		bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		canvas = new Canvas(bitmap);
		canvas.drawColor(Color.WHITE);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		drawDoodle();		
		canvas.drawBitmap(bitmap, 0, 0, null);
	}
	
	private void drawDoodle()
	{
		theme.drawDoodle(canvas, doodle);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// Bookkeeping
		currentEvent = event;
		currentTime = SystemClock.uptimeMillis();
		currentVec = new Vec(currentEvent.getX(), currentEvent.getY());
		currentPressure = (int)(currentEvent.getPressure() * 100);
		
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			taps[0] = taps[1];
			taps[1] = currentTime;
			
			moved = false;
		}
		else if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			// Here comes the crux..
			Vec move = currentVec.subtract(lastVec);
			
			// If the location of the current MotionEvent is far enough away from the last
			// known touch location, its a new segment
			if (move.getLength() > MOVE_THRESHOLD)
			{
				moved = true;
				drawing = true;
				dispathDoodleViewEvent(DOODLE_VIEW_EVENT_START_DRAW_DOODLE);
				
				// Simply add segment to recording doodle
				doodle.addDoodleSegment(
						new DoodleSegment(
								lastVec, currentVec,
								lastPressure, currentPressure
								)
						);
				
				// Then request redraw
				invalidate();
			}
		}
		else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			if (!moved && currentTime - taps[0] < DOUBLECLICK_TIME_THRESHOLD)
			{
				startNewDrawing();
			}
			else
			{
				// Request redraw
				invalidate();
				
				drawing = false;
				dispathDoodleViewEvent(DOODLE_VIEW_EVENT_COMPLETE_DOODLE);
			}
		}
		
		// Bookkeeping
		lastEvent = currentEvent;
		lastTime = currentTime;
		lastVec = currentVec;
		lastPressure = currentPressure;
		
		return true;
	}
	
	public void startNewDrawing()
	{
		dispathDoodleViewEvent(DOODLE_VIEW_EVENT_CLEAR_VIEW);
		
		// Stop recording
		doodle = new Doodle();
		
		// Request redraw
		invalidate();
	}
	
	/**
	 * Check whether the view has a doodle
	 */
	public boolean hasDoodle()
	{
		return doodle.getSegments().size() > 0;
	}
	
	/**
	 * Check whether the user has completed a doodle
	 */
	public boolean hasCompletedDoodle()
	{
		return hasDoodle() && !drawing;
	}
	
	/**
	 * Check whether the user is drawing
	 */
	public boolean isDrawing()
	{
		return drawing;
	}
	
	/**
	 * Get a clone of the currently drawn doodle.
	 * Beware! This could be a dummy doodle, first
	 *  use hasDoodle() or hasCompletedDoodle() to check
	 *  whether the user has drawn and/or completed a doodle.
	 */
	public Doodle getDoodle()
	{
		return doodle.clone();
	}
	
	/**
	 * Sets the doodle to be shown
	 * Beware! This will only work when !isDrawing()
	 * Beware! You might be removing the user's precious doodle!
	 */
	public void setDoodle(Doodle d)
	{
		doodle = d;
		invalidate();
	}
	
	public Rect getSize()
	{
		return new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
	
	public static interface DoodleViewListener {
		public void onStartDrawDoodle();
		public void onCompleteDoodle();
		public void onClearView();
	}
	private DoodleViewListener listener;
	private static final int DOODLE_VIEW_EVENT_START_DRAW_DOODLE = 0;
	private static final int DOODLE_VIEW_EVENT_COMPLETE_DOODLE = 1;
	private static final int DOODLE_VIEW_EVENT_CLEAR_VIEW = 2;
	private void dispathDoodleViewEvent(int e) {
		if (listener != null) {
			if (e == DOODLE_VIEW_EVENT_START_DRAW_DOODLE) {
				listener.onStartDrawDoodle();
			} else if (e == DOODLE_VIEW_EVENT_COMPLETE_DOODLE) {
				listener.onCompleteDoodle();
			} else if (e == DOODLE_VIEW_EVENT_CLEAR_VIEW) {
				listener.onClearView();
			}
		}
	}
	public void setDoodleViewListener(DoodleViewListener listener) {
		this.listener = listener;
	}
}