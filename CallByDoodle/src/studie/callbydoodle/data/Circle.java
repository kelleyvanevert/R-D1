/*=============================================================================
 * CallByDoodle
 * 
 * CallByDoodle is an Android project created in the Spring of 2011
 * for Research & Development 1, a course the authors follow at
 * Radboud University, Nijmegen.
 * 
 * Authors:
 * Roelf Leenders
 * Kelley van Evert
 * Jochem Kooistra
 * 
 * Supervisors:
 * Erik Barendsen
 * Sjaak Smetsers
 * 
 * Project started in February of 2011, scheduled for completion 
 * in the summer of 2011.
 ============================================================================*/


package studie.callbydoodle.data;

import java.io.Serializable;

public class Circle implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private float x, y, r;
	
	public Circle(float x, float y, float radius)
	{
		this.x = x;
		this.y = y;
		this.r = radius;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public float getRadius()
	{
		return r;
	}
}
