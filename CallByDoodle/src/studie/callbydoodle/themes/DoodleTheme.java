package studie.callbydoodle.themes;

import studie.callbydoodle.data.Doodle;
import android.graphics.Canvas;

public interface DoodleTheme
{
	void drawDoodle(Canvas canvas, Doodle doodle);
	int getToolbarBackgroundColor();
	int getToolbarTextColor();
}