package jp.ac.tsukuba.cs.conclave.earthquake.image;

import java.awt.Color;

import jp.ac.tsukuba.cs.conclave.earthquake.data.DataPoint;
import jp.ac.tsukuba.cs.conclave.earthquake.faultmodel.FaultModel;

public class DrawBothFaultPlanes extends MapDrawCommand {

	DataPoint event;
	Double dist;
	
	public DrawBothFaultPlanes(DataPoint e, double d)
	{
		event = e;
		depth = 0;
		dist = d;
	}
	
	@Override
	public void draw(MapImage img) {
		
		FaultModel fm1 = new FaultModel(event,0,dist);
		FaultModel fm2 = new FaultModel(event,1,dist);
				
		img.drawFaultModelPlane(fm1, this.mainColor, Color.red);
		img.drawFaultModelPlane(fm2, this.subColor, Color.red);
		
	}

}
