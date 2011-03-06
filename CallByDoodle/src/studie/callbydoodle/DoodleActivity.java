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
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class DoodleActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(new DoodleView(this));
    }
    
    private static class DoodleView extends View
    {
    	// To draw
    	private Bitmap bitmap;
    	private Canvas canvas;
    	
    	// Paint to draw circles
    	private Paint paint;
    	
    	// Color rotation: current HUE in HSV
    	// HUE <= [0 .. 360)
    	private int hue_rotate;
    	private final int HUE_ROTATE_STEP = 3;
    	private final float COLOR_SATURATION = 1;
    	private final float COLOR_VALUE = (float)0.7;
    	
    	private ArrayList<Circle> circles;
    	
		public DoodleView(Context context)
		{
			super(context);
			setFocusable(true);
			
			hue_rotate = 0;
			
			paint = new Paint();
			paint.setStyle(Paint.Style.FILL);
			paint.setStrokeWidth(10);
			paint.setColor(Color.BLACK);
			
			taps = new long[2];
			taps[0] = SystemClock.uptimeMillis() - 1000;
			taps[1] = SystemClock.uptimeMillis() - 1000;
			
			circles = new ArrayList<Circle>();
		}
		
		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh)
		{
			super.onSizeChanged(w, h, oldw, oldh);
			bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
			canvas = new Canvas(bitmap);
		}
		
		@Override
		protected void onDraw(Canvas canvas)
		{
			canvas.drawBitmap(bitmap, 0, 0, null);
		}
		
		private long[] taps;
		
		private MotionEvent lastEvent;
		private long lastTime;
		
		private MotionEvent currentEvent;
		private long currentTime;
		
		@Override
		public boolean onTouchEvent(MotionEvent event)
		{
			currentEvent = event;
			currentTime = SystemClock.uptimeMillis();
			
			if (event.getActionMasked() == MotionEvent.ACTION_DOWN)
			{
				taps[0] = taps[1];
				taps[1] = currentTime;
			}
			else if (event.getActionMasked() == MotionEvent.ACTION_MOVE)
			{
				makeCircles();				
				drawCanvas();
				invalidate();
			}
			else if (event.getActionMasked() == MotionEvent.ACTION_UP)
			{
				System.out.println("-- Taps: "+(taps[1] - taps[0])+", "+(currentTime - taps[1]));
				
				// taps[0] <-- touch down event
				// taps[1] <-- touch down event nummer 2
				// currentTime <-- huidige tijd
				// als (currentTime - taps[0]) < 350 dan is er dus minder dan 350 ms verstreken
				// sinds de eerste van twee touch down events en het huidige touch up event:
				// er is dus sprake van een dubbel-klik
				if (currentTime - taps[0] < 350)
				{
					// Hier zouden we bijvoorbeeld kunnen gaan bellen..
					//Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:0317XXXXXX"));
					//getContext().startActivity(call);
					
					// ..of het canvas leeg maken..
					clearCanvas();
					invalidate();
				}
			}
			
			lastEvent = currentEvent;
			lastTime = currentTime;
			
			return true;
		}
		
		private void clearCanvas()
		{
			System.out.println("-- Clear canvas");
			canvas.drawColor(Color.BLACK);
		}
		
		private void makeCircles()
		{
			// draw circle
            for (int n = 0; n < currentEvent.getHistorySize(); n++)
            {
            	circles.add(new Circle(currentEvent.getHistoricalX(n),
            			currentEvent.getHistoricalY(n),
            			80 * currentEvent.getHistoricalPressure(n)));
            }
		}
		
		private void drawCanvas()
		{
			// rotate background color
			hue_rotate = (hue_rotate + HUE_ROTATE_STEP) % 360;
			canvas.drawColor(Color.HSVToColor(new float[] {hue_rotate, COLOR_SATURATION, COLOR_VALUE}));
			
			for (Circle c : circles)
			{
				canvas.drawCircle(c.getX(), c.getY(), c.getRadius(), paint);
			}
		}
    }
}