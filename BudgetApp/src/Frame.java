import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Frame extends JFrame {
	Frame() {
		try {
			Image image = ImageIO.read(new File("zmainLogo.png"));
			this.setIconImage(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(new Panel1());
		this.pack();
		this.setAlwaysOnTop(true);
		this.setTitle("Budget 101");
		this.setVisible(true);

	}

}
