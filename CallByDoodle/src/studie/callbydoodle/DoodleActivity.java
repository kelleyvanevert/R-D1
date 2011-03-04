package studie.callbydoodle;

import java.security.Timestamp;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.gesture.*;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
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
    	private Paint paint;
    	private Bitmap bitmap;
    	private Canvas canvas;
    	private Path path;
    	private Paint bitmappaint;
    	
    	private int[] colors;
    	private int current_color;
    	
    	// Building up a gesture
    	//private ArrayList<Path>
    	private ArrayList<GesturePoint> gesturepoints;
    	private Gesture gesture;
    	
		public DoodleView(Context context)
		{
			super(context);
			setFocusable(true);
			path = new Path();
			
			paint = new Paint();
			paint.setColor(Color.RED);
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			paint.setStrokeWidth(16);
			
			colors = new int[] {
				Color.RED,
				Color.WHITE,
				Color.BLUE,
				Color.CYAN,
				Color.GREEN,
				Color.MAGENTA,
				Color.YELLOW,
				Color.LTGRAY,
			};
			current_color = 0;
			
			gesture = new Gesture();
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
			canvas.drawBitmap(bitmap, 0, 0, bitmappaint);
			canvas.drawPath(path, paint);
		}
		
		private static final float TOUCH_TOLERANCE = 1;
		private float lastx;
		private float lasty;
		
		@Override
		public boolean onTouchEvent(MotionEvent event)
		{
			float x = event.getX();
			float y = event.getY();
			
			if (event.getActionMasked() == MotionEvent.ACTION_DOWN)
			{
				// start new gesture stroke
				gesturepoints = new ArrayList<GesturePoint>();
				gesturepoints.add(new GesturePoint(x, y, System.currentTimeMillis()));
				
				// draw
				canvas.drawCircle(x, y, 8, paint);
				invalidate();
			}
			else if (event.getActionMasked() == MotionEvent.ACTION_MOVE)
			{
				// add gesture point
				gesturepoints.add(new GesturePoint(x, y, System.currentTimeMillis()));
				
				// draw
				canvas.drawLine(lastx, lasty, x, y, paint);
				canvas.drawCircle(x, y, 8, paint);
				invalidate();
			}
			else if (event.getActionMasked() == MotionEvent.ACTION_UP)
			{
				// make gesture stroke, add to gesture
				GestureStroke stroke = new GestureStroke(gesturepoints);
				gesture.addStroke(stroke);
				invalidate();
				
				// change color
				current_color = (current_color + 1) % colors.length;
				paint.setColor(colors[current_color]);
			}
			
			lastx = x;
			lasty = y;
			
			return true;
		}
    }
}