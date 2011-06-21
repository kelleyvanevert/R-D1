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
import studie.callbydoodle.DoodleView.DoodleViewListener;
import studie.callbydoodle.data.Doodle;
import studie.callbydoodle.data.DoodleLibrary;
import studie.callbydoodle.themes.DoodleTheme;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DoodleActivity extends Activity
	implements DoodleViewListener, SharedPreferences.OnSharedPreferenceChangeListener
{
	/**
	 * Views to handle
	 */
	DoodleView doodleView;
	TextView toolbarText;
	Button callButton;
	LinearLayout background;
	
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        // Load the library asynchronously
        // This means the UI will not be blocked, though of course
        // one will have to wait for completion of library loading
        // before any recognition can take place...
        new DoodleLibraryLoader().execute();
        
        // Listen for any preference changes
        // (Theme change..)
        prefs.registerOnSharedPreferenceChangeListener(this);
        
        // Put the content, retrieve components,
        // start listening for events and load theme
        setContentView(R.layout.main);
        doodleView = (DoodleView)findViewById(R.id.doodleview);
        doodleView.setDoodleViewListener(this);
        toolbarText = (TextView)findViewById(R.id.toolbartext);
        callButton = (Button)findViewById(R.id.btn_call);
        background = (LinearLayout)findViewById(R.id.background);
        reloadTheme();
        
        // Initial state
        state = STATE_WAITING;
        toolbarText.setText(getString(R.string.toolbar_state_waiting));
        
        // Check whether this is the first time this app is being used.
        // If so, give a brief description!
        boolean firstTime = prefs.getBoolean("first_time", true);
        if (firstTime)
        {
        	prefs.edit().putBoolean("first_time", true).commit();
        	// TODO
        	// startActivity(new Intent(this, IntroductionActivity.class));
        }
    }

	/*
	 * Asynchronous library loading!
	 */
	private final String DOODLE_STORE_FILE = "store.doodlelib";
	private DoodleLibrary library;
	
    private class DoodleLibraryLoader extends AsyncTask<Void, Void, DoodleLibrary>
    {
    	@Override
		protected DoodleLibrary doInBackground(Void... params)
		{
			// Try to find existing doodle library storage file
	        FileInputStream fis = null;
	        try {
				fis = openFileInput(DOODLE_STORE_FILE);
				StringBuffer buf = new StringBuffer();
				int ch;
		        while ((ch = fis.read()) != -1)
					buf.append((char)ch);
		        fis.close();
		        return DoodleLibrary.parseDoodleLibrary(buf.toString());
			} catch (Exception e) {
				// Either not able to read from file, or not able to reconstruct doodle library.
				return new DoodleLibrary();
			}
		}
		
		@Override
		protected void onPostExecute(DoodleLibrary result)
		{
			library = result;
			System.out.println("Library LOADED");
			
			// See comment @ declaration of libraryLoadPendingDoodleRecognition
			if (libraryLoadPendingDoodleRecognition != null) {
				System.out.println("..continue recognition..");
				if (currentRecognizer != null && !currentRecognizer.isCancelled()) {
					currentRecognizer.cancel(true);
				}
				currentRecognizer = new Recognizer().execute(libraryLoadPendingDoodleRecognition);
				libraryLoadPendingDoodleRecognition = null;
			}
		}
    }
    
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.doodle_activity_menu, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch (item.getItemId())
    	{
    	case R.id.save_contact:
    		tryPerformSaveContact();
    		return true;
    	case R.id.goto_settings:
    		startActivity(new Intent(this, SettingsActivity.class));
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    private void tryPerformSaveContact()
    {
		if (doodleView.hasCompletedDoodle()) {
			startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI),
					PICK_CONTACT_FOR_DOODLE_SAVE_REQUEST);
   		}
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
    
    private static final int STATE_WAITING = 0;
    private static final int STATE_DRAWING = 1;
    private static final int STATE_THINKING = 2;
    private static final int STATE_DONE = 3;
    private int state = STATE_WAITING;
    
    // Workaround for a problem I don't really have an elegant solution for:
    // Currently, I'm both loading the library (at the initial application start)
    // and doing recognitions asynchronously, using AsyncTasks.
    // How do I accomplish having the library load complete before doing any
    // recognitions? I don't really know, and I do like the simple use of AsyncTasks,
    // so I chose for the following hack:
    // Seen as only one recognition will ever happen (or be requested) at the same time,
    // and there is only one simple event involved ("library loaded!"), I just
    // hard-code the .execute() for that one pending recognition in the
    // .onPostExecute() of the library loading.
    private Doodle libraryLoadPendingDoodleRecognition = null;
    
    private AsyncTask<Doodle, Void, String> currentRecognizer;
	private class Recognizer extends AsyncTask<Doodle, Void, String>
	{
		@Override
		protected String doInBackground(Doodle... params)
		{
			// Try to recognize the doodle.
			// !! Don't forget to periodically check for isCancelled(), for efficiency..
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				return "Interrupt";
			}
			return "Kelley";
		}
		
		protected void onPostExecute(String result)
		{
			if (state == STATE_THINKING)
			{
				//callButton.setVisibility(View.VISIBLE);
				toolbarText.setText("Maybe " + result + "?");
				state = STATE_DONE;
			}
		};
	};
	
	@Override
	public void onClearView() {
		callButton.setVisibility(View.GONE);
		if (currentRecognizer != null && !currentRecognizer.isCancelled()) {
			currentRecognizer.cancel(true);
		}
		libraryLoadPendingDoodleRecognition = null;
		state = STATE_WAITING;
		toolbarText.setText("Draw to start...");
	}
	
	@Override
	public void onCompleteDoodle() {
		if (doodleView.hasCompletedDoodle()) {
			state = STATE_THINKING;
			if (currentRecognizer != null && !currentRecognizer.isCancelled()) {
				currentRecognizer.cancel(true);
			}
			if (library == null) {
				System.out.println("! Library not yet loaded! recognition must wait");
				libraryLoadPendingDoodleRecognition = doodleView.getDoodle();
			} else {
				System.out.println("Recognise!");
				currentRecognizer = new Recognizer().execute(doodleView.getDoodle());
			}
		}
	}
	
	@Override
	public void onStartDrawDoodle() {
		callButton.setVisibility(View.GONE);
		if (currentRecognizer != null && !currentRecognizer.isCancelled()) {
			currentRecognizer.cancel(true);
		}
		libraryLoadPendingDoodleRecognition = null;
		state = STATE_DRAWING;
		toolbarText.setText("Guessing...");
	}
	
	/**
	 * (Re)loads the current theme, as in the default shared preferences
	 */
	@SuppressWarnings("unchecked")
	public void reloadTheme()
	{
        try {
        	SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        	Class<DoodleTheme> t = (Class<DoodleTheme>) Class.forName("studie.callbydoodle.themes." + p.getString("theme", "DefaultTheme"));
        	DoodleTheme theme = t.newInstance();
        	// Finally!
        	doodleView.setTheme(theme);
        	background.setBackgroundColor(theme.getToolbarBackgroundColor());
        	toolbarText.setTextColor(theme.getToolbarTextColor());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Responds to changes in the default shared preferences
	 */
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals("theme")) {
			reloadTheme();
		}
	}
}