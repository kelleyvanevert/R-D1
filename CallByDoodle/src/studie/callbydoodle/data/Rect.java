package studie.callbydoodle.data;

public class Rect
{
	private int left, top, right, bottom;
	
	public Rect(int left, int top, int right, int bottom)
	{
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	
	public Rect(Vec a, Vec b)
	{
		left = Math.min(a.getX(), b.getX());
		top = Math.min(a.getY(), b.getY());
		right = Math.max(a.getX(), b.getX());
		bottom = Math.max(a.getY(), b.getY());
	}
	
	public Rect(Rect r)
	{
		left = r.Left();
		top = r.Top();
		right = r.Right();
		bottom = r.Bottom();
	}
	
	public int Left()
	{
		return left;
	}
	
	public int Top()
	{
		return top;
	}
	
	public int Right()
	{
		return right;
	}
	
	public int Bottom()
	{
		return bottom;
	}
	
	public Vec TopLeft()
	{
		return new Vec(left, top);
	}
	
	public Vec TopRight()
	{
		return new Vec(right, top);
	}
	
	public Vec BottomLeft()
	{
		return new Vec(left, bottom);
	}
	
	public Vec BottomRight()
	{
		return new Vec(right, bottom);
	}
	
	public int Width()
	{
		return right - left;
	}
	
	public int Height()
	{
		return bottom - top;
	}
	
	public Rect union(Rect other)
	{
		return new Rect(
			Math.min(left, other.Left()),
			Math.min(top, other.Top()),
			Math.max(right, other.Right()),
			Math.max(bottom, other.Bottom())
		);
	}
	
	public Rect fit(Vec v)
	{
		return new Rect(
			Math.min(left, v.getX()),
			Math.min(top, v.getY()),
			Math.max(right, v.getX()),
			Math.max(bottom, v.getY())
		);
	}
	
	@Override
	public String toString()
	{
		return "["+left+","+top+"] ["+right+","+bottom+"]";
	}
}
