package studie.callbydoodle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.gesture.*
;public class CallByDoodle extends Activity
{
	DoodleView ourView;
	
	
	GestureLibrary doodleLib;
	Gesture doodleGesture;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ourView = new DoodleView(this);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(ourView);
    }
    
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	
    	//menu.add(R.string.options_empty_canvas);
    	//menu.add(R.string.options_gesture_data); 
    	menu.add("Leeg Canvas");
    	menu.add("Converteer");
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	String itemName = item.getTitle().toString();
    	
    	//if(itemName.equals(R.string.options_empty_canvas))
    	if(itemName.equals("Leeg Canvas"))
    	{
    		ourView.clearCanvas();
    	}    	
    	
    	//if(itemName.equals(R.string.options_gesture_data))
    	if(itemName.equals("Converteer"))
    	{
    		Gesture ourGes = ourView.getGesture();
    		
    		ourView.setGesture(ourGes);
    	}
    	
    	
    	return true;
    }
    
}