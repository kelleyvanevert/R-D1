package studie.callbydoodle.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import android.gesture.GestureStore;
import android.net.Uri;

public class DoodleLibrary
{
	/**
	 * The string is a "contact lookup key".
	 */
	private Dictionary<String, Doodle> doodles;
	
	/**
	 * Writing to strage
	 */
	private File doodleDir;
	private boolean writeToStorage = false;
	
	/**
	 * Creates a new, empty, doodle library
	 */
	public DoodleLibrary()
	{
		doodles = new Hashtable<String, Doodle>();
		writeToStorage = false;
	}
	
	/**
	 * Creates a doodles library that tries to fetch all it's info from the given directory.
	 */
	public DoodleLibrary(File doodleDir)
	{
		this();
		this.doodleDir = doodleDir;
		writeToStorage = true;
	}
	
	public boolean contactHasDoodle(String lookupKey)
	{
		return (doodles.get(lookupKey) != null);
	}
	
	public void add(String lookupKey, Doodle doodle)
	{
		doodles.put(lookupKey, doodle);
		// Temporary
		if (writeToStorage) {
			File doodleFile = new File(doodleDir.getAbsolutePath() + File.separator + lookupKey);
			try {
				FileOutputStream out = new FileOutputStream(doodleFile);
				out.write(doodle.serialize().getBytes());
			} catch (IOException e) {
				System.out.println("Doodle library write to file error");
			}
		}
	}
	
	public Doodle get(String lookupKey)
	{
		return doodles.get(lookupKey);
	}
	
	public GestureStore getGestureStore()
	{
		GestureStore s = new GestureStore();
		for (Enumeration<String> keys = doodles.keys(); keys.hasMoreElements() ;) {
			String key = keys.nextElement();
			s.addGesture(key, doodles.get(key).getGesture());
		}
		return s;
	}
}
