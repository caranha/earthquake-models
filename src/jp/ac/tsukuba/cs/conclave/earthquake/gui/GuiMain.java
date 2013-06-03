package jp.ac.tsukuba.cs.conclave.earthquake.gui;

import javax.swing.JFrame;

public class GuiMain {

	
	public static void main(String[] args) {

		// Loading the data
		DataContext c = DataContext.getInstance();
		//c.init("data/catalog_fnet_1997_20130429_f3.txt","data/jma_cat_2000_2012_Mth2.5_formatted.dat");
		c.init("catalog_fnet_1997_20130429_f3.txt","jma_cat_2000_2012_Mth2.5_formatted.dat");
		
        JFrame frame = new JFrame("Earthquake FM testing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        ModelPanel p1 = new ModelPanel();               

        frame.add(p1.getPanel());
        frame.pack();	

        //Display the window.
        frame.setVisible(true);
	}
}
