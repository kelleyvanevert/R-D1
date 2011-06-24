package studie.callbydoodle.data;

import java.io.Serializable;
import java.util.ArrayList;

public class DoodleLibrary extends ArrayList<DoodleLibraryEntry> implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public boolean add(DoodleLibraryEntry entry)
	{
		for (int i = 0; i < size(); i++) {
			if (get(i).getLookupKey() == entry.getLookupKey()) {
				return false;
			}
		}
		return super.add(entry);
	}

	public int indexOfLookupKey(String lookupKey)
	{
		for (int i = 0; i < size(); i++) {
			if (get(i).getLookupKey() == lookupKey) {
				return i;
			}
		}
		
		return -1;
	}
	
	// More..?
	// Sort by call activity?
}
