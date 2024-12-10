package game;

import frame.Frame;
import frame.Panel;
import tool.Constants;

public class Game implements Runnable {


	Panel panel;
	Frame frame;
	Thread gameLoop;

	Game() {
		loadFrame();
		loadGameLoop();
		
	}

	private void paintGame() {
		panel.repaint();
	}

	private void updateGame() {
panel.x++;	}
	
	private void loadGameLoop() {
		gameLoop = new Thread(this);
		gameLoop.start();
	}
	
	private void loadFrame() {
		panel = new Panel();
		frame = new Frame(panel);
	}
	
	@Override
	public void run() {
		double timePerFrame = 1000000000 / Constants.gameLoop.fpsWanted;
		double timePerUpdate = 1000000000 / Constants.gameLoop.UPS_SET;

		long previousTime = System.nanoTime();

		double deltaU = 0;
		double deltaF = 0;

		int frames = 0;
		int updates = 0;

		long lastCheck = System.currentTimeMillis();

		while (true) {
			long currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;
			if (deltaU >= 1) {
				updateGame();
				updates++;
				deltaU--;
			}
			if (deltaF >= 1) {
				paintGame();
				frames++;
				deltaF--;
			}

			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + " UPS: " + updates);
				frames = 0;
				updates = 0;
			}

		}
	}
}