package studie.callbydoodle.themes;

import java.util.ArrayList;

import studie.callbydoodle.data.Doodle;
import studie.callbydoodle.data.Seg;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class AngleTheme implements DoodleTheme
{
	private Paint paint;
	private double radius = 10;
	private float strokeWidth = 2;
	private int paintColor = Color.BLACK;
	
	public AngleTheme()
	{
		paint = new Paint();
		paint.setColor(paintColor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(strokeWidth);
	}
	
	@Override
	public void drawDoodle(Canvas canvas, Doodle doodle)
	{
		ArrayList<Seg> segs = doodle.getSpecs().getSegs();
		canvas.drawColor(Color.WHITE);
		for (Seg seg : segs) {
			double angle = seg.RealAngle();
			int x = seg.Start().getX();
			int y = seg.Start().getY();
			canvas.drawLine(
					(float)(x - radius * Math.cos(angle)),
					(float)(y - radius * Math.sin(angle)),
					(float)(x + radius * Math.cos(angle)),
					(float)(y + radius * Math.sin(angle)),
					paint
				);
		}
	}
	
	@Override
	public int getToolbarBackgroundColor()
	{
		return Color.WHITE;
	}
	
	@Override
	public int getToolbarTextColor()
	{
		return Color.BLACK;
	}
}
