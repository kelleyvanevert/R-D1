package studie.callbydoodle;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.TextView;
import android.app.Activity;
import android.content.SharedPreferences;

public class SettingsActivity extends Activity
{
	private TextView debugMsg;
	
	// Settings
	private static final String PREFERENCE_FILE  = "DoodleSettings";
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        // Use the settings layout as View for this activity
        setContentView(R.layout.settings);
        
        // Get the class of the theme to be used
        Class theme;
        try {
        	theme = Class.forName(getSetting("theme", "studie.callbydoodle.LineTheme"));
        } catch (Exception e) {
        	theme = LineTheme.class;
        }
        
        /*
        // Check the appropriate radiobutton
        if(themeSetting.equals(DoodleView.THEME_BACKGROUND))
        {
        	RadioButton button = (RadioButton) findViewById(R.id.background);
        	button.toggle();
        }
        if(themeSetting.equals(DoodleView.THEME_HUE_ROTATE))
        {
        	RadioButton button = (RadioButton) findViewById(R.id.colours);
        	button.toggle();
        }
        */
        
        //debugMsg = (TextView)findViewById(R.id.debugMsg);        
        //debugMsg.setText(themeSetting);
        
        //Fill the spinner with random data
    }
    
    /**
     * Keep track of theme changes
     * 
     * @param radioButton
     */
    public void onThemeChange(View radioButton)
    { 
    	if(radioButton.getId() == R.id.background)
    	{
    		setSetting("theme", "studie.callbydoodle.LineTheme");
    		//debugMsg.setText("Background modus geselecteerd..");
    	}
    	else if(radioButton.getId() == R.id.colours)
    	{
    		setSetting("theme", "studie.callbydoodle.ColourTheme");
    		//debugMsg.setText("Colour modus geselecteerd..");
    	}
    }
    
    /**
     * Get a user setting from memory
     * 
     * @param keyName		Setting to be retrieved
     * @param defaultValue	If no value found
     * @return				The requested setting or the defaultValue
     */
    public String getSetting(String keyName, String defaultValue)
    {
    	SharedPreferences prefs = getSharedPreferences(PREFERENCE_FILE,0);
    	return prefs.getString(keyName, defaultValue);
    }
    
    /**
     * Store a user setting from memory
     * 
     * @param keyName		Setting to be stored
     * @param value			Value that must be stored
     */
    public void setSetting(String keyName, String value)
    {
    	SharedPreferences.Editor prefs = getSharedPreferences(PREFERENCE_FILE,0).edit(); 	
       	prefs.putString(keyName, value);
       	prefs.commit();
    }
}