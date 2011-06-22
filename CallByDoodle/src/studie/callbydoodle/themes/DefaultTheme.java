package studie.callbydoodle.themes;

import studie.callbydoodle.data.Doodle;
import studie.callbydoodle.data.DoodleSegment;
import studie.callbydoodle.data.Vec;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class DefaultTheme implements DoodleTheme
{
	public static Paint paint;
	
	public static final int TOOLBAR_BACKGROUND_COLOR = Color.LTGRAY;
	public static final int TOOLBAR_TEXT_COLOR = Color.BLACK;
	public static final int BACKGROUND_COLOR = Color.WHITE;
	public static final int PAINT_COLOR = Color.BLACK;
	// MotionEvent.getPressure() times this constant to get the drawing radius
	public static final int PRESSURE_TO_RADIUS = 60;
	
	public DefaultTheme()
	{
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setStrokeWidth(10);
		paint.setColor(PAINT_COLOR);		
	}
	
	@Override
	public void drawDoodle(Canvas canvas, Doodle doodle)
	{
		canvas.drawColor(BACKGROUND_COLOR);
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
		}
	}

	@Override
	public int getToolbarBackgroundColor() {
		return TOOLBAR_BACKGROUND_COLOR;
	}

	@Override
	public int getToolbarTextColor() {
		return TOOLBAR_TEXT_COLOR;
	}
}
