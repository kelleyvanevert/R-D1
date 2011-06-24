package studie.callbydoodle.data;

import java.io.Serializable;

public class DoodleLibraryEntry implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String lookup_key;
	private Doodle doodle;
	// TODO more.. call activity, for example..
	
	public DoodleLibraryEntry(String lookup_key, Doodle doodle)
	{
		this.lookup_key = lookup_key;
		this.doodle = doodle;
	}
	
	public String getLookupKey()
	{
		return lookup_key;
	}
	
	public Doodle getDoodle()
	{
		return doodle.clone();
	}
}
