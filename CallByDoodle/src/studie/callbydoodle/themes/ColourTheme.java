package studie.callbydoodle.themes;

import studie.callbydoodle.data.Doodle;
import studie.callbydoodle.data.DoodleSegment;
import studie.callbydoodle.data.Vec;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class ColourTheme implements DoodleTheme
{
	private Paint paint;
	
	// Using colour rotation for background
	// Color rotation: current HUE in HSV
	// HUE <= [0 .. 360)
	private int hue_rotate;
	private final int HUE_ROTATE_STEP = 3;
	private final float HUE_COLOR_SATURATION = 1;
	private final float HUE_COLOR_VALUE = (float)0.7;
	
	// MotionEvent.getPressure() times this constant to get the drawing radius
	private final float PRESSURE_TO_RADIUS = (float)0.6;
	
	public ColourTheme()
	{
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setStrokeWidth(10);
		paint.setColor(Color.BLACK);
		
		hue_rotate = 0;
	}
	
	@Override
	public void drawDoodle(Canvas canvas, Doodle doodle)
	{
		hue_rotate = (hue_rotate + HUE_ROTATE_STEP) % 360;
		canvas.drawColor(Color.HSVToColor(new float[] {hue_rotate, HUE_COLOR_SATURATION, HUE_COLOR_VALUE}));
		for (DoodleSegment segment : doodle.getSegments())
		{
			Path p = new Path();
			
			Vec move = segment.getVecEnd().subtract(segment.getVecStart());
			Vec radiusVecEnd = move.setLength(PRESSURE_TO_RADIUS * segment.getPressureEnd());
			Vec radiusVecStart = move.setLength(PRESSURE_TO_RADIUS * segment.getPressureStart());
			Vec vecEndLeft = segment.getVecEnd().add(radiusVecEnd.rotateLeft());
			Vec vecEndRight = segment.getVecEnd().add(radiusVecEnd.rotateRight());
			Vec vecStartLeft = segment.getVecStart().add(radiusVecStart.rotateLeft());
			Vec vecStartRight = segment.getVecStart().add(radiusVecStart.rotateRight());
			
			p.moveTo(vecEndLeft.getX(), vecEndLeft.getY());
			p.lineTo(vecEndRight.getX(), vecEndRight.getY());
			p.lineTo(vecStartRight.getX(), vecStartRight.getY());
			p.lineTo(vecStartLeft.getX(), vecStartLeft.getY());
			p.lineTo(vecEndLeft.getX(), vecEndLeft.getY());
			
			canvas.drawPath(p, paint);
			/*
			canvas.drawLine(segment.getVecStart().getX(), segment.getVecStart().getY(),
					segment.getVecEnd().getX(), segment.getVecEnd().getY(), paint);
					*/
		}
	}

	@Override
	public int getToolbarBackgroundColor() {
		return Color.BLACK;
	}

	@Override
	public int getToolbarTextColor() {
		return Color.WHITE;
	}
}
