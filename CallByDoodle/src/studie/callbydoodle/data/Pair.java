package studie.callbydoodle.data;

public class Pair<A, B>
{
	private A a;
	private B b;
	
	public Pair(A a, B b)
	{
		this.a = a;
		this.b = b;
	}
	
	public A A()
	{
		return a;
	}
	
	public B B()
	{
		return b;
	}
	
	public Object get(int i)
	{
		return i == 0 ? a : b;
	}
}
