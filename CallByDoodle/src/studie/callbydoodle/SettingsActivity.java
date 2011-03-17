package studie.callbydoodle;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.app.Activity;

public class SettingsActivity extends Activity{
	
	private TextView debugMsg;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.settings);
        
        debugMsg = (TextView)findViewById(R.id.debugMsg);
        
        debugMsg.setText("Geladen!");
        
        //Fill the spinner with random data
        //TODO
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
    		//Do something
    		debugMsg.setText("Background modus geselecteerd..");
    	}
    	if(radioButton.getId() == R.id.colours)
    	{
    		//Do something else
    		debugMsg.setText("Colour modus geselecteerd..");
    	}    	
    }

}
