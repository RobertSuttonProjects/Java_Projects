package frame;

import java.awt.Graphics;

import javax.swing.JPanel;

public class Panel extends JPanel{
	public int x = 0;
	public Panel(){
		this.setPreferredSize(tool.Constants.FrameCon.FRAME_DIMENSION);
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawRect(x, 0, 100, 100);
	}
}
