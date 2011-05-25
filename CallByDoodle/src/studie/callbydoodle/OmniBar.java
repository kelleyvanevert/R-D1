package studie.callbydoodle;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewAnimator;

public class OmniBar extends ViewAnimator
{
	/**
	 * Contents
	 */
	private TextView txt, txt2;
	
	private GestureDetector swipeDetector;
	private static final int SWIPE_MIN_DISTANCE = 100;
	private static final int SWIPE_MIN_VELOCITY = 150;
	
	public OmniBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		txt = new TextView(context);
		txt.setText("Kelley van Evert");
		txt.setTextSize((float)24);
		addView(txt);
		txt2 = new TextView(context);
		txt2.setText("Tom Janmaat");
		txt2.setTextSize((float)24);
		addView(txt2);
		
		swipeDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

			  @Override

			  public boolean onSingleTapUp(MotionEvent ev) {

			    Log.d("onSingleTapUp",ev.toString());

			    return true;

			  }

			  @Override

			  public void onShowPress(MotionEvent ev) {

			    Log.d("onShowPress",ev.toString());

			  }

			  @Override

			  public void onLongPress(MotionEvent ev) {

			    Log.d("onLongPress",ev.toString());

			  }

			  @Override

			  public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

			    Log.d("onScroll",e1.toString());

			    return true;

			  }

			  @Override

			  public boolean onDown(MotionEvent ev) {

			    Log.d("onDownd",ev.toString());

			    return true;

			  }

			  @Override

			  public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

			    Log.d("d",e1.toString());

			    Log.d("e2",e2.toString());

			    return true;

			  }
			/*
			public boolean onFling(MotionEvent start, MotionEvent end, float vx, float vy) {
				int dx = (int)(end.getX() - start.getX());
				System.out.println("..onFling, dx[100] = "+dx+", vx[150] = "+vx);
				if (dx >= SWIPE_MIN_DISTANCE && vx >= SWIPE_MIN_VELOCITY) {
					System.out.println("YES!");
					return true;
				}
				System.out.println("no");
				return false;
			}*/
		});
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent m) {
		System.out.println("onmibar onTouchEvent");
		System.out.println("-- swipeDetector says: "+( swipeDetector.onTouchEvent(m) ? "YES" : "no"));
		
		setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.left_in));
		showNext();
		return false;
	}
}
