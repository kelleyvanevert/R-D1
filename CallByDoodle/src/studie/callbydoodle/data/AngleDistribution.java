package studie.callbydoodle.data;

import java.io.Serializable;

public class AngleDistribution implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int[] distribution;
	
	public AngleDistribution()
	{
		distribution = new int[Specs.NUM_DISTINCT_ANGLES];
	}
	
	public AngleDistribution(AngleDistribution d)
	{
		distribution = d.distribution.clone();
	}
	
	private AngleDistribution(int[] d)
	{
		distribution = d;
	}
	
	public void increment(int angle)
	{
		try {
			distribution[angle]++;
		} catch (Exception e) {}
	}
	
	public void reset()
	{
		distribution = new int[Specs.NUM_DISTINCT_ANGLES];
	}
	
	public int count(int angle)
	{
		try {
			return distribution[angle];
		} catch (Exception e) {
			return -1;
		}
	}
	
	public AngleDistribution prepare()
	{
		final int s = Specs.NUM_DISTINCT_ANGLES;
		
		int[] result = new int[s];
		
		for (int i = 0; i < s; i++)
		{
			result[i] =
				distribution[(i - 1 + s) % s] +
				distribution[(i     + s) % s] +
				distribution[(i + 1 + s) % s];
		}
		
		return new AngleDistribution(result);
	}
	
	/**
	 * This is a measure for similarity between two angle distributions.
	 * It is always in the range [0, 100).
	 * TODO ? better way of measuring
	 */
	public static int similarity(AngleDistribution a, AngleDistribution b)
	{
		double total = 0, overlap = 0;
		for (int angle = 0; angle < Specs.NUM_DISTINCT_ANGLES; angle++) {
			total += Math.max(a.count(angle), b.count(angle));
			overlap += Math.min(a.count(angle), b.count(angle));
		}
		
		return (int)((overlap * 100.0) / total);
	}
	
	public String display()
	{
		StringBuilder str = new StringBuilder();
		
		for (int i = 0; i < Specs.NUM_DISTINCT_ANGLES; i++) {
			for (int j = 0; j < distribution[i]; j++) {
				str.append('X');
			}
			str.append("\n");
		}
		
		return str.toString();
	}
}
