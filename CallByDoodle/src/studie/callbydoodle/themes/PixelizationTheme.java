package studie.callbydoodle.themes;

import studie.callbydoodle.data.Doodle;
import studie.callbydoodle.data.Pixelization;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class PixelizationTheme implements DoodleTheme
{
	private Paint paint;
	
	public PixelizationTheme()
	{
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);
	}
	
	@Override
	public void drawDoodle(Canvas canvas, Doodle doodle)
	{
		canvas.drawColor(Color.WHITE);
		Pixelization p = doodle.getSpecs().getPixelization();
		int pxWidth = canvas.getWidth() / p.size();
		int pxHeight = canvas.getHeight() / p.size();
		for (int y = 0; y < p.size(); y++) {
			for (int x = 0; x < p.size(); x++) {
				if (p.at(x, y) == 1) {
					canvas.drawRect(x * pxWidth, y * pxHeight, (x + 1) * pxWidth, (y + 1) * pxHeight, paint);
				}
			}
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
