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

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class DoodleActivity extends Activity
{
	DoodleView ourView;
	
	private DoodleLibrary library;
	
	// Temporary
	private int i = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // Create a new doodle library
        // For now, I'm just read/writing from the SD card.
        // Future: internal storage, and import/export options for SD card storage.
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String d = Environment.getExternalStorageDirectory().getAbsolutePath();
			File doodlePath = new File(d + File.separator + "Doodles");
			if (!doodlePath.exists()) doodlePath.mkdir();
			library = new DoodleLibrary(doodlePath);
		} else {
			System.out.println("Could not read doodle/contact info from SD card.");
			library = new DoodleLibrary();
		}
        
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
    	menu.add("Save Contact");
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
    	if(itemName.equals("Save Contact"))
    	{
    		if (ourView.hasCompletedDoodle()) {
    			// Choose contact, then save to library
    			library.add("heart-" + (i++), ourView.getDoodle());
    			
    			/*
    			String state = Environment.getExternalStorageState();
    			if (Environment.MEDIA_MOUNTED.equals(state)) {
    				String d = Environment.getExternalStorageDirectory().getAbsolutePath();
    				File doodlePath = new File(d + File.separator + "Doodles");
    				if (!doodlePath.exists()) doodlePath.mkdir();
    				File doodleFile = new File(doodlePath + File.separator + "doodle");
    				try {
    					FileOutputStream f = new FileOutputStream(doodleFile);
    					f.write(ourView.getDoodle().serialize().getBytes());
    				} catch (Exception e) {
    					System.out.println("Could not save doodle: file write error!");
    				}
    			} else {
    				System.out.println("Could not save doodle: storage mount error!");
    			}*/
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