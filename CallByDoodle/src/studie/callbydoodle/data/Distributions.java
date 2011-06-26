package studie.callbydoodle.data;

import java.util.ArrayList;

public abstract class Distributions
{
	public static int[] prepare(int[] d)
	{
		int[] result = new int[d.length];
		
		for (int i = 0; i < d.length; i++)
		{
			result[i] =
				d[(i - 1 + d.length) % d.length] +
				d[(i     + d.length) % d.length] +
				d[(i + 1 + d.length) % d.length];
		}
		
		return result;
	}
	
	public static Pair<int[], int[]> similarity(ArrayList<int[]> distributions)
	{
		if (distributions.size() == 0) {
			return null;
		}
		
		int[] min = distributions.get(0).clone();
		int[] max = distributions.get(0).clone();
		for (int d = 0; d < distributions.size(); d++) {
			for (int i = 0; i < distributions.get(d).length; i++) {
				min[i] = Math.min(min[i], distributions.get(d)[i]);
				max[i] = Math.max(max[i], distributions.get(d)[i]);
			}
		}
		
		return new Pair<int[], int[]>(min, max);
	}
	
	public static String display(int[] d)
	{
		StringBuilder str = new StringBuilder();
		
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < d[i]; j++) {
				str.append('X');
			}
			str.append("\n");
		}
		
		return str.toString();
	}
	
	public static String display_similarity(Pair<int[], int[]> sim)
	{
		StringBuilder str = new StringBuilder();
		
		for (int i = 0; i < sim.A().length; i++) {
			for (int j = 0; j < sim.A()[i]; j++) {
				str.append('-');
			}
			for (int j = sim.A()[i]; j < sim.B()[i]; j++) {
				str.append('X');
			}
			str.append("\n");
		}
		
		return str.toString();
	}
}
