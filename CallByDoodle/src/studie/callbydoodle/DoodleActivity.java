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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import studie.callbydoodle.data.Doodle;
import studie.callbydoodle.data.DoodleLibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.gesture.GestureStore;
import android.gesture.Prediction;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.Contacts.People;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewAnimator;

public class DoodleActivity extends Activity
{
	/**
	 * Views to handle
	 */
	DoodleView ourView;
	OmniBar omnibar;
	
	private final String DOODLE_STORE_FILE = "store.doodlelib";
	private DoodleLibrary library;
	
	/**
	 * When the user wants to save a doodle, we'll be using
	 *  these attributes..
	 */
	private static final int PICK_CONTACT_FOR_DOODLE_SAVE_REQUEST = 1;
	private Doodle saveDoodle = null;
	
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
		
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        ourView = (DoodleView)findViewById(R.id.doodleview);
        omnibar = (OmniBar)findViewById(R.id.omnibar);
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
    	menu.add("Dump to SD");
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
    			/*
    			AlertDialog.Builder alert = new AlertDialog.Builder(this);
    			alert.setTitle("Name?");
    			alert.setMessage("Which name?");
    			final EditText input = new EditText(this);
    			alert.setView(input);
    			alert.setPositiveButton("Allright!", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						System.out.println("Allright! "+input.getText());
		    			// Choose contact, then save to library
		    			library.add(input.getText().toString(), ourView.getDoodle());
					}
				});
    			alert.show();
    			*/
    			saveDoodle = ourView.getDoodle();
    			startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI),
    					PICK_CONTACT_FOR_DOODLE_SAVE_REQUEST);
       		}
    	}

    	if(itemName.equals("Dump to SD"))
    	{
    		String ser = library.serialize();
    		System.out.println("Dumping library serialization..");
    		System.out.print(ser);
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				String d = Environment.getExternalStorageDirectory().getAbsolutePath();
				File exportDir = new File(d + File.separator + "CallByDoodle");
				if (!exportDir.exists())
					exportDir.mkdir();
				final Format formatter = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
				File exportFile = new File(exportDir + File.separator + "export-"+formatter.format(new Date())+".doodlelib");
				System.out.println("Trying to write to file [ "+exportFile.toURI()+" ]..");
				try {
					if (!exportFile.exists())
						exportFile.createNewFile();
					FileOutputStream f = new FileOutputStream(exportFile);
					f.write(library.serialize().getBytes());
					f.close();
					System.out.println("Success!");
				} catch (Exception e) {
					System.out.println("Could not save doodle: file write error!");
					e.printStackTrace();
				}
			} else {
				System.out.println("Could not save doodle: storage mount error!");
			}
    	}
    	
    	return true;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	if (requestCode == PICK_CONTACT_FOR_DOODLE_SAVE_REQUEST && saveDoodle != null) {
        	String lookupKey = null,
        	       displayName = null;
    		if (resultCode == Activity.RESULT_OK) {
    			Uri contactData = data.getData();
    			Cursor c = getContentResolver().query(contactData, new String[]{ContactsContract.Contacts.LOOKUP_KEY, ContactsContract.Contacts.DISPLAY_NAME}, null, null, null);
				if (c.moveToFirst()) {
					lookupKey = c.getString(0);
					displayName = c.getString(1);
				}
				c.close();
    		}
    		if (lookupKey != null && displayName != null) {
    			library.add(lookupKey + ", " + displayName, saveDoodle);
    		}
    	}
    }
}