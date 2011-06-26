package studie.callbydoodle.data;

import java.io.Serializable;

public class DoodleLibraryEntry implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String lookup_key;
	private Doodle doodle;
	private Doodle.Specs specs;
	// TODO more.. call activity, for example..
	
	public DoodleLibraryEntry(String lookup_key, Doodle doodle)
	{
		this.lookup_key = lookup_key;
		this.doodle = doodle;
		this.specs = doodle.getSpecs();
	}
	
	public String getLookupKey()
	{
		return lookup_key;
	}
	
	public Doodle getDoodle()
	{
		return doodle.clone();
	}
	
	public Doodle.Specs getSpecs()
	{
		return specs;
	}
}
