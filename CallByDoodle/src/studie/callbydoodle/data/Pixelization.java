package studie.callbydoodle.data;

public class Pixelization
{
	private int[][] px;
	
	public Pixelization(int[][] px)
	{
		this.px = px;
	}
	
	public int size()
	{
		return px.length;
	}
	
	public int at(int x, int y)
	{
		return px[y][x];
	}
	
	public int area()
	{
		return px.length * px.length;
	}
	
	/**
	 * Calculate the overlap between all given pixelizations,
	 * in percentage.
	 * (If none given, -1 will be returned..)
	 * - Assumes the sizes are all correct!
	 */
	public static int overlap(Pixelization... ps)
	{
		if (ps.length == 0) {
			return -1;
		}
		
		int size = ps[0].px.length;
		double oror = 0;
		int overlap = 0;
		
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				boolean all = true, exist = false;
				for (int p = 0; p < ps.length; p++) {
					all = all && ps[p].px[y][x] == 1;
					exist = exist || ps[p].px[y][x] == 1;
				}
				if (all) {
					overlap++;
				}
				if (exist) {
					oror++;
				}
			}
		}
		
		return (int)(overlap * (100.0 / oror));
	}
	
	public String display()
	{
		StringBuilder str = new StringBuilder();
		
		for (int y = 0; y < px.length; y++) {
			for (int x = 0; x < px[y].length; x++) {
				str.append(px[y][x] == 1 ? 'X' : '.');
				str.append(' ');
			}
			str.append("\n");
		}
		
		return str.toString();
	}
	
	public Pixelization clone()
	{
		return new Pixelization(px.clone());
	}
}
