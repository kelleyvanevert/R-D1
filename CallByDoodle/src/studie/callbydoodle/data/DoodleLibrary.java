package studie.callbydoodle.data;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Pattern;

import android.gesture.GestureStore;

public class DoodleLibrary
{
	/**
	 * The string contains the "contact lookup key".
	 */
	private Dictionary<String, Doodle> doodles;
	
	/**
	 * Creates a new, empty, doodle library
	 */
	public DoodleLibrary()
	{
		doodles = new Hashtable<String, Doodle>();
	}
	
	public boolean contactHasDoodle(String contactID)
	{
		return (doodles.get(contactID) != null);
	}
	
	public void add(String contactID, Doodle doodle)
	{
		doodles.put(contactID, doodle);
	}
	
	public Doodle get(String contactID)
	{
		return doodles.get(contactID);
	}
	
	public Dictionary<String, Doodle> getDoodles()
	{
		return doodles;
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

	public static DoodleLibrary parseDoodleLibrary(String storeText) throws Exception
	{
		DoodleLibrary lib = new DoodleLibrary();
		
		Scanner s = new Scanner(storeText + "\nlastline");
		String line;
		String contactkey = null;
		StringBuffer doodleBuff = null;
		while (s.hasNextLine()) {
			line = s.nextLine() + " ";
			if (line.charAt(0) == ' ' && doodleBuff != null) {
				// another segment
				doodleBuff.append(line + "\n"); 
			} else {
				if (contactkey != null && doodleBuff != null) {
					// First save previous doodle
					try {
						lib.add(contactkey, Doodle.parseDoodle(doodleBuff.toString()));
					} catch (Exception e) {
						// Error parsing -- forget it
					}
				}
				contactkey = line.trim();
				doodleBuff = new StringBuffer();
			}
		}
		
		return lib;
	}
	
	public String serialize()
	{
		StringBuffer buf = new StringBuffer();
		
		Enumeration<String> n = doodles.keys();
		while (n.hasMoreElements()) {
			String contactkey = n.nextElement();
			Doodle doodle = (Doodle)doodles.get(contactkey);
			buf.append(contactkey + "\n" + doodle.serialize() + "\n");
		}
		
		return buf.toString();
	}
}
