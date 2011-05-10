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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import studie.callbydoodle.data.Doodle;
import studie.callbydoodle.data.DoodleLibrary;

import android.app.Activity;
import android.content.Context;
import android.gesture.GestureStore;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class DoodleActivity extends Activity
{
	DoodleView ourView;
	
	private final String DOODLE_STORE_FILE = "store.doodlelib";
	private DoodleLibrary library;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // Try to find existing doodle library storage file
        FileInputStream fis = null;
        try {
			fis = openFileInput(DOODLE_STORE_FILE);
			StringBuffer buf = new StringBuffer();
			int ch;
	        while ((ch = fis.read()) != -1)
				buf.append((char)ch);
	        fis.close();
	        library = DoodleLibrary.parseDoodleLibrary(buf.toString());
		} catch (Exception e) {
			// Either not able to read from file, or not able to reconstruct doodle library.
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
    
    public void onDestroy()
    {
    	super.onDestroy();
    	
    	// Write back doodle library
    	FileOutputStream fos;
    	try {
    		fos = openFileOutput(DOODLE_STORE_FILE, Context.MODE_PRIVATE);
			fos.write(library.serialize().getBytes());
	    	fos.close();
		} catch (IOException e) {
		}
    }
    
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	//menu.add(R.string.options_empty_canvas);
    	//menu.add(R.string.options_gesture_data); 
    	menu.add("Compare to Prev");
    	menu.add("Save Contact");
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	String itemName = item.getTitle().toString();
    	
    	//if(itemName.equals(R.string.options_empty_canvas))
    	if(itemName.equals("Compare to Prev"))
    	{
    		if (ourView.hasCompletedDoodle()) {
	    		Doodle current = ourView.getDoodle();
	    		GestureStore store = library.getGestureStore();
	    		ArrayList<Prediction> predictions = store.recognize(current.getGesture());
	    		for (Prediction p : predictions) {
	    			System.out.println("Prediction: "+p.name+"@"+p.score);
	    		}
	    		if (predictions.size() > 0 && predictions.get(0).score > 1) {
	    			Prediction pred = predictions.get(0);
	    			Toast.makeText(this, "Best: "+pred.name+"@"+pred.score, Toast.LENGTH_SHORT);
	    			System.out.println("Best: "+pred.name+"@"+pred.score);
	    			ourView.setDoodle(library.get(pred.name));
	    		} else {
	    			Toast.makeText(this, "NONE", Toast.LENGTH_SHORT);
	    			System.out.println("NONE");
	    		}
	    		
	    		/*
	    		Intent settingsIntent = new Intent();
	    		settingsIntent.setClassName("studie.callbydoodle","studie.callbydoodle.TestRecognitionActivity");
	    		DoodleActivity.this.startActivity(settingsIntent);
	    		*/
    		}
    	}
    	
    	//if(itemName.equals(R.string.options_gesture_data))
    	if(itemName.equals("Save Contact"))
    	{
    		if (ourView.hasCompletedDoodle()) {
    			// Choose contact, then save to library
    			library.add("heart-" + library.getDoodles().size(), ourView.getDoodle());
    			
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
    	
    	return true;
    }
    
}