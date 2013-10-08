package jp.ac.tsukuba.cs.conclave.earthquake.gui.map;

/**
 * This abstract class describes something that will be drawn on the map.
 * 
 * @author caranha
 *
 */
public abstract class MapDrawCommand implements Comparable<MapDrawCommand> {

	int depth = 0;	
	
	/**
	 * 
	 * @param img
	 */
	public abstract void draw(MapImage img);	

	public void setDepth(int d)
	{
		depth = d;
	}
	
	public int getDepth()
	{
		return depth;
	}
	
	public int compareTo(MapDrawCommand o)
	{
		return (this.getDepth() - o.getDepth());
	}		
}
