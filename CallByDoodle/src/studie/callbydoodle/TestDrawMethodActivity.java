package studie.callbydoodle;

import java.util.ArrayList;

import android.net.Uri;
import android.os.SystemClock;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.gesture.*;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class TestDrawMethodActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(new DoodleView(this));
    }
    
    private static class DoodleView extends View
    {
    	private Bitmap bitmap;
    	private Canvas canvas;
    	
    	private Paint paint;
    	private Paint bitmapPaint;
    	
    	// Color rotation: current HUE in HSV
    	// HUE <= [0 .. 360)
    	private int hue_rotate;
    	private final int HUE_ROTATE_STEP = 3;
    	private final float COLOR_SATURATION = 1;
    	private final float COLOR_VALUE = (float)0.7;
    	
		public DoodleView(Context context)
		{
			super(context);
			setFocusable(true);
			
			hue_rotate = 0;
			
			paint = new Paint();
			paint.setStyle(Paint.Style.FILL);
			paint.setStrokeWidth(3);
			paint.setColor(Color.BLACK);
			
			bitmapPaint = new Paint(Paint.DITHER_FLAG);
			
			taps = new long[2];
			taps[0] = SystemClock.uptimeMillis() - 1000;
			taps[1] = SystemClock.uptimeMillis() - 1000;
			
			//left = new Path();
			//right = new Path();
			drawPaths = new ArrayList<Path>();
			drawCircles = new ArrayList<Circle>();
		}
		
		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh)
		{
			super.onSizeChanged(w, h, oldw, oldh);
			bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
			canvas = new Canvas(bitmap);
			canvas.drawColor(Color.WHITE);
		}
		
		@Override
		protected void onDraw(Canvas canvas)
		{
			drawPaths();
			canvas.drawBitmap(bitmap, 0, 0, null);
		}
		
		private void drawRotatedBackgroundColor()
		{
			hue_rotate = (hue_rotate + HUE_ROTATE_STEP) % 360;
			canvas.drawColor(Color.HSVToColor(new float[] {hue_rotate, COLOR_SATURATION, COLOR_VALUE}));
		}
		
		private void drawPaths()
		{
			// We are no longer drawing a left and right path, but..
			//canvas.drawPath(left, paint);
			//canvas.drawPath(right, paint);
			// ..an array of "segment paths":
			for (Path p : drawPaths)
			{
				canvas.drawPath(p, paint);
			}
			for (Circle c : drawCircles)
			{
				canvas.drawCircle(c.getX(), c.getY(), c.getRadius(), paint);
			}
		}
		
		private long[] taps;
		
		private MotionEvent lastEvent;
		private long lastTime;
		private Vec lastVec;
		private Vec lastPointLeft;
		private Vec lastPointRight;
		
		private MotionEvent currentEvent;
		private long currentTime;
		private Vec currentVec;
		
		private boolean moved;
		
		//private Path left;
		//private Path right;
		
		private ArrayList<Path> drawPaths;
		private ArrayList<Circle> drawCircles;
		
		@Override
		public boolean onTouchEvent(MotionEvent event)
		{
			currentEvent = event;
			currentTime = SystemClock.uptimeMillis();
			currentVec = new Vec(currentEvent.getX(), currentEvent.getY());
			
			if (event.getActionMasked() == MotionEvent.ACTION_DOWN)
			{
				taps[0] = taps[1];
				taps[1] = currentTime;
				
				//left.reset();
				//right.reset();
				//left.moveTo(currentEvent.getX(), currentEvent.getY());
				//right.moveTo(currentEvent.getX(), currentEvent.getY());
				
				lastPointLeft = new Vec(currentEvent.getX(), currentEvent.getY());
				lastPointRight = new Vec(currentEvent.getX(), currentEvent.getY());
				
				moved = false;
			}
			else if (event.getActionMasked() == MotionEvent.ACTION_MOVE)
			{
				//mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
				// ..where mX / mY old; x / y new
				/*
				left.quadTo(lastEvent.getX(), lastEvent.getY(),
						(currentEvent.getX()+lastEvent.getX())/2, (currentEvent.getY()+lastEvent.getY())/2);
				right.quadTo(lastEvent.getX(), lastEvent.getY(),
						(currentEvent.getX()+lastEvent.getX())/2, (currentEvent.getY()+lastEvent.getY())/2);
				*/
				
				// Here comes the crux..
				Vec move = currentVec.subtract(lastVec);
				if (move.getLength() > 4)
				{
					moved = true;
					
					Vec radiusVec = move.setLength(80 * currentEvent.getPressure());
					Vec pointLeft = currentVec.add(radiusVec.rotateLeft());
					Vec pointRight = currentVec.add(radiusVec.rotateRight());
					//left.lineTo(pointLeft.getX(), pointLeft.getY());
					//right.lineTo(pointRight.getX(), pointRight.getY());
					
					Path segment = new Path();
					segment.moveTo(lastPointLeft.getX(), lastPointLeft.getY());
					segment.lineTo(lastPointRight.getX(), lastPointRight.getY());
					segment.lineTo(pointRight.getX(), pointRight.getY());
					segment.lineTo(pointLeft.getX(), pointLeft.getY());
					segment.lineTo(lastPointLeft.getX(), lastPointLeft.getY());
					drawPaths.add(segment);
					
					lastPointLeft = pointLeft;
					lastPointRight = pointRight;
					
					drawRotatedBackgroundColor();
					invalidate();
				}
			}
			else if (event.getActionMasked() == MotionEvent.ACTION_UP)
			{
				//left.lineTo(lastEvent.getX(), lastEvent.getY());
				//right.lineTo(lastEvent.getX(), lastEvent.getY());
				
				if (!moved)
				{
					if (currentTime - taps[0] < 350)
					{
						// clear canvas
						drawPaths.clear();
						drawCircles.clear();
					}
					else
					{
						// draw dot
						drawCircles.add(new Circle(currentEvent.getX(), currentEvent.getY(), 15));
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
    }
}