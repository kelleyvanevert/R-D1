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
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class DoodleActivity extends Activity
{
	DoodleView ourView;
	
	private DoodleLibrary library;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // Create a new doodle library
        // In the future, we will get our doodle library from some storage,
        //  then save it again when we make new doodles or if we choose
        //  to record doodle variations...
        library = new DoodleLibrary();
        
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
    	menu.add("Save");
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
    	if(itemName.equals("Save"))
    	{
    		if (ourView.hasCompletedDoodle()) {
    			// Choose contact, then save to library
    			System.out.println(ourView.getDoodle().serialize());
    		}
    	}
    	
    	if(itemName.equals("Settings"))
    	{
    		Intent settingsIntent = new Intent();
    		settingsIntent.setClassName("studie.callbydoodle","studie.callbydoodle.SettingsActivity");
    		
    		DoodleActivity.this.startActivity(settingsIntent);
    	}
    	
    	return true;
    }
    
}