/*=============================================================================
 * CallByDoodle
 * 
 * CallByDoodle is an Android project created in the Spring of 2011
 * for Research & Development 1, a course the authors follow at
 * Radboud University, Nijmegen.
 * 
 * Authors:
 * Roelf Leenders
 * Kelley van Evert
 * Jochem Kooistra
 * 
 * Supervisors:
 * Erik Barendsen
 * Sjaak Smetsers
 * 
 * Project started in February of 2011, scheduled for completion 
 * in the summer of 2011.
 ============================================================================*/


package studie.callbydoodle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.gesture.*;

public class CallByDoodle extends Activity
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
    
    
    public void onResume()
    {
    	super.onResume();
    	//ourView.loadThemeSetting();
    }
    
    
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	
    	//menu.add(R.string.options_empty_canvas);
    	//menu.add(R.string.options_gesture_data); 
    	menu.add("Leeg Canvas");
    	menu.add("Converteer");
    	menu.add("Settings");
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	String itemName = item.getTitle().toString();
    	
    	//if(itemName.equals(R.string.options_empty_canvas))
    	if(itemName.equals("Leeg Canvas"))
    	{
    		ourView.startNewRecording();
    	}    	
    	
    	//if(itemName.equals(R.string.options_gesture_data))
    	if(itemName.equals("Converteer"))
    	{
    		//Gesture ourGes = ourView.getGesture();
    		
    		//ourView.setGesture(ourGes);
    	}
    	
    	if(itemName.equals("Settings"))
    	{
    		Intent settingsIntent = new Intent();
    		settingsIntent.setClassName("studie.callbydoodle","studie.callbydoodle.SettingsActivity");
    		
    		CallByDoodle.this.startActivity(settingsIntent);
    	}
    	
    	return true;
    }
    
}