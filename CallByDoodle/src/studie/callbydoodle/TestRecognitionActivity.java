package studie.callbydoodle;

import java.io.File;

import studie.callbydoodle.data.DoodleLibrary;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;

public class TestRecognitionActivity extends Activity
{
	private DoodleLibrary library;
	
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
    }
}
