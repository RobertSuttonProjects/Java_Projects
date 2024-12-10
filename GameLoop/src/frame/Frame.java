package frame;

import javax.swing.JFrame;

public class Frame extends JFrame{
	public Frame(Panel panel){
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(panel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}
}
