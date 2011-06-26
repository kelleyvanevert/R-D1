package studie.callbydoodle.data;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Pixelization
{
	private Bitmap bitmap;
	private Canvas canvas;
	private static Paint paint;
	
	public Pixelization()
	{
		bitmap = Bitmap.createBitmap(Specs.PIXELIZATION_SIZE, Specs.PIXELIZATION_SIZE,
			Bitmap.Config.ALPHA_8);
		canvas = new Canvas(bitmap);
		canvas.drawColor(Color.TRANSPARENT);
		if (paint == null) {
			paint = new Paint();
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.BLACK);
			paint.setStrokeWidth(Specs.PIXELIZATION_STROKE_WIDTH);
			paint.setAntiAlias(false);
		}
		
		/*
		Rect r = doodle.getRect();
		Vec offset;
		double scale = ((double)Specs.PIXELIZATION_SIZE) / (double)Math.max(r.Width(), r.Height());
		if (r.Height() > r.Width()) {
			offset = new Vec(scale * Math.floor((r.Height() - r.Width()) / 2), 0);
		} else {
			offset = new Vec(0, scale * Math.floor((r.Width() - r.Height()) / 2));
		}
		
		for (DoodleSegment segment : doodle.getSegments()) {
			draw(
					offset.add(new Vec(
							scale * segment.getVecStart().getDoubleX(),
							scale * segment.getVecStart().getDoubleY()
							)),
					offset.add(new Vec(
							scale * segment.getVecEnd().getDoubleX(),
							scale * segment.getVecEnd().getDoubleY()
							))
					);
		}
		*/
	}
	
	private void draw(Vec a, Vec b)
	{
		canvas.drawLine(a.getX(), a.getY(), b.getX(), b.getY(), paint);
	}
	
	public static int similarity(Pixelization a, Pixelization b)
	{
		int total = 0, overlap = 0;
		
		for (int y = 0; y < Specs.PIXELIZATION_SIZE; y++) {
			for (int x = 0; x < Specs.PIXELIZATION_SIZE; x++) {
				if (a.bitmap.getPixel(x, y) != Color.TRANSPARENT && b.bitmap.getPixel(x, y) != Color.TRANSPARENT) {
					overlap++;
					total++;
				} else if (a.bitmap.getPixel(x, y) != Color.TRANSPARENT || b.bitmap.getPixel(x, y) != Color.TRANSPARENT) {
					total++;
				}
			}
		}
		
		return (int)((overlap * 100.0) / total);
	}
	
	public Bitmap getBitmap()
	{
		return bitmap;
	}
}
