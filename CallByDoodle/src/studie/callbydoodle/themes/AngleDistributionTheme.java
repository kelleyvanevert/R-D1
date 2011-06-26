package studie.callbydoodle.themes;

import studie.callbydoodle.data.AngleDistribution;
import studie.callbydoodle.data.Doodle;
import studie.callbydoodle.data.Specs;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class AngleDistributionTheme extends DefaultTheme
{
	private Paint distributionPaint;
	
	private static final int BAR_HEIGHT = 8;
	private static final int BAR_CELL_WIDTH = 5;
	
	private int hue_rotate;
	private final int HUE_ROTATE_STEP = 27;
	private final float HUE_COLOR_SATURATION = 1;
	private final float HUE_COLOR_VALUE = (float)0.7;
	
	public AngleDistributionTheme()
	{
		super();
		
		distributionPaint = new Paint();
		distributionPaint.setStyle(Paint.Style.FILL);
	}
	
	@Override
	public void drawDoodle(Canvas canvas, Doodle doodle)
	{
		super.drawDoodle(canvas, doodle);
		
		hue_rotate = 0;
		AngleDistribution distribution = doodle.getSpecs().getAngleDistribution();
		for (int angle = 0; angle < Specs.NUM_DISTINCT_ANGLES; angle++) {
			distributionPaint.setColor(Color.HSVToColor(new float[] {hue_rotate, HUE_COLOR_SATURATION, HUE_COLOR_VALUE}));
			hue_rotate = (hue_rotate + HUE_ROTATE_STEP) % 360;
			canvas.drawRect(
					0,
					angle * BAR_HEIGHT, // top
					distribution.count(angle) * BAR_CELL_WIDTH, // right
					(angle + 1) * BAR_HEIGHT, // bottom
					distributionPaint
				);
		}
	}
}
