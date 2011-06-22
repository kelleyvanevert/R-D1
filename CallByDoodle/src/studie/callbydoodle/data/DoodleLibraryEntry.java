package studie.callbydoodle.data;

public class DoodleLibraryEntry
{
	private String contact_id;
	private Doodle doodle;
	// TODO more.. call activity, for example..
	
	public DoodleLibraryEntry(String contact_id, Doodle doodle)
	{
		this.contact_id = contact_id;
		this.doodle = doodle;
	}
	
	public String getContactID()
	{
		return contact_id;
	}
	
	public Doodle getDoodle()
	{
		return doodle.clone();
	}
}
