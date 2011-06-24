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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import studie.callbydoodle.DoodleView.DoodleViewListener;
import studie.callbydoodle.data.Doodle;
import studie.callbydoodle.data.DoodleLibrary;
import studie.callbydoodle.data.DoodleLibraryEntry;
import studie.callbydoodle.themes.DoodleTheme;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

public class DoodleActivity extends Activity
	implements DoodleViewListener,
	SharedPreferences.OnSharedPreferenceChangeListener,
	View.OnClickListener
{
	/**
	 * Views to handle
	 */
	DoodleView doodleView;
	Button btnLeft, btnRight, btnResult;
	RelativeLayout toolbarRelativeLayout;
	
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
        btnLeft = (Button)findViewById(R.id.btn_left);
        btnLeft.setFocusable(false);
        btnLeft.setOnClickListener(this);
        btnRight = (Button)findViewById(R.id.btn_right);
        btnRight.setFocusable(false);
        btnRight.setOnClickListener(this);
        btnResult = (Button)findViewById(R.id.toolbartext);
        btnResult.setFocusable(false);
        btnResult.setOnClickListener(this);
        toolbarRelativeLayout = (RelativeLayout)findViewById(R.id.toolbar);
        reloadTheme();
        
        // Initial state transition
        doStateTransition(TRANSITION_INIT);
        
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
	        ObjectInputStream is = null;
	        try {
				is = new ObjectInputStream(openFileInput(DOODLE_STORE_FILE));
				DoodleLibrary library = (DoodleLibrary)is.readObject();
				is.close();
				return library;
			} catch (Exception e) {
				// Either not able to read from file, or not able to reconstruct doodle library.
				return new DoodleLibrary();
			}
		}
		
		@Override
		protected void onPostExecute(DoodleLibrary result)
		{
			library = result;
			doStateTransition(TRANSITION_LIBRARY_LOADED);
		}
    }
    
    private class LibrarySaver extends AsyncTask<DoodleLibrary, Void, Void>
    {
		@Override
		protected Void doInBackground(DoodleLibrary... libraries)
		{
			if (libraries.length > 0)
			{
		    	ObjectOutputStream oos;
		    	try {
		    		oos = new ObjectOutputStream(openFileOutput(DOODLE_STORE_FILE, Context.MODE_PRIVATE));
		    		oos.writeObject(library);
			    	oos.close();
				} catch (Exception e) {}
			}
			
			return null;
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
    	case R.id.menu_delete_doodle:
    		if (state == STATE_BROWSING) {
    			library.remove(browsePosition);
    			setBrowsingPosition(browsePosition - 1);
    		}
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    private void tryPerformSaveContact()
    {
		if (doodleView.hasCompletedDoodle()) {
			saveDoodle = doodleView.getDoodle();
			startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI),
					PICK_CONTACT_FOR_DOODLE_SAVE_REQUEST);
   		}
    }
    
    private String getContactDisplayName(String lookupKey)
    {
    	Uri lookupUri = Uri.withAppendedPath(Contacts.CONTENT_LOOKUP_URI, lookupKey);
    	Cursor c = getContentResolver().query(lookupUri, new String[]{Contacts.DISPLAY_NAME}, null, null, null);
    	if (c.moveToFirst()) {
    		return c.getString(0);
    	} else {
    		return null;
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
    			library.add(new DoodleLibraryEntry(lookupKey, saveDoodle));
    			doStateTransition(TRANSITION_NEW_CONTACT_ADDED, lookupKey);
    			new LibrarySaver().execute(library);
    		}
    	}
    }
    
    private static final int
		TRANSITION_INIT = 0,
		TRANSITION_CLEAR = 1,
		TRANSITION_START_DRAW = 2,
		TRANSITION_END_DRAW = 3,
		TRANSITION_RECOGNITION_RESULT = 4,
		TRANSITION_NEW_CONTACT_ADDED = 5,
		TRANSITION_LIBRARY_LOADED = 6,
		TRANSITION_BROWSE_BUTTON_LEFT = 7,
		TRANSITION_BROWSE_BUTTON_RIGHT = 8;
    private static final int
    	STATE_WAITING = 0,
    	STATE_DRAWING = 1,
    	STATE_THINKING = 2,
    	STATE_DONE = 3,
    	STATE_BROWSING = 4;
    private int state = STATE_WAITING;
    // When browsing, the 
    private int browsePosition = -1;
    
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
			doStateTransition(TRANSITION_RECOGNITION_RESULT, result);
		};
	};
	
	@Override
	public void onClearView() {
		doStateTransition(TRANSITION_CLEAR);
	}
	
	@Override
	public void onCompleteDoodle() {
		doStateTransition(TRANSITION_END_DRAW);
	}
	
	@Override
	public void onStartDrawDoodle() {
		doStateTransition(TRANSITION_START_DRAW);
	}
	
	@Override
	public void onClick(View v)
	{
		if (v == btnLeft) {
			doStateTransition(TRANSITION_BROWSE_BUTTON_LEFT);
		} else if (v == btnRight) {
			doStateTransition(TRANSITION_BROWSE_BUTTON_RIGHT);
		} else if (v == btnResult) {
			// TODO show popup menu: [call, text]
		}
	}
	
	private void doStateTransition(int transition)
	{
		doStateTransition(transition, null);
	}
	
	private void doStateTransition(int transition, Object arg)
	{
		boolean libraryEmpty = (library == null) || (library.size() == 0);
		
		if (transition == TRANSITION_INIT || transition == TRANSITION_CLEAR) {
			if (currentRecognizer != null && !currentRecognizer.isCancelled()) {
				currentRecognizer.cancel(true);
			}
			libraryLoadPendingDoodleRecognition = null;
			state = STATE_WAITING;
			btnLeft.setEnabled(false);
			btnResult.setEnabled(false);
			btnRight.setEnabled(!libraryEmpty);
			btnResult.setText(getString(R.string.toolbar_state_waiting));
		} else if (transition == TRANSITION_START_DRAW) {
			if (currentRecognizer != null && !currentRecognizer.isCancelled()) {
				currentRecognizer.cancel(true);
			}
			libraryLoadPendingDoodleRecognition = null;
			state = STATE_DRAWING;
			btnLeft.setEnabled(false);
			btnResult.setEnabled(false);
			btnRight.setEnabled(!libraryEmpty);
			btnResult.setText(getString(R.string.toolbar_state_thinking));
		} else if (transition == TRANSITION_END_DRAW) {
			if (doodleView.hasCompletedDoodle()) {
				if (currentRecognizer != null && !currentRecognizer.isCancelled()) {
					currentRecognizer.cancel(true);
				}
				if (library == null) {
					libraryLoadPendingDoodleRecognition = doodleView.getDoodle();
				} else {
					currentRecognizer = new Recognizer().execute(doodleView.getDoodle());
				}
				state = STATE_THINKING;
				btnLeft.setEnabled(false);
				btnRight.setEnabled(!libraryEmpty);
				btnResult.setText(getString(R.string.toolbar_state_thinking));
				btnResult.setEnabled(false);
			}
		} else if (transition == TRANSITION_RECOGNITION_RESULT) {
			if (state == STATE_THINKING)
			{
				String id = (String)arg;
				if (new Random().nextBoolean()) {
					btnResult.setEnabled(true);
					btnResult.setText(id);
				} else {
					btnResult.setEnabled(false);
					btnResult.setText(getString(R.string.toolbar_state_done_unknown));
				}
				state = STATE_DONE;
				state = STATE_THINKING;
				btnLeft.setEnabled(false);
				btnResult.setEnabled(false);
				btnRight.setEnabled(!libraryEmpty);
			}
		} else if (transition == TRANSITION_NEW_CONTACT_ADDED) {
			setBrowsingPosition(library.indexOfLookupKey((String)arg));
			state = STATE_BROWSING;
		} else if (transition == TRANSITION_LIBRARY_LOADED) {
			// See comment @ declaration of libraryLoadPendingDoodleRecognition
			if (libraryLoadPendingDoodleRecognition != null) {
				System.out.println("..continue recognition..");
				if (currentRecognizer != null && !currentRecognizer.isCancelled()) {
					currentRecognizer.cancel(true);
				}
				currentRecognizer = new Recognizer().execute(libraryLoadPendingDoodleRecognition);
				libraryLoadPendingDoodleRecognition = null;
			}
			
			if (!libraryEmpty) {
				btnRight.setEnabled(true);
			}
		} else if (state != STATE_BROWSING && transition == TRANSITION_BROWSE_BUTTON_RIGHT) {
			setBrowsingPosition(0);
			state = STATE_BROWSING;
		} else if (state == STATE_BROWSING && transition == TRANSITION_BROWSE_BUTTON_RIGHT) {
			setBrowsingPosition(browsePosition + 1);
		} else if (state == STATE_BROWSING && transition == TRANSITION_BROWSE_BUTTON_LEFT) {
			setBrowsingPosition(browsePosition - 1);
			if (browsePosition < 0) {
				state = STATE_WAITING;
			}
		}
	}
	
	private void setBrowsingPosition(int newpos)
	{
		browsePosition = newpos;
		if (browsePosition == -1)
		{
			btnLeft.setEnabled(false);
			btnRight.setEnabled(library.size() > 0);
			btnResult.setText(getString(R.string.toolbar_state_waiting));
			btnResult.setEnabled(false);
			doodleView.startNewDrawing();
		}
		else
		{
			btnLeft.setEnabled(true);
			btnRight.setEnabled(library.size() > browsePosition + 1);
			btnResult.setText(getContactDisplayName(library.get(browsePosition).getLookupKey()));
			btnResult.setEnabled(true);
			doodleView.setDoodle(library.get(browsePosition).getDoodle());
		}
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
        	toolbarRelativeLayout.setBackgroundColor(theme.getToolbarBackgroundColor());
        	btnResult.setTextColor(theme.getToolbarTextColor());
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