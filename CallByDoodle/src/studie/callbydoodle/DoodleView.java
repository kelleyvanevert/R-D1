package studie.callbydoodle;

import java.util.ArrayList;

import android.os.SystemClock;
import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GesturePoint;
import android.gesture.GestureStroke;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;



public class DoodleView extends View
{
	private Bitmap bitmap;
	private Canvas canvas;
	
	//Using an image for background
	private Paint paint;
	private Paint bitmapPaint;
	Bitmap mBackground;
    	
	//Themes will come later, but for now hardcode a choice
	private final int THEME_BACKGROUND = 0;
	private final int THEME_HUE_ROTATE = 1;
	private final int CURRENT_THEME = 0;//THEME_HUE_ROTATE;
	
	//Using colour rotation for background
	// Color rotation: current HUE in HSV
	// HUE <= [0 .. 360)
	private int hue_rotate;
	private final int HUE_ROTATE_STEP = 3;
	private final float HUE_COLOR_SATURATION = 1;
	private final float HUE_COLOR_VALUE = (float)0.7;
	
	// You have to move at least this amount for the view to respond to a line-draw
	private final int MOVE_THRESHOLD = 4;
	
	// Motionevent.getPressure() times this constant to get the drawing radius
	private final int PRESSURE_TO_RADIUS = 60;
	
	// Max amount of time between first tap down and second tap up in ms to register double click
	private final int DOUBLECLICK_TIME_THRESHOLD = 300;
	
	
	
	private long[] taps;
	
	private MotionEvent lastEvent;
	private long lastTime;
	private Vec lastVec;
	private Vec lastPointLeft;
	private Vec lastPointRight;
	
	private MotionEvent currentEvent;
	private long currentTime;
	private Vec currentVec;
		
	//Is a new stroke starting?
	private boolean moved;
	
	//Size a point created by 'clicking' the screen
	private static final int TOUCH_DOT_SIZE = 15; 
	//Minimum distance between successive touchpoints to register as different strokes
	private static final int NEW_STROKE_MINDIST = 4;
	
	private ArrayList<DPoint> drawPaths;
	private ArrayList<Circle> drawCircles;
	
	
	/**
	 * DPoint is een Path gecombineerd met een timestamp zodat we van de paden later
	 * nog een Gesture kunnen construeren
	 * 
	 *
	 */
	private class DPoint extends Path
	{
		private long timeStamp;
		private Vec start, end;
		private float pStart, pEnd;
		/**
		 * Construct als path met een tijd
		 */
		public DPoint(Path path, long timeStamp, Vec start, Vec end, float pStart, float pEnd)
		{
			super(path);
			this.timeStamp = timeStamp;
			//Initialize vectors
			this.start = start;
			this.end = end;
			//Initialize pressure
			this.pStart = pStart;
			this.pEnd = pEnd;
		}

		/**
		 * Zonder vectors/pressure
		 * 
		 * @param path Om te tekenen
		 * @param timeStamp Aanraaktijd van dit doodlepoint
		 */
		public DPoint(Path path, long timeStamp)
		{
			super(path);
			this.timeStamp = timeStamp;
		}
		
		/**
		 * Of zonder gegevens
		 */
		public DPoint()
		{
			super();			
		}
		
		/* Basic getters */
		public long getTime()
		{
			return timeStamp;
		}
		
		public Vec getStartPos()
		{
			return start;
		}

		public Vec getEndPos()
		{
			return end;
		}		
		
		/* Setters */
		public void setTime(long timeStamp)
		{
			this.timeStamp = timeStamp;
		}
		
		public void setVectors(Vec start, Vec end)
		{
			this.start = start;
			this.end = end;
		}
		
		public void setPressure(int pStart, int pEnd)
		{
			this.pStart = pStart;
			this.pEnd = pEnd;
		}

		public float getPressureStart(){ return pStart; }
		
		public float getPressureEnd(){ return pEnd; }
	}
	
	
	public DoodleView(Context context)
	{
		super(context);
		setFocusable(true);
		
		//hue_rotate = 0;
		
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(3);
		paint.setColor(Color.BLACK);
		
		bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);	//or DITHER_FLAG, maakt niet echt uit	
		
		taps = new long[2];
		taps[0] = SystemClock.uptimeMillis() - 1000;
		taps[1] = SystemClock.uptimeMillis() - 1000;
		
		//left = new Path();
		//right = new Path();
		drawPaths = new ArrayList<DPoint>();
		drawCircles = new ArrayList<Circle>();

	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		//Create Canvas based on Bitmap, and clear it with color white
		bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		canvas = new Canvas(bitmap);
		canvas.drawColor(Color.WHITE);
		
