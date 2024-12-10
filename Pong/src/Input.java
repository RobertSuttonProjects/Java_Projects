import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Input implements KeyListener {

	public static Input Instance;

	public Input() {
		Instance = this;
	}

	boolean wPressed = false;
	boolean sPressed = false;
	boolean upPressed = false;
	boolean downPressed = false;

	float paddleSpeed = (float) 0.2;
	float paddleMaxSpeed = (float) 5;

	public void updateL() {
		if (Panel.Instance.paddle1.ySpeed > -1 * paddleMaxSpeed && wPressed) {
			Panel.Instance.paddle1.ySpeed -= paddleSpeed;
		}
		if (Panel.Instance.paddle1.ySpeed < paddleMaxSpeed && sPressed) {
			Panel.Instance.paddle1.ySpeed += paddleSpeed;
		}
		if (!wPressed && !sPressed) {
			Panel.Instance.paddle1.ySpeed = 0;
		}
		if (Panel.Instance.paddle1.y - Panel.Instance.paddle1.ySpeed <= 0) {
			Panel.Instance.paddle1.ySpeed = 1;
		}
		if (Panel.Instance.paddle1.y + Panel.Instance.paddle1.ySpeed
				+ Panel.Instance.paddle1.height >= Constants.FRAME_HEIGHT) {
			Panel.Instance.paddle1.ySpeed = -1;
		}

	}

	public void updateR() {
		if (Panel.Instance.paddle2.ySpeed > -1 * paddleMaxSpeed && upPressed) {
			Panel.Instance.paddle2.ySpeed -= paddleSpeed;
		}
		if (Panel.Instance.paddle2.ySpeed < paddleMaxSpeed && downPressed) {
			Panel.Instance.paddle2.ySpeed += paddleSpeed;

		}
		if (!upPressed && !downPressed) {
			Panel.Instance.paddle2.ySpeed = 0;
		}
		if (Panel.Instance.paddle2.y - Panel.Instance.paddle2.ySpeed <= 0) {
			Panel.Instance.paddle2.ySpeed = 1;
		}
		if (Panel.Instance.paddle2.y + Panel.Instance.paddle2.ySpeed
				+ Panel.Instance.paddle2.height >= Constants.FRAME_HEIGHT) {
			Panel.Instance.paddle2.ySpeed = -1;
		}
	}

	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println(e.getKeyCode());
		if (e.getKeyCode() == 87) {
			wPressed = true;
		}

		if (e.getKeyCode() == 83) {
			sPressed = true;
		}
		if (e.getKeyCode() == 38) {
			upPressed = true;
		}
		if (e.getKeyCode() == 40) {
			downPressed = true;
		}
		if(e.getKeyCode() == 68) {
			Panel.Instance.usePowerup(true);
		}
		if(e.getKeyCode() == 39) {
			Panel.Instance.usePowerup(false);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 87) {
			wPressed = false;
		}

		if (e.getKeyCode() == 83) {
			sPressed = false;
		}
		if (e.getKeyCode() == 38) {
			upPressed = false;
		}
		if (e.getKeyCode() == 40) {
			downPressed = false;

		}
	}

}
