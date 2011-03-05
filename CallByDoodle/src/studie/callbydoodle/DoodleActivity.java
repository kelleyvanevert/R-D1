package studie.callbydoodle;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.gesture.*;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
    	
    	// Building up a gesture
    	//private ArrayList<Path>
    	private ArrayList<GesturePoint> gesturepoints;
    	private Gesture gesture;
    	
		public DoodleView(Context context)
		{
			super(context);
			setFocusable(true);
			
			hue_rotate = 0;
			
			paint = new Paint();
			paint.setStyle(Paint.Style.FILL);
			paint.setStrokeWidth(10);
			rotateColor();
			
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
			canvas.drawBitmap(bitmap, 0, 0, null);
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event)
		{
			float x = event.getX();
			float y = event.getY();
			
			if (event.getActionMasked() == MotionEvent.ACTION_DOWN)
			{
				// start new gesture stroke
				//gesturepoints = new ArrayList<GesturePoint>();
				//gesturepoints.add(new GesturePoint(x, y, System.currentTimeMillis()));
				
	            for (int n = 0; n < event.getHistorySize(); n++)
	            {
	            	drawCircle(event.getHistoricalX(n), event.getHistoricalY(n),
	            			   event.getHistoricalPressure(n));
	            }
				invalidate();
			}
			else if (event.getActionMasked() == MotionEvent.ACTION_MOVE)
			{
				// add gesture point
				//gesturepoints.add(new GesturePoint(x, y, System.currentTimeMillis()));
				
				// draw
				drawCircle(x, y, event.getPressure());
				invalidate();
			}
			else if (event.getActionMasked() == MotionEvent.ACTION_UP)
			{
				// make gesture stroke, add to gesture
				//GestureStroke stroke = new GestureStroke(gesturepoints);
				//gesture.addStroke(stroke);

				drawCircle(x, y, event.getPressure());
				invalidate();
			}
			
			return true;
		}
		
		private void drawCircle(float x, float y, float pressure)
		{
			// draw circle
			canvas.drawCircle(x, y, pressure*80, paint);
			
			rotateColor();
		}
		
		private void rotateColor()
		{
			hue_rotate = (hue_rotate + HUE_ROTATE_STEP) % 360;
			paint.setColor(Color.HSVToColor(new float[] {hue_rotate, COLOR_SATURATION, COLOR_VALUE}));
		}
    }
}