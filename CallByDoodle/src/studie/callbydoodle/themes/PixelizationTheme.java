package studie.callbydoodle.themes;

import studie.callbydoodle.data.Doodle;
import studie.callbydoodle.data.Pixelization;
import studie.callbydoodle.data.Specs;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

public class PixelizationTheme extends DefaultTheme
{	
	public PixelizationTheme()
	{
		super();
	}
	
	@Override
	public void drawDoodle(Canvas canvas, Doodle doodle)
	{
		super.drawDoodle(canvas, doodle);
		
		/*
		Pixelization p = doodle.getSpecs().getPixelization();
		
		Bitmap greyScale = Bitmap.createBitmap(Specs.PIXELIZATION_SIZE, Specs.PIXELIZATION_SIZE, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(greyScale);
		Paint paint = new Paint();
		ColorMatrix matrix = new ColorMatrix();
		matrix.setSaturation(0);
		ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
		paint.setColorFilter(filter);
		c.drawColor(Color.LTGRAY);
		c.drawBitmap(p.getBitmap(), 0, 0, paint);
		
		canvas.drawBitmap(greyScale, 0, 0, null);
		*/
		
		/*
		canvas.drawRect(0, 0, p.size() * PIXEL_SIZE, p.size() * PIXEL_SIZE, pixelBackgroundPaint);
		for (int y = 0; y < p.size(); y++) {
			for (int x = 0; x < p.size(); x++) {
				if (p.at(x, y) == 1) {
					canvas.drawRect(
							x * PIXEL_SIZE, // left
							y * PIXEL_SIZE, // top
							(x + 1) * PIXEL_SIZE, // right
							(y + 1) * PIXEL_SIZE, // bottom
							pixelPaint
						);
				}
			}
		}
		*/
	}
}
