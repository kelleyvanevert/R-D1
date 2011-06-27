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
	private static final int BAR_CELL_WIDTH = 4;
	private static final int DISTRIBUTION_VERTICAL_SPACING = 20;
	
	private int hue_rotate;
	private final int HUE_ROTATE_STEP = 30;
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
		
		int dx = canvas.getWidth() / 2;
		int dy = BAR_HEIGHT * Specs.NUM_DISTINCT_ANGLES + DISTRIBUTION_VERTICAL_SPACING;
		// offset by position     tot, TL, TR, BL,   BR
		int[] offsetX = new int[] {0,  0,  dx, 0,    dx};
		int[] offsetY = new int[] {0,  dy, dy, 2*dy, 2*dy};
		
		hue_rotate = 0;
		AngleDistribution[] distributions = new AngleDistribution[5];
		for (int i = 0; i < 5; i++) {
			distributions[i] = doodle.getSpecs().getPreparedAngleDistribution(i);
		}
		for (int angle = 0; angle < Specs.NUM_DISTINCT_ANGLES; angle++) {
			// Change color
			distributionPaint.setColor(Color.HSVToColor(new float[] {hue_rotate, HUE_COLOR_SATURATION, HUE_COLOR_VALUE}));
			distributionPaint.setAlpha(150);
			hue_rotate = (hue_rotate + HUE_ROTATE_STEP) % 360;
			// For each distribution, draw horizontal bar
			for (int i = 0; i < 5; i++) {
				canvas.drawRect(
						offsetX[i] + 0,
						offsetY[i] + angle * BAR_HEIGHT, // top
						offsetX[i] + distributions[i].count(angle) * BAR_CELL_WIDTH, // right
						offsetY[i] + (angle + 1) * BAR_HEIGHT, // bottom
						distributionPaint
					);
			}
		}
	}
}
