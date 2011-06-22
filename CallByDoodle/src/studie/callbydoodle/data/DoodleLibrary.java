package studie.callbydoodle.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

import android.gesture.GestureStore;

public class DoodleLibrary extends ArrayList<DoodleLibraryEntry> implements Serializable
{
	@Override
	public boolean add(DoodleLibraryEntry entry)
	{
		for (int i = 0; i < size(); i++) {
			if (get(i).getContactID() == entry.getContactID()) {
				return false;
			}
		}
		return super.add(entry);
	}
	
	// More..?
	// Sort by call activity?
}
