package jp.ac.tsukuba.cs.conclave.earthquake.image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Observable;


/**
 * This class holds a map Image, and a series of commands to draw on that image.
 * It deal with drawing/redrawing/clearing and otherwise manipulating the map image.
 * It is independent from the GUI, and can be used as some sort of "image logger".
 * 
 * @author caranha
 *
 */

public class MapController extends Observable implements Iterable<MapDrawCommand> {
	
	MapImage image;
	ArrayList<MapDrawCommand> drawcommands;
	
	public MapController(MapImage map)
	{
		image = map;
		drawcommands = new ArrayList<MapDrawCommand>();
	}
	
	public void addDrawCommand(MapDrawCommand c)
	{
		drawcommands.add(c);
		Collections.sort(drawcommands);
		redrawMap();
	}
	
	public boolean removeDrawCommand(MapDrawCommand c)
	{
		Boolean ret = drawcommands.remove(c);
		redrawMap();
		return ret;
	}
	public void clearDrawCommand()
	{
		drawcommands.clear();
		redrawMap();
	}

	public void redrawMap()
	{
		image.clear();
				
		for (MapDrawCommand aux: drawcommands)
		{
			if (aux.isDrawable())
				aux.draw(image);
		}
		
		setChanged();
		notifyObservers();
	}

	public MapImage getImage()
	{
		return image;
	}

	@Override
	public Iterator<MapDrawCommand> iterator() {
		return drawcommands.iterator();
	}
	
}
