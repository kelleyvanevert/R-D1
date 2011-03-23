package studie.callbydoodle;

import java.util.Dictionary;
import java.util.Hashtable;

public class DoodleLibrary
{
	private Dictionary<String, Doodle> library;
	
	public DoodleLibrary()
	{
		library = new Hashtable<String, Doodle>();
	}
}
