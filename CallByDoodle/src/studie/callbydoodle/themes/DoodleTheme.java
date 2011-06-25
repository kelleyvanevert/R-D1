package studie.callbydoodle.themes;

import android.graphics.Canvas;
import studie.callbydoodle.data.Doodle;

public interface DoodleTheme
{
	void drawDoodle(Canvas canvas, Doodle doodle);
	int getToolbarBackgroundColor();
	int getToolbarTextColor();
}