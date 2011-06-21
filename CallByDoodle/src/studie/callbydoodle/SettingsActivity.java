package studie.callbydoodle;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity
{
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch (item.getItemId()) {
    	case R.id.save_settings:
    		finish();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    /*
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
    public String getSetting(String keyName, String defaultValue)
    {
    	SharedPreferences prefs = getSharedPreferences(PREFERENCE_FILE,0);
    	return prefs.getString(keyName, defaultValue);
    }
    public void setSetting(String keyName, String value)
    {
    	SharedPreferences.Editor prefs = getSharedPreferences(PREFERENCE_FILE,0).edit(); 	
       	prefs.putString(keyName, value);
       	prefs.commit();
    }
    */
}