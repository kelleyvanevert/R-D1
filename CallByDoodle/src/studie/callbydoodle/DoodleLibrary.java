package studie.callbydoodle;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.Dictionary;
import java.util.Hashtable;

public class DoodleLibrary
{
	private Dictionary<String, Doodle> doodles;
	
	/**
	 * Creates a new, empty, doodle library
	 */
	public DoodleLibrary()
	{
		doodles = new Hashtable<String, Doodle>();
	}
	
	public DoodleLibrary(InputStream a)
	{
		
		try {
			ObjectInputStream i = new ObjectInputStream(a);
			
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