		//Resize background bitmap too
		mBackground = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.background), w, h, true);	
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		
		drawBackground();
		drawPaths();		
		canvas.drawBitmap(bitmap, 0, 0, null);
	}
	
	private void drawBackground()
	{
		switch(CURRENT_THEME)
		{
			case THEME_HUE_ROTATE:
				hue_rotate = (hue_rotate + HUE_ROTATE_STEP) % 360;
				canvas.drawColor(Color.HSVToColor(new float[] {hue_rotate, HUE_COLOR_SATURATION, HUE_COLOR_VALUE}));				
			break;
			case THEME_BACKGROUND:
				canvas.drawBitmap(mBackground, new Matrix(), null);
			break;
		}			
	}
	
	
	private void drawPaths()
	{
		// We are no longer drawing a left and right path, but..
		//canvas.drawPath(left, paint);
		//canvas.drawPath(right, paint);
		// ..an array of "segment paths":
		for (Path p : drawPaths)
		{
			//paint.setColor(Color.rgb(100, 0, 0));
			canvas.drawPath(p, paint);
		}
		for (Circle c : drawCircles)
		{			
			canvas.drawCircle(c.getX(), c.getY(), c.getRadius(), paint);
			
		}
	}
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		currentEvent = event;
		currentTime = SystemClock.uptimeMillis();
		currentVec = new Vec(currentEvent.getX(), currentEvent.getY());
		
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			taps[0] = taps[1];
			taps[1] = currentTime;
			
			lastPointLeft = new Vec(currentEvent.getX(), currentEvent.getY());
			lastPointRight = new Vec(currentEvent.getX(), currentEvent.getY());
			
			moved = false;
		}
		else if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
		
			// Here comes the crux..
			Vec move = currentVec.subtract(lastVec);
			
			//If the location of the current MotionEvent is far enough away from the last
			//known touch location, its a new stroke
			if (move.getLength() > NEW_STROKE_MINDIST)
			{
				moved = true;
				
				Vec radiusVec = move.setLength(80 * currentEvent.getPressure());
				Vec pointLeft = currentVec.add(radiusVec.rotateLeft());
				Vec pointRight = currentVec.add(radiusVec.rotateRight());
				
				//Create a filled path bordering the new line segment
				DPoint segment = new DPoint();
				segment.moveTo(lastPointLeft.getX(), lastPointLeft.getY()); 
				segment.lineTo(lastPointRight.getX(), lastPointRight.getY());
				segment.lineTo(pointRight.getX(), pointRight.getY());
				segment.lineTo(pointLeft.getX(), pointLeft.getY());
				segment.lineTo(lastPointLeft.getX(), lastPointLeft.getY());
				//Set the other DPoint fields, time and the bare movement
				segment.setTime(event.getEventTime() - System.currentTimeMillis());
				segment.setVectors(lastVec, currentVec);
				drawPaths.add(segment);
				
				lastPointLeft = pointLeft;
				lastPointRight = pointRight;
				
				drawBackground();
				invalidate();
			}
		}
		else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			//left.lineTo(lastEvent.getX(), lastEvent.getY());
			//right.lineTo(lastEvent.getX(), lastEvent.getY());
			
			if (!moved)
			{
				if (currentTime - taps[0] < 350)
				{
					clearCanvas();
				}
				else
				{
					// draw dot(hebben we punten nodig?)
					drawCircles.add(new Circle(currentEvent.getX(), currentEvent.getY(), TOUCH_DOT_SIZE));
				}
			}
			
			canvas.drawColor(Color.WHITE);
			invalidate();
		}
		
		lastEvent = currentEvent;
		lastTime = currentTime;
		lastVec = currentVec;
		
		return true;
	}
	
	/**
	 * Redraw the Canvas
	 */
	public void clearCanvas()
	{
		// clear canvas
		drawPaths.clear();
		drawCircles.clear();
		canvas.drawColor(Color.WHITE);
		invalidate();		
	}
	
	/**
	 * Convert this object to a Gesture
	 * 
	 * @return Gesture containing the contents of this DoodleView
	 */
	public Gesture getGesture()
	{		
		DPoint lastPoint=null, pointDist=null;
		Gesture ges = new Gesture();
		ArrayList<GesturePoint> pointList = new ArrayList<GesturePoint>();
		
		Log.i("DBG","getGesture reporting!");
		
		for (DPoint dp : drawPaths)
		{	
			//Check if this point is the start of a new stroke
			if(lastPoint == null || (lastPoint.getEndPos().subtract(dp.getStartPos())).getLength() > NEW_STROKE_MINDIST)
			{
				//Log.i("DBG","");
				//New stroke, add previous one to Gesture
				if(pointList.size() > 0)
				{
					ges.addStroke(new GestureStroke(pointList));
				}
				//Empty the points list
				pointList.clear();
				
				//Add this point
				pointList.add(new GesturePoint(dp.getEndPos().getX(), dp.getEndPos().getY(), dp.getTime()));
			}
			else
			{
				//Add to last stroke
				pointList.add(new GesturePoint(dp.getEndPos().getX(), dp.getEndPos().getY(), dp.getTime()));
			}
			lastPoint = dp;
		}
		
		//Add last stroke
		if(pointList.size() > 0)
		{
			ges.addStroke(new GestureStroke(pointList));
		}
	
		return ges;
	}
	
	
	
	
	public boolean setGesture(Gesture ges)
	{
		//Get strokes from this gesture
		ArrayList<GestureStroke> strokes = ges.getStrokes();
		int i = 0;
		Log.i("DBG","setGesture reporting!");
		//Parse each
		for (GestureStroke gs : strokes)
		{
			Log.i("DBG","Stroke " + i + " Length: " + gs.length);
			
			//Get all points
			for(int j=0; j<gs.points.length; j++)
			{
				Log.i("DBG","Point: " + gs.points[j]);
			}
			
			i++;
			
		}
		
		Log.i("DBG","Einde van de loop: i= " + i);
		
		return true;
	}
	
	
}