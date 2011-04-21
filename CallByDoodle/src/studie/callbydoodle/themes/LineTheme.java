package studie.callbydoodle.themes;

import studie.callbydoodle.DoodleTheme;
import studie.callbydoodle.data.Doodle;
import studie.callbydoodle.data.DoodleSegment;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class LineTheme implements DoodleTheme
{
	private Paint paint;
	
	public LineTheme()
	{
		paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(3);
		paint.setColor(Color.BLACK);		
	}
	
	@Override
	public void drawDoodle(Canvas canvas, Doodle doodle)
	{
		canvas.drawColor(Color.WHITE);
		for (DoodleSegment segment : doodle.getSegments())
		{
			canvas.drawLine(segment.getVecStart().getX(), segment.getVecStart().getY(),
					segment.getVecEnd().getX(), segment.getVecEnd().getY(), paint);
		}
	}
}
